package com.fundynamic.d2tm.game.math;

import org.newdawn.slick.geom.Vector2f;

import java.util.Vector;

public class Vector2D {

    private final Vector2f vec;

    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

    public static Vector2D create(float x, float y) {
        return new Vector2D(x, y);
    }

    public Vector2D(float x, float y) {
        this.vec = new Vector2f(x, y);
    }

    public float getX() {
        return vec.getX();
    }

    public float getY() {
        return vec.getY();
    }

    public Vector2D move(Vector2D velocity) {
        return move(velocity.getX(), velocity.getY());
    }

    public Vector2D move(float xVelocity, float yVelocity) {
        float newX = vec.getX() + xVelocity;
        float newY = vec.getY() + yVelocity;
        return new Vector2D(newX, newY);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + vec.getX() +
                ", y=" + vec.getX() +
                '}';
    }

    public Vector2D diff(Vector2D otherVector) {
        return new Vector2D(vec.getX() - otherVector.getX(), vec.getY() - otherVector.getY());
    }

    public int getXAsInt() {
        return (int) vec.getX();
    }

    public int getYAsInt() {
        return (int) vec.getY();
    }

    public Vector2D scale(float delta) {
        Vector2f scaled = vec.copy().scale(delta);
        return Vector2D.create(scaled.getX(), scaled.getY());
    }

    public Vector2D add(Vector2D other) {
        Vector2f added = vec.copy().add(other.vec);
        return Vector2D.create(added.getX(), added.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2D vector2D = (Vector2D) o;

        if (vec != null ? !vec.equals(vector2D.vec) : vector2D.vec != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return vec != null ? vec.hashCode() : 0;
    }

    public Vector2D div(float div) {
        float divX = getX() / div;
        float divY = getY() / div;
        return Vector2D.create(divX, divY);
    }
}
