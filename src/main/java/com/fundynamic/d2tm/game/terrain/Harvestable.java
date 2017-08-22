package com.fundynamic.d2tm.game.terrain;

public interface Harvestable {

    /**
     * Reduces the amount of 'resource (spice)' on a tile. Returns the actual withdrawn amount.
     * @param amount
     * @return
     */
    float harvest(float amount);

}
