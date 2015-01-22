package com.fundynamic.d2tm.game.math;

public class Vector2D {

    private final int x, y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2D moveUp() {
        return new Vector2D(x, y - 1);
    }

    public Vector2D moveDown() {
        return new Vector2D(x, y + 1);
    }

    public Vector2D moveLeft() {
        return new Vector2D(x - 1, y);
    }

    public Vector2D moveRight() {
        return new Vector2D(x + 1, y);
    }
}
