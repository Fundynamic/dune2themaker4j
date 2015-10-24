package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class UnitMoveIntents {

    private static List<Vector2D> intendedVectors = new ArrayList<>();

    public static void addIntent(Vector2D target) {
        intendedVectors.add(target);
    }

    public static boolean hasIntentFor(Vector2D target) {
        return intendedVectors.contains(target);
    }

    public static void removeIntent(Vector2D target) {
        if (!hasIntentFor(target)) {
            throw new IllegalArgumentException("Unknown intent at " + target + ", cannot remove!\nIntents: " + intendedVectors);
        }
        intendedVectors.remove(target);
    }

}
