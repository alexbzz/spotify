package com.spotify.controller;

import com.spotify.model.Track;
import com.spotify.service.AudioPlayerService;
import com.spotify.service.LibraryService;
import com.spotify.service.PlaybackQueue;
import com.spotify.ui.MainView;
import com.spotify.ui.PlayerBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class MainController {

    private final LibraryService libraryService;
    private final AudioPlayerService audioPlayerService;
    private final PlaybackQueue playbackQueue;
    private final ObservableList<Track> trackList;

    private MainView mainView;
    private boolean isPlaying = false;

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
    }

    public void bindView(MainView view) {
        this.mainView = view;
        PlayerBar bar = view.getPlayerBar();

        audioPlayerService.totalDurationSeconds.addListener((obs, oldVal, newVal) -> {
            bar.progressSlider.setMax(newVal.doubleValue());
        });

        audioPlayerService.currentTimeSeconds.addListener((obs, oldVal, newVal) -> {
            // ⚠️ On ne met à jour que si l'utilisateur ne drag pas le slider
            if (!bar.progressSlider.isValueChanging()) {
                bar.progressSlider.setValue(newVal.doubleValue());
            }
            bar.lblCurrentTime.setText(formatTime(newVal.doubleValue()));
        });

        audioPlayerService.totalDurationSeconds.addListener((obs, oldVal, newVal) -> {
            bar.lblTotalTime.setText(formatTime(newVal.doubleValue()));
        });
        bar.lblTrackInfo.textProperty().bind(audioPlayerService.trackInfoProperty);
        bar.btnPlayPause.setOnAction(e -> togglePlayPause());
        bar.btnNext.setOnAction(e -> playNext());
        bar.btnPrevious.setOnAction(e -> playPrevious());
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

        audioPlayerService.setOnEndOfMedia(this::playNext);
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
        Track next = playbackQueue.next();
        if (next != null) playTrack(next);
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
}