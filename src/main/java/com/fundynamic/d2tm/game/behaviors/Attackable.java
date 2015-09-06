package com.fundynamic.d2tm.game.behaviors;


import com.fundynamic.d2tm.game.entities.Entity;

// TODO: rename, this means "I can attack something" not "I can be attacked"
public interface Attackable {

    /**
     * Orders attackable to attack
     */
    void attack(Entity entity);

}
