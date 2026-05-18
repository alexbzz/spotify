package com.spotify.model;

public class Track {

    private String title;
    private String artist;
    private double duration;
    private String filePath;

    public Track(String title, String artist, double duration, String filePath) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.filePath = filePath;
    }
    //get
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public double getDuration() { return duration; }
    public String getFilePath() { return filePath; }
    //set
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setDuration(double duration) { this.duration = duration; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    //affichage de la durée d'un son
    public String getFormattedDuration() {
        if (duration <= 0) return "--:--";
        int minutes = (int) duration / 60;
        int seconds = (int) duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return title + " - " + artist + " (" + getFormattedDuration() + ")";
    }
}