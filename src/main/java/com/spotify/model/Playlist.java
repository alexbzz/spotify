package com.spotify.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String name;
    private List<Track> tracks;

    public Playlist(String name) {
        this.name = name;
        this.tracks = new ArrayList<>();
    }

    public void addTrack(Track track) {
        if (!tracks.contains(track)) tracks.add(track);
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Track> getTracks() { return tracks; }

    @Override
    public String toString() { return name; }
}