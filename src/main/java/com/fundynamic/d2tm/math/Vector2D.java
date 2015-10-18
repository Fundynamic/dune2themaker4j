package com.fundynamic.d2tm.math;

import org.newdawn.slick.geom.Vector2f;

public class Vector2D {

    private final Vector2f vec;

    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

    public static Vector2D create(float x, float y) {
        return new Vector2D(x, y);
    }

    /**
     * Generate a vector with random (int) coordinates. Where X will be between 0 and given maxX and Y will be between
     * 0 and maxY.
     *
     * @param maxX
     * @param maxY
     * @return new Vector instance
     */
    public static Vector2D random(int maxX, int maxY) {
        return random(0, maxX, 0, maxY);
    }

    /**
     * Generate a vector with random (int) coordinates. Where X will be between minX and given maxX and Y will be between
     * minY and maxY.
     *
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @return new Vector instance
     */
    public static Vector2D random(int minX, int maxX, int minY, int maxY) {
        return Vector2D.create(Random.getRandomBetween(minX, maxX), Random.getRandomBetween(minY, maxY));
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
                ", y=" + vec.getY() +
                '}';
    }

    public Vector2D diff(Vector2D otherVector) {
        return new Vector2D(vec.getX() - otherVector.getX(), vec.getY() - otherVector.getY());
    }

    public Vector2D min(Vector2D vecToSubstract) {
        return new Vector2D(vec.getX() - vecToSubstract.getX(), vec.getY() - vecToSubstract.getY());
    }

    public Vector2D min(int x, int y) {
        return new Vector2D(vec.getX() - x, vec.getY() - y);
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

    public Vector2D add(int correctX, int correctY) {
        return Vector2D.create(getX() + correctX, getY() + correctY);
    }

    public Vector2D div(float amount) {
        Vector2f scaled = vec.copy().scale(1F / amount);
        return Vector2D.create(scaled.getX(), scaled.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2D vector2D = (Vector2D) o;

        return vec.equals(vector2D.vec);
    }

    @Override
    public int hashCode() {
        return vec != null ? vec.hashCode() : 0;
    }

    public Vector2D normalise() {
        Vector2f normalised = vec.getNormal();
        return create(normalised.getX(), normalised.getY());
    }

    public float distance(Vector2D target) {
        return vec.distance(new Vector2f(target.getX(), target.getY()));
    }

    public double getAngle() {
        return vec.getTheta();
    }

    public double angleTo(Vector2D target) {
        Vector2D direction = target.min(this);
        direction = Vector2D.create(direction.getX(), direction.getY() * -1);
        return direction.getAngle();
    }

}
