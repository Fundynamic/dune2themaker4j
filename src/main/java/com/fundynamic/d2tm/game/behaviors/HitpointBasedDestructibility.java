package com.fundynamic.d2tm.game.behaviors;

public class HitPointBasedDestructibility implements Destructible {

    private int hitPoints;

    public HitPointBasedDestructibility(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    @Override
    public void takeDamage(int hitPoints) {
        // do nothing!?
        this.hitPoints -= hitPoints;
        System.out.println("I took " + hitPoints + " damage. I now have left: " + this.hitPoints + " hitpoints.");
    }

    @Override
    public boolean isDestroyed() {
        return hitPoints < 1;
    }

    @Override
    public String toString() {
        return "HitPointBasedDestructibility{" +
                "hitPoints=" + hitPoints +
                '}';
    }
}
