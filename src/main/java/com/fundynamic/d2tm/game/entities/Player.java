package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.math.Vector2D;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final String name;
    private final int colorId;

    private Map<Vector2D, Boolean> shrouded;

    public Player(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
        this.shrouded = new HashMap<>();
    }

    public int getColorId() {
        return colorId;
    }

    public boolean isShrouded(Vector2D position) {
        Boolean value = shrouded.get(position);
        return value != null ? value : true;
    }

    public void revealShroudFor(Vector2D position) {
        shrouded.put(position, false);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", colorId=" + colorId +
                '}';
    }

}
