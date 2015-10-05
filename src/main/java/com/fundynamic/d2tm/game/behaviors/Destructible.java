package com.fundynamic.d2tm.game.behaviors;


public interface Destructible {

    void takeDamage(int hitPoints);

    /**
     * Returns true when:
     * 1) the entity no longer lives
     * 2) the entity no longer serves any purpose
     *
     * If true, then the entity will be removed from the EntityRepository list of entities. (state)
     * This is done in the update() method in the PlayingState class. And will only be done after all entities have
     * had their 'update' cycle.
     *
     * @return boolean
     */
    boolean isDestroyed();
}
