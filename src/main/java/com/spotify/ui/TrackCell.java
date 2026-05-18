package com.spotify.ui;

import com.spotify.model.Track;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

public class TrackCell extends ListCell<Track> {

    private final HBox container;
    private final Label titleLabel;
    private final Label artistLabel;
    private final Label durationLabel;

    public TrackCell() {
        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        artistLabel = new Label();
        artistLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        VBox leftBox = new VBox(2, titleLabel, artistLabel);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        durationLabel = new Label();
        durationLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        container = new HBox(10, leftBox, durationLabel);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle("-fx-padding: 8 12 8 12;");
    }

    @Override
    protected void updateItem(Track track, boolean empty) {
        super.updateItem(track, empty);
        if (empty || track == null) {
            setGraphic(null);
        } else {
            titleLabel.setText(track.getTitle());
            artistLabel.setText(track.getArtist());
            durationLabel.setText(track.getFormattedDuration());
            setGraphic(container);
        }
    }
}