package com.spotify.service;

import com.spotify.model.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaybackQueue {

    private List<Track> queue = new ArrayList<>();
    private int currentIndex = -1;

    public void setQueue(List<Track> tracks) {
        queue = new ArrayList<>(tracks);
        currentIndex = 0;
    }

    public void setCurrentIndex(int index) {
        if (index >= 0 && index < queue.size()) {
            currentIndex = index;
        }
    }

    public Track current() {
        if (currentIndex < 0 || currentIndex >= queue.size()) return null;
        return queue.get(currentIndex);
    }

    public Track next() {
        if (queue.isEmpty()) return null;
        currentIndex = (currentIndex + 1) % queue.size();
        return queue.get(currentIndex);
    }

    public Track previous() {
        if (queue.isEmpty()) return null;
        currentIndex = (currentIndex - 1 + queue.size()) % queue.size();
        return queue.get(currentIndex);
    }

    public void shuffle() {
        Track current = current();
        Collections.shuffle(queue);
        if (current != null) {
            queue.remove(current);
            queue.add(0, current);
            currentIndex = 0;
        }
    }

    public int getCurrentIndex() { return currentIndex; }
    public List<Track> getQueue() { return queue; }
    public boolean isEmpty() { return queue.isEmpty(); }
}