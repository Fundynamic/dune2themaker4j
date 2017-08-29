package com.fundynamic.d2tm.game.behaviors;


import com.fundynamic.d2tm.game.entities.Entity;

public interface Destructible {

    void takeDamage(int hitPoints, Entity origin);

    int getHitPoints();
}
