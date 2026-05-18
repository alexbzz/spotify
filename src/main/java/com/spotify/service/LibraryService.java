package com.spotify.service;

import com.spotify.model.Track;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryService {

    private List<Track> tracks = new ArrayList<>();
    public List<Track> loadFromDirectory(String directoryPath) {
        Path folder = Paths.get(directoryPath);

        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            System.out.println(" Attention dossier introuvable : " + directoryPath);
            return new ArrayList<>();
        }

        try (Stream<Path> stream = Files.walk(folder)) {
            tracks = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.endsWith(".mp3") || name.endsWith(".wav");
                    })
                    .map(p -> {
                        String fileName = p.getFileName().toString();
                        String title = fileName.replaceAll("\\.(mp3|wav)$", "");
                        return new Track(title, "Inconnu", 0, p.toAbsolutePath().toString());
                    })
                    .sorted(Comparator.comparing(Track::getTitle))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.out.println(" Attention erreur lors du scan : " + e.getMessage());
        }

        System.out.println(":)" + tracks.size() + " morceaux chargés depuis " + directoryPath);
        tracks.forEach(System.out::println);

        return tracks;
    }

    public List<Track> getTracks() { return tracks; }

    public List<Track> search(String query) {
        String q = query.toLowerCase();
        return tracks.stream()
                .filter(t -> t.getTitle().toLowerCase().contains(q)
                        || t.getArtist().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Track> sortByTitle() {
        return tracks.stream()
                .sorted(Comparator.comparing(Track::getTitle))
                .collect(Collectors.toList());
    }

    public List<Track> sortByArtist() {
        return tracks.stream()
                .sorted(Comparator.comparing(Track::getArtist))
                .collect(Collectors.toList());
    }
}