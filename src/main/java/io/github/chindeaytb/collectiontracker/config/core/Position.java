package io.github.chindeaytb.collectiontracker.config.core;

import com.google.gson.annotations.Expose;

public class Position {

    @Expose
    private int overlayX;
    @Expose
    private int overlayY;
    @Expose
    private float scale = 1.0f;

    public Position(int x, int y) {
        this.overlayX = x;
        this.overlayY = y;
    }

    public int getX() {
        return overlayX;
    }

    public int getY() {
        return overlayY;
    }

    public float getScale() {
        return scale;
    }

    public void setPosition(int x, int y) {
        this.overlayX = x;
        this.overlayY = y;
    }

    public void setScaling(float scale) {
        this.scale = scale;
    }

}
