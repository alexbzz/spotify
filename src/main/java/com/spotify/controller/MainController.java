package com.spotify.controller;

import com.spotify.model.Track;
import com.spotify.service.LibraryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainController {

    private final LibraryService libraryService;
    private final ObservableList<Track> trackList;

    public MainController() {
        this.libraryService = new LibraryService();
        this.trackList = FXCollections.observableArrayList();
        loadLibrary();
    }

    private void loadLibrary() {
        String musicPath = "src/main/resources/music";
        libraryService.loadFromDirectory(musicPath);
        trackList.setAll(libraryService.getTracks());
    }

    public ObservableList<Track> getTrackList() {
        return trackList;
    }
}