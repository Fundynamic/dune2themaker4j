package com.fundynamic.d2tm.game.behaviors;


import com.fundynamic.d2tm.game.entities.Entity;

public interface Attackable {

    /**
     * Orders attackable to attack
     */
    void attack(Entity entity);

}
