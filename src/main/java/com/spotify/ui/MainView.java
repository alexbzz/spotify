package com.spotify.ui;

import com.spotify.controller.MainController;
import com.spotify.model.Track;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView {

    private final BorderPane root;
    private final ListView<Track> trackListView;
    public final PlayerBar playerBar;

    public MainView(MainController controller) {
        Label sidebarTitle = new Label("Bibliothèque");
        sidebarTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        VBox sidebar = new VBox(16, sidebarTitle);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(180);
        sidebar.setStyle("-fx-background-color: #000000;");
        Label tracklistTitle = new Label("Tous les morceaux");
        tracklistTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");
        tracklistTitle.setPadding(new Insets(16, 16, 8, 16));

        trackListView = new ListView<>();
        trackListView.setItems(controller.getTrackList());
        trackListView.setCellFactory(lv -> new TrackCell());
        trackListView.setStyle("-fx-background-color: #121212;");

        VBox centerBox = new VBox(0, tracklistTitle, trackListView);
        centerBox.setStyle("-fx-background-color: #121212;");
        VBox.setVgrow(trackListView, javafx.scene.layout.Priority.ALWAYS);

        playerBar = new PlayerBar();

        root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(centerBox);
        root.setBottom(playerBar);
        root.setStyle("-fx-background-color: #121212;");
    }

    public BorderPane getRoot() { return root; }
    public ListView<Track> getTrackListView() { return trackListView; }
}