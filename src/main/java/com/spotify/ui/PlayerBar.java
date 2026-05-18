package com.spotify.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PlayerBar extends HBox {

    public final Button btnPrevious;
    public final Button btnPlayPause;
    public final Button btnNext;
    public final Button btnShuffle;
    public final Slider progressSlider;
    public final Slider volumeSlider;
    public final Label lblCurrentTime;
    public final Label lblTotalTime;
    public final Label lblTrackInfo;

    public PlayerBar() {
        btnPrevious  = new Button("⏮");
        btnPlayPause = new Button("▶");
        btnNext      = new Button("⏭");
        btnShuffle   = new Button("Shuffle");

        btnPrevious.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-text-fill: white;");
        btnPlayPause.setStyle("-fx-font-size: 20px; -fx-background-color: transparent; -fx-text-fill: white;");
        btnNext.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-text-fill: white;");
        btnShuffle.setStyle("-fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #aaaaaa;");

        HBox controls = new HBox(16, btnPrevious, btnPlayPause, btnNext, btnShuffle);
        controls.setAlignment(Pos.CENTER);

        lblTrackInfo = new Label("Aucun morceau");
        lblTrackInfo.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        lblTrackInfo.setAlignment(Pos.CENTER);

        lblCurrentTime = new Label("0:00");
        lblCurrentTime.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        lblTotalTime = new Label("0:00");
        lblTotalTime.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        progressSlider = new Slider(0, 1, 0);
        progressSlider.setStyle("-fx-accent: #1db954;");
        HBox.setHgrow(progressSlider, Priority.ALWAYS);

        HBox progressBox = new HBox(8, lblCurrentTime, progressSlider, lblTotalTime);
        progressBox.setAlignment(Pos.CENTER);

        Label lblVolume = new Label("Volume");
        lblVolume.setStyle("-fx-text-fill: white;");
        volumeSlider = new Slider(0, 1, 0.8);
        volumeSlider.setMaxWidth(100);
        volumeSlider.setStyle("-fx-accent: #1db954;");

        HBox volumeBox = new HBox(8, lblVolume, volumeSlider);
        volumeBox.setAlignment(Pos.CENTER_RIGHT);

        VBox centerBox = new VBox(4, lblTrackInfo, progressBox);
        centerBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(centerBox, Priority.ALWAYS);

        this.getChildren().addAll(controls, centerBox, volumeBox);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: #181818; -fx-border-color: #333333; -fx-border-width: 1 0 0 0;");
        this.setPrefHeight(90);
    }
}