package com.spotify;

import com.spotify.service.LibraryService;

public class Main {
    public static void main(String[] args) {
        LibraryService library = new LibraryService();

        String musicPath = "src/main/resources/music";
        library.loadFromDirectory(musicPath);
    }
}