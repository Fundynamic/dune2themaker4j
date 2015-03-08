package com.fundynamic.d2tm.game.entities;


public class Player {

    private final String name;
    private final int colorId;

    public Player(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", colorId=" + colorId +
                '}';
    }
}
