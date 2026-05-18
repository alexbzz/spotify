package com.spotify.ui;

import com.spotify.controller.MainController;
import com.spotify.model.Track;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainView {

    private final BorderPane root;
    private final ListView<Track> trackListView;
    private final PlayerBar playerBar;
    public final TextField searchField;

    public MainView(MainController controller) {
        // --- SIDEBAR ---
        Label sidebarTitle = new Label("Ma Bibliotheque");
        sidebarTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        VBox sidebar = new VBox(16, sidebarTitle);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(180);
        sidebar.setStyle("-fx-background-color: #000000;");

        searchField = new TextField();
        searchField.setPromptText("Rechercher un morceau...");
        searchField.setStyle(
                "-fx-background-color: #2a2a2a;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #aaaaaa;" +
                        "-fx-border-color: #444444;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 13px;"
        );
        searchField.setPadding(new Insets(8));

        Label tracklistTitle = new Label("Tous les morceaux");
        tracklistTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");

        trackListView = new ListView<>();
        trackListView.setItems(controller.getFilteredList());
        trackListView.setCellFactory(lv -> new TrackCell());
        trackListView.setStyle("-fx-background-color: #121212;");

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
    public PlayerBar getPlayerBar() { return playerBar; }
    public TextField getSearchField() { return searchField; }
}