package com.fundynamic.d2tm.game.map;

import org.newdawn.slick.geom.Vector2f;

public class Perimeter {

    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;

    public Perimeter(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public Vector2f makeSureVectorStaysWithin(Vector2f vec) {
        float x = Math.min(Math.max(vec.getX(), minX), maxX);
        float y = Math.min(Math.max(vec.getY(), minY), maxY);
        return new Vector2f(x, y);
    }
}
