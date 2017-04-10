package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.terrain.Harvestable;
import org.newdawn.slick.Image;

/**
 * A stub for harvestable terrain used for testing purposes
 */
public class HarvestableTerrain extends EmptyTerrain implements Harvestable {

    private int amount;

    public HarvestableTerrain(Image image, int amount) {
        super(image);
        this.amount = amount;
    }

    @Override
    public int harvest(int amount) {
        return this.amount -= amount;
    }
}
