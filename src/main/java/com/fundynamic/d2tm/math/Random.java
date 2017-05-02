package com.fundynamic.d2tm.math;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Terrain;

import java.util.List;

public class Random {

    /**
     * Returns value between min *until* max, not including max.
     *
     * Example, if you want a number between 0 or 1, you do: getRandomBetween(0, 2)
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomBetween(int min, int max) {
        final int maxForRandom = max - min;
        return (min + getInt(maxForRandom));
    }

    public static int getInt(int max) {
        return (int) (Math.random() * max);
    }

    /**
     * Given a list of T, returns a random item from the list.
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T getRandomItem(List<T> list) {
        if (list.isEmpty()) return null;
        return list.get(getInt(list.size()));
    }
}
