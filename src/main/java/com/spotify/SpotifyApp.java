package com.spotify;

import com.spotify.controller.MainController;
import com.spotify.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SpotifyApp extends Application {

    @Override
    public void start(Stage stage) {
        MainController controller = new MainController();
        MainView mainView = new MainView(controller);

        controller.bindView(mainView);

        Scene scene = new Scene(mainView.getRoot(), 900, 600);

        stage.setTitle("Spotify Clone");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(400);
        stage.show();
    }
}