package com.fundynamic.d2tm.game.entities.units;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.math.Vector2D;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

/**
 * This class represents all possible Dune 2 facings. So we don't support C&C sprites. If we want to do so we need to
 * make sure the amount of facings should be dynamic (8 or more).
 */
public enum UnitFacings {
    RIGHT(0),
    RIGHT_UP(1),
    UP(2),
    LEFT_UP(3),
    LEFT(4),
    LEFT_DOWN(5),
    DOWN(6),
    RIGHT_DOWN(7);

    private static Map<Integer, UnitFacings> facingsById;
    static {
        facingsById = new HashMap<>();
        for (UnitFacings facing : UnitFacings.values()) {
            facingsById.put(facing.getValue(), facing);
        }
    }

    private final int value;

    UnitFacings(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UnitFacings byId(int id) {
        return facingsById.get(id);
    }

    /**
     * A very straightforward method to determine the facing given a 'from' and 'to' position
     *
     * This is using the UnitFacings class (enum values). The method getFacingInt is more flexible.
     *
     * @deprecated getFacingInt is favored over this.
     *
     * @param from
     * @param to
     * @return
     */
    @Deprecated
    public static UnitFacings determine(Vector2D from, Vector2D to) {
        boolean left = to.getXAsInt() < from.getXAsInt();
        boolean right = to.getXAsInt() > from.getXAsInt();
        boolean up = to.getYAsInt() < from.getYAsInt();
        boolean down = to.getYAsInt() > from.getYAsInt();

        if (up && left) return UnitFacings.LEFT_UP;
        if (up && right) return UnitFacings.RIGHT_UP;
        if (down && left) return UnitFacings.LEFT_DOWN;
        if (down && right) return UnitFacings.RIGHT_DOWN;
        if (up) return UnitFacings.UP;
        if (down) return UnitFacings.DOWN;
        if (left) return UnitFacings.LEFT;
        if (right) return UnitFacings.RIGHT;

        return UnitFacings.RIGHT;
    }

    @Deprecated // it is still based on 8 facings
    public static int nextFacing(int current, int desired) {
        // Decide what the nextId (facing) should be
        int newId = current + facingDirection(current, desired);

        // make sure we stay within range
        if (newId < 0) newId = 7;
        if (newId > 7) newId = 0;

        return newId;
    }

    public static int facingDirection(int current, int desired) {
        int counterClockwise = getCounterClockwiseSteps(current, desired);
        int clockwise = getClockwiseSteps(current, desired);

        // Decide what the nextId (facing) should be
        if (counterClockwise < clockwise) {
            return 1; // go counter-clockwise, which means go 'right' on the sprite
        }
        return -1; // go clock-wise which means go 'left' on the sprite
    }

    public static int getClockwiseSteps(int currentId, int desiredId) {
        int clockwise = abs(currentId - (desiredId - 7));
        if (clockwise > 7) clockwise -= 8;
        return clockwise;
    }

    public static int getCounterClockwiseSteps(int currentId, int desiredId) {
        int counterClockwise;
        if (desiredId < currentId) {
            counterClockwise = abs((currentId + desiredId) - 8);
        } else {
            counterClockwise = abs(desiredId - currentId);
        }
        return counterClockwise;
    }

    // TODO: this looks very similar to the Projectile determine facing logic, move to somewhere else!?
    public static UnitFacings getFacing(Vector2D from, Vector2D to) {
        int facings = 8; // 8 facings in total for a unit
        return byId(calculateFacingSpriteIndex(from, to, facings));
    }

    public static UnitFacings getFacing(Entity from, Entity to) {
        return getFacing(from.getCenteredCoordinateOfEntity(), to.getCenteredCoordinateOfEntity());
    }

    public static int getFacingInt(Entity from, Entity to) {
        return getFacing(from.getCenteredCoordinateOfEntity(), to.getCenteredCoordinateOfEntity()).getValue();
    }

    public static int getFacingInt(Entity from, Vector2D to) {
        return getFacing(from.getCenteredCoordinateOfEntity(), to).getValue();
    }

    public static int getFacingInt(Vector2D from, Vector2D to) {
        return getFacing(from, to).getValue();
    }

    public static int calculateFacingSpriteIndex(Vector2D from, Vector2D to, int facings) {
        return calculateFacingSpriteIndex(from, to, facings, 360F / facings);
    }

    public static int calculateFacingSpriteIndex(Vector2D from, Vector2D to, int facings, float chop) {
        double angle = from.angleTo(to);
        angle += (chop / 2);
        return (int) (angle / chop) % facings;
    }

    /**
     * Given the current facing (float) param, the desired (int) facing and a given turnspeed. Turn towards the
     * desired facing with the given turn speed. Returns the new facing (float).
     *
     * Makes sure flipping from facing 7 to 0 (or the other way around) happens.
     *
     * @param facing
     * @param desiredFacing
     * @param turnSpeed
     * @return
     */
    public static float turnTo(float facing, int desiredFacing, float turnSpeed) {
        float newFacing = facing + (facingDirection((int) facing, desiredFacing) * turnSpeed);
        if (newFacing < 0) newFacing = 7;
        if (newFacing > 7) newFacing = 0;
        return newFacing;
    }

}
