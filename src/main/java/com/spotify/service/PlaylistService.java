package com.spotify.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.spotify.model.Playlist;
import com.spotify.model.Track;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {

    private static final String SAVE_PATH = "playlists.json";
    private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public PlaylistService() {
        load();
    }

    public Playlist createPlaylist(String name) {
        Playlist playlist = new Playlist(name);
        playlists.add(playlist);
        save();
        return playlist;
    }

    public void deletePlaylist(Playlist playlist) {
        playlists.remove(playlist);
        save();
    }

    public void renamePlaylist(Playlist playlist, String newName) {
        playlist.setName(newName);
        save();
        playlists.set(playlists.indexOf(playlist), playlist);
    }

    public void addTrackToPlaylist(Playlist playlist, Track track) {
        playlist.addTrack(track);
        save();
    }

    public void save() {
        try {
            List<PlaylistData> data = new ArrayList<>();
            for (Playlist p : playlists) {
                PlaylistData pd = new PlaylistData();
                pd.name = p.getName();
                pd.tracks = new ArrayList<>();
                for (Track t : p.getTracks()) {
                    TrackData td = new TrackData();
                    td.title = t.getTitle();
                    td.artist = t.getArtist();
                    td.duration = t.getDuration();
                    td.filePath = t.getFilePath();
                    pd.tracks.add(td);
                }
                data.add(pd);
            }
            mapper.writeValue(new File(SAVE_PATH), data);
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    public void load() {
        File file = new File(SAVE_PATH);
        if (!file.exists()) return;
        try {
            List<PlaylistData> data = mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(List.class, PlaylistData.class));
            for (PlaylistData pd : data) {
                Playlist p = new Playlist(pd.name);
                for (TrackData td : pd.tracks) {
                    p.addTrack(new Track(td.title, td.artist, td.duration, td.filePath));
                }
                playlists.add(p);
            }
        } catch (IOException e) {
            System.out.println("Erreur chargement : " + e.getMessage());
        }
    }

    public ObservableList<Playlist> getPlaylists() { return playlists; }

    public static class PlaylistData {
        public String name;
        public List<TrackData> tracks;
    }

    public static class TrackData {
        public String title;
        public String artist;
        public double duration;
        public String filePath;
    }
}