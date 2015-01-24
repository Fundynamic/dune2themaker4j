package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.math.Vector2D;

public class Perimeter<T extends Number> {

    private final T minX;
    private final T maxX;

    private final T minY;
    private final T maxY;

    public Perimeter(T minX, T maxX, T minY, T maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public Vector2D makeSureVectorStaysWithin(Vector2D vec) {
        Number vecX = vec.getX();
        Number vecY = vec.getY();
        if (vecX.floatValue() < minX.floatValue()) vecX = minX;
        if (vecX.floatValue() > maxX.floatValue()) vecX = maxX;
        if (vecY.floatValue() < minY.floatValue()) vecY = minY;
        if (vecY.floatValue() > maxY.floatValue()) vecY = maxY;
        return new Vector2D<>(vecX, vecY);
    }
}
