package com.spotify.controller;

import com.spotify.model.Playlist;
import com.spotify.model.Track;
import com.spotify.service.AudioPlayerService;
import com.spotify.service.LibraryService;
import com.spotify.service.PlaybackQueue;
import com.spotify.service.PlaylistService;
import com.spotify.ui.MainView;
import com.spotify.ui.PlayerBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;

import java.util.Optional;

public class MainController {

    private final LibraryService libraryService;
    private final AudioPlayerService audioPlayerService;
    private final PlaybackQueue playbackQueue;
    private final PlaylistService playlistService;
    private final ObservableList<Track> trackList;
    private final FilteredList<Track> filteredList;

    private MainView mainView;
    private boolean isPlaying = false;
    private boolean isRepeat = false;

    public MainController() {
        this.libraryService = new LibraryService();
        this.audioPlayerService = new AudioPlayerService();
        this.playbackQueue = new PlaybackQueue();
        this.playlistService = new PlaylistService();
        this.trackList = FXCollections.observableArrayList();
        this.filteredList = new FilteredList<>(trackList, p -> true);
        loadLibrary();
    }

    private void loadLibrary() {
        String musicPath = "src/main/resources/music";
        libraryService.loadFromDirectory(musicPath);
        trackList.setAll(libraryService.getTracks());
        playbackQueue.setQueue(libraryService.getTracks());
    }

    private void showAllTracks() {
        trackList.clear();
        trackList.addAll(libraryService.getTracks());
        filteredList.setPredicate(p -> true);
    }

    public void bindView(MainView view) {
        this.mainView = view;
        PlayerBar bar = view.getPlayerBar();

        audioPlayerService.totalDurationSeconds.addListener((obs, oldVal, newVal) ->
                bar.progressSlider.setMax(newVal.doubleValue())
        );

        audioPlayerService.currentTimeSeconds.addListener((obs, oldVal, newVal) -> {
            if (!bar.progressSlider.isValueChanging()) {
                bar.progressSlider.setValue(newVal.doubleValue());
            }
            bar.lblCurrentTime.setText(formatTime(newVal.doubleValue()));
        });

        audioPlayerService.totalDurationSeconds.addListener((obs, oldVal, newVal) ->
                bar.lblTotalTime.setText(formatTime(newVal.doubleValue()))
        );

        bar.lblTrackInfo.textProperty().bind(audioPlayerService.trackInfoProperty);
        bar.btnPlayPause.setOnAction(e -> togglePlayPause());
        bar.btnNext.setOnAction(e -> playNext());
        bar.btnPrevious.setOnAction(e -> playPrevious());

        bar.btnShuffle.setOnAction(e -> {
            playbackQueue.shuffle();
            bar.btnShuffle.setStyle("-fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #1db954;");
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() ->
                            bar.btnShuffle.setStyle("-fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #aaaaaa;")
                    );
                }
            }, 1000);
        });

        bar.btnRepeat.setOnAction(e -> {
            isRepeat = !isRepeat;
            if (isRepeat) {
                bar.btnRepeat.setStyle("-fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #1db954;");
            } else {
                bar.btnRepeat.setStyle("-fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #aaaaaa;");
            }
        });

        bar.volumeSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                audioPlayerService.setVolume(newVal.doubleValue())
        );

        bar.progressSlider.setOnMouseReleased(e ->
                audioPlayerService.seekTo(bar.progressSlider.getValue())
        );

        ListView<Track> listView = view.getTrackListView();
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Track selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    int index = trackList.indexOf(selected);
                    playbackQueue.setCurrentIndex(index);
                    playTrack(selected);
                }
            }
        });

        ContextMenu trackContextMenu = new ContextMenu();
        Menu addToPlaylistMenu = new Menu("Ajouter a une playlist");
        trackContextMenu.getItems().add(addToPlaylistMenu);
        listView.setContextMenu(trackContextMenu);

        trackContextMenu.setOnShowing(e -> {
            addToPlaylistMenu.getItems().clear();
            for (Playlist p : playlistService.getPlaylists()) {
                MenuItem item = new MenuItem(p.getName());
                item.setOnAction(ev -> {
                    Track selected = listView.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        playlistService.addTrackToPlaylist(p, selected);
                    }
                });
                addToPlaylistMenu.getItems().add(item);
            }
            if (addToPlaylistMenu.getItems().isEmpty()) {
                MenuItem none = new MenuItem("Aucune playlist");
                none.setDisable(true);
                addToPlaylistMenu.getItems().add(none);
            }
        });

        audioPlayerService.setOnEndOfMedia(this::playNext);

        TextField searchField = view.getSearchField();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(track -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String query = newVal.toLowerCase();
                return track.getTitle().toLowerCase().contains(query)
                        || track.getArtist().toLowerCase().contains(query);
            });
        });

        view.btnNewPlaylist.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nouvelle playlist");
            dialog.setHeaderText(null);
            dialog.setContentText("Nom de la playlist :");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                if (!name.isBlank()) playlistService.createPlaylist(name);
            });
        });

        ListView<Playlist> playlistListView = view.getPlaylistView();

        ContextMenu playlistContextMenu = new ContextMenu();
        MenuItem renameItem = new MenuItem("Renommer");
        MenuItem deleteItem = new MenuItem("Supprimer");
        playlistContextMenu.getItems().addAll(renameItem, deleteItem);
        playlistListView.setContextMenu(playlistContextMenu);

        renameItem.setOnAction(e -> {
            Playlist selected = playlistListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                TextInputDialog dialog = new TextInputDialog(selected.getName());
                dialog.setTitle("Renommer");
                dialog.setHeaderText(null);
                dialog.setContentText("Nouveau nom :");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    if (!name.isBlank()) playlistService.renamePlaylist(selected, name);
                });
            }
        });

        deleteItem.setOnAction(e -> {
            Playlist selected = playlistListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                playlistService.deletePlaylist(selected);
                showAllTracks();
            }
        });

        playlistListView.setOnMouseClicked(e -> {
            Playlist selected = playlistListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                trackList.clear();
                trackList.addAll(selected.getTracks());
                filteredList.setPredicate(p -> true);
            }
        });

        view.getBtnAllTracks().setOnAction(e -> {
            playlistListView.getSelectionModel().clearSelection();
            showAllTracks();
        });
    }

    private void playTrack(Track track) {
        audioPlayerService.play(track);
        isPlaying = true;
        mainView.getPlayerBar().btnPlayPause.setText("⏸");
    }

    private void togglePlayPause() {
        if (playbackQueue.current() == null) return;
        if (isPlaying) {
            audioPlayerService.pause();
            isPlaying = false;
            mainView.getPlayerBar().btnPlayPause.setText("▶");
        } else {
            audioPlayerService.resume();
            isPlaying = true;
            mainView.getPlayerBar().btnPlayPause.setText("⏸");
        }
    }

    private void playNext() {
        if (isRepeat) {
            Track current = playbackQueue.current();
            if (current != null) playTrack(current);
        } else {
            Track next = playbackQueue.next();
            if (next != null) playTrack(next);
        }
    }

    private void playPrevious() {
        Track prev = playbackQueue.previous();
        if (prev != null) playTrack(prev);
    }

    private String formatTime(double seconds) {
        int m = (int) seconds / 60;
        int s = (int) seconds % 60;
        return String.format("%d:%02d", m, s);
    }

    public ObservableList<Track> getTrackList() { return trackList; }
    public FilteredList<Track> getFilteredList() { return filteredList; }
    public ObservableList<Playlist> getPlaylists() { return playlistService.getPlaylists(); }
}