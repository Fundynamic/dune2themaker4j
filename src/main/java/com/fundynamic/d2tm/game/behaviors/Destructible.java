package com.fundynamic.d2tm.game.behaviors;


public interface Destructible {

    void takeDamage(int hitPoints);

    boolean isDestroyed();
}
