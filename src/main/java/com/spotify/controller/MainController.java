package com.spotify.controller;

import com.spotify.model.Track;
import com.spotify.service.AudioPlayerService;
import com.spotify.service.LibraryService;
import com.spotify.service.PlaybackQueue;
import com.spotify.ui.MainView;
import com.spotify.ui.PlayerBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController {

    private final LibraryService libraryService;
    private final AudioPlayerService audioPlayerService;
    private final PlaybackQueue playbackQueue;
    private final ObservableList<Track> trackList;
    private FilteredList<Track> filteredList;

    private MainView mainView;
    private boolean isPlaying = false;
    private boolean isRepeat = false;

    public MainController() {
        this.libraryService = new LibraryService();
        this.audioPlayerService = new AudioPlayerService();
        this.playbackQueue = new PlaybackQueue();
        this.trackList = FXCollections.observableArrayList();
        loadLibrary();
    }

    private void loadLibrary() {
        String musicPath = "src/main/resources/music";
        libraryService.loadFromDirectory(musicPath);
        trackList.setAll(libraryService.getTracks());
        playbackQueue.setQueue(libraryService.getTracks());
        filteredList = new FilteredList<>(trackList, p -> true);
    }

    public void bindView(MainView view) {
        this.mainView = view;
        PlayerBar bar = view.getPlayerBar();

        // --- Barre de progression ---
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

        // --- Info morceau ---
        bar.lblTrackInfo.textProperty().bind(audioPlayerService.trackInfoProperty);

        // --- Boutons ---
        bar.btnPlayPause.setOnAction(e -> togglePlayPause());
        bar.btnNext.setOnAction(e -> playNext());
        bar.btnPrevious.setOnAction(e -> playPrevious());

        // --- Shuffle : flash vert 1 seconde puis retour gris ---
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

        // --- Repeat : toggle vert/gris ---
        bar.btnRepeat.setOnAction(e -> {
            isRepeat = !isRepeat;
            if (isRepeat) {
                bar.btnRepeat.setStyle("-fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #1db954;");
            } else {
                bar.btnRepeat.setStyle("-fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #aaaaaa;");
            }
        });

        // --- Volume ---
        bar.volumeSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                audioPlayerService.setVolume(newVal.doubleValue())
        );

        // --- Seek ---
        bar.progressSlider.setOnMouseReleased(e ->
                audioPlayerService.seekTo(bar.progressSlider.getValue())
        );

        // --- Double clic sur un morceau ---
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

        // --- Fin de morceau ---
        audioPlayerService.setOnEndOfMedia(this::playNext);

        // --- Recherche ---
        TextField searchField = view.getSearchField();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(track -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String query = newVal.toLowerCase();
                return track.getTitle().toLowerCase().contains(query)
                        || track.getArtist().toLowerCase().contains(query);
            });
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
}