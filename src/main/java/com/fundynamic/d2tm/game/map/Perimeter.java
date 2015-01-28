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

    public Vector2D makeSureVectorStaysWithin(Vector2D vec) {
        float vecX = vec.getX();
        float vecY = vec.getY();
        if (vecX < minX) vecX = minX;
        if (vecX > maxX) vecX = maxX;
        if (vecY < minY) vecY = minY;
        if (vecY > maxY) vecY = maxY;
        return new Vector2D(vecX, vecY);
    }
}
