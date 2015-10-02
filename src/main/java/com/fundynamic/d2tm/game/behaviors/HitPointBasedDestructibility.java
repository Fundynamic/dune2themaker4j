package com.fundynamic.d2tm.game.behaviors;

public class HitPointBasedDestructibility {

    private int hitPoints;

    public HitPointBasedDestructibility(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void takeDamage(int hitPoints) {
        this.hitPoints -= hitPoints;
    }

    public boolean hasDied() {
        return hitPoints < 1;
    }

    @Override
    public String toString() {
        return "HitPointBasedDestructibility{" +
                "hitPoints=" + hitPoints +
                '}';
    }

    public int getHitPoints() {
        return hitPoints;
    }
}
