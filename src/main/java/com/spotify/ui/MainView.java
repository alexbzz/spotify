package com.spotify.ui;

import com.spotify.controller.MainController;
import com.spotify.model.Playlist;
import com.spotify.model.Track;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainView {

    private final BorderPane root;
    private final ListView<Track> trackListView;
    private final ListView<Playlist> playlistView;
    private final PlayerBar playerBar;
    private final Button btnAllTracks;
    public final TextField searchField;
    public final Button btnNewPlaylist;

    public MainView(MainController controller) {
        Label sidebarTitle = new Label("Ma Bibliotheque");
        sidebarTitle.setStyle("-fx-text-fill: #1db954; -fx-font-weight: bold; -fx-font-size: 14px;");

        btnAllTracks = new Button("Tous les morceaux");
        btnAllTracks.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand;");
        btnAllTracks.setMaxWidth(Double.MAX_VALUE);
        btnAllTracks.setOnMouseEntered(e -> btnAllTracks.setStyle("-fx-background-color: #1a3a1a; -fx-text-fill: #1db954; -fx-font-size: 12px; -fx-cursor: hand;"));
        btnAllTracks.setOnMouseExited(e -> btnAllTracks.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand;"));

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #333333;");

        Label playlistsLabel = new Label("Playlists");
        playlistsLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px; -fx-font-weight: bold;");

        btnNewPlaylist = new Button("+ Nouvelle playlist");
        btnNewPlaylist.setStyle("-fx-background-color: transparent; -fx-text-fill: #1db954; -fx-font-size: 12px; -fx-cursor: hand;");
        btnNewPlaylist.setMaxWidth(Double.MAX_VALUE);
        btnNewPlaylist.setOnMouseEntered(e -> btnNewPlaylist.setStyle("-fx-background-color: #1a3a1a; -fx-text-fill: #1db954; -fx-font-size: 12px; -fx-cursor: hand;"));
        btnNewPlaylist.setOnMouseExited(e -> btnNewPlaylist.setStyle("-fx-background-color: transparent; -fx-text-fill: #1db954; -fx-font-size: 12px; -fx-cursor: hand;"));

        playlistView = new ListView<>();
        playlistView.setItems(controller.getPlaylists());
        playlistView.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        playlistView.setPrefHeight(200);
        VBox.setVgrow(playlistView, Priority.ALWAYS);

        VBox sidebar = new VBox(12, sidebarTitle, btnAllTracks, sep, playlistsLabel, btnNewPlaylist, playlistView);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #000000;");

        searchField = new TextField();
        searchField.setPromptText("Rechercher un morceau...");
        searchField.setStyle(
                "-fx-background-color: #2a2a2a;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #aaaaaa;" +
                        "-fx-border-color: #1db954;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 13px;"
        );
        searchField.setPadding(new Insets(8));

        Label tracklistTitle = new Label("Tous les morceaux");
        tracklistTitle.setStyle("-fx-text-fill: #1db954; -fx-font-weight: bold; -fx-font-size: 15px;");

        trackListView = new ListView<>();
        trackListView.setItems(controller.getFilteredList());
        trackListView.setCellFactory(lv -> new TrackCell());
        trackListView.setStyle("-fx-background-color: #121212; -fx-border-color: transparent;");

        VBox centerBox = new VBox(8, tracklistTitle, searchField, trackListView);
        centerBox.setPadding(new Insets(16));
        centerBox.setStyle("-fx-background-color: #121212;");
        VBox.setVgrow(trackListView, Priority.ALWAYS);

        playerBar = new PlayerBar();

        root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(centerBox);
        root.setBottom(playerBar);
        root.setStyle("-fx-background-color: #121212;");
    }

    public BorderPane getRoot() { return root; }
    public ListView<Track> getTrackListView() { return trackListView; }
    public ListView<Playlist> getPlaylistView() { return playlistView; }
    public PlayerBar getPlayerBar() { return playerBar; }
    public TextField getSearchField() { return searchField; }
    public Button getBtnAllTracks() { return btnAllTracks; }
}