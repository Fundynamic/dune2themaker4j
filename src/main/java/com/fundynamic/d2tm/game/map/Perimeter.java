package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.math.Vector2D;

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

    // TODO: Write test for this?
    public Vector2D makeSureVectorStaysWithin(Vector2D vec) {
        float x = Math.min(Math.max(vec.getX(), minX), maxX);
        float y = Math.min(Math.max(vec.getY(), minY), maxY);
        return Vector2D.create(x, y);
    }
}
