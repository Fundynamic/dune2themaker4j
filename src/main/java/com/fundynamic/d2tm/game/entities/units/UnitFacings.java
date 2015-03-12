package com.fundynamic.d2tm.game.entities.units;


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
}
