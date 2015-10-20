package com.fundynamic.d2tm.game.entities.units;


import com.fundynamic.d2tm.math.Vector2D;

import java.util.HashMap;
import java.util.Map;

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

}
