package com.spotify.ui;

import com.spotify.model.Track;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TrackCell extends ListCell<Track> {

    private final HBox container;
    private final Label titleLabel;
    private final Label artistLabel;
    private final Label durationLabel;

    public TrackCell() {
        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: white;");

        artistLabel = new Label();
        artistLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        VBox leftBox = new VBox(2, titleLabel, artistLabel);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        durationLabel = new Label();
        durationLabel.setStyle("-fx-text-fill: #1db954; -fx-font-size: 11px;");

        container = new HBox(10, leftBox, durationLabel);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle("-fx-padding: 8 12 8 12; -fx-background-color: transparent;");
    }

    @Override
    protected void updateItem(Track track, boolean empty) {
        super.updateItem(track, empty);
        if (empty || track == null) {
            setGraphic(null);
            setStyle("-fx-background-color: transparent;");
        } else {
            titleLabel.setText(track.getTitle());
            artistLabel.setText(track.getArtist());
            durationLabel.setText(track.getFormattedDuration());
            setGraphic(container);
            // Hover vert foncé, sélection vert Spotify
            setStyle("-fx-background-color: transparent;");
            setOnMouseEntered(e -> setStyle("-fx-background-color: #1a3a1a;"));
            setOnMouseExited(e -> {
                if (!isSelected()) setStyle("-fx-background-color: transparent;");
            });
            selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) setStyle("-fx-background-color: #1db954;");
                else setStyle("-fx-background-color: transparent;");
            });
        }
    }
}