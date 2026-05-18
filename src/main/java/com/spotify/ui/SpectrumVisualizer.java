package com.spotify.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SpectrumVisualizer extends Canvas {

    private float[] magnitudes;
    private static final int BAR_COUNT = 32;
    private static final Color BAR_COLOR = Color.web("#1db954");
    private static final Color BAR_COLOR_TOP = Color.web("#1ed760");

    public SpectrumVisualizer(double width, double height) {
        super(width, height);
        magnitudes = new float[BAR_COUNT];
        for (int i = 0; i < BAR_COUNT; i++) magnitudes[i] = -60f;
    }

    public void updateMagnitudes(float[] newMagnitudes) {
        int step = newMagnitudes.length / BAR_COUNT;
        for (int i = 0; i < BAR_COUNT; i++) {
            float sum = 0;
            for (int j = 0; j < step; j++) {
                sum += newMagnitudes[i * step + j];
            }
            float avg = sum / step;
            magnitudes[i] = Math.max(-60f, avg);
        }
        draw();
    }

    public void clear() {
        for (int i = 0; i < BAR_COUNT; i++) magnitudes[i] = -60f;
        draw();
    }

    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();

        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.web("#121212"));
        gc.fillRect(0, 0, width, height);

        double barWidth = (width / BAR_COUNT) * 0.7;
        double gap = (width / BAR_COUNT) * 0.3;

        for (int i = 0; i < BAR_COUNT; i++) {
            float magnitude = magnitudes[i];
            double normalized = (magnitude + 60) / 60.0;
            normalized = Math.max(0, Math.min(1, normalized));
            double barHeight = normalized * height;

            double x = i * (barWidth + gap);
            double y = height - barHeight;

            gc.setFill(BAR_COLOR);
            gc.fillRect(x, y, barWidth, barHeight);

            if (barHeight > 4) {
                gc.setFill(BAR_COLOR_TOP);
                gc.fillRect(x, y, barWidth, 3);
            }
        }
    }
}