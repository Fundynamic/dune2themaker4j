package com.fundynamic.d2tm.game.behaviors;


import com.fundynamic.d2tm.game.entities.Entity;

/**
 * This thing can destroy others, by means of attacking.
 *
 */
public interface Destroyer {

    /**
     * Orders attackable to attack
     */
    void attack(Entity entity);

}
