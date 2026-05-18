package com.spotify.service;

import com.spotify.model.Track;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class AudioPlayerService {

    private MediaPlayer mediaPlayer;
    private Track currentTrack;

    public final DoubleProperty currentTimeSeconds = new SimpleDoubleProperty(0);
    public final DoubleProperty totalDurationSeconds = new SimpleDoubleProperty(0);
    public final StringProperty trackInfoProperty = new SimpleStringProperty("Aucun morceau");

    //Appelé a la fin du morceau
    private Runnable onEndOfMedia;

    public void play(Track track) {
        //Arrete l'ancien player
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        currentTrack = track;
        trackInfoProperty.set(track.getTitle() + " — " + track.getArtist());

        String uri = new File(track.getFilePath()).toURI().toString();
        Media media = new Media(uri);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            double duration = mediaPlayer.getTotalDuration().toSeconds();
            Platform.runLater(() -> {
                totalDurationSeconds.set(duration);
                track.setDuration(duration);
            });
            mediaPlayer.play();
        });


        mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() ->
                    currentTimeSeconds.set(newVal.toSeconds())
            );
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            if (onEndOfMedia != null) {
                Platform.runLater(onEndOfMedia);
            }
        });
    }

    public void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public void resume() {
        if (mediaPlayer != null) mediaPlayer.play();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            Platform.runLater(() -> {
                currentTimeSeconds.set(0);
                trackInfoProperty.set("Aucun morceau");
            });
        }
    }

    public void seekTo(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) mediaPlayer.setVolume(volume);
    }

    public boolean isPlaying() {
        return mediaPlayer != null &&
                mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void setOnEndOfMedia(Runnable callback) {
        this.onEndOfMedia = callback;
    }

    public Track getCurrentTrack() { return currentTrack; }
}