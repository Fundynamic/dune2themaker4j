package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.graphics.Theme;

public class Concrete extends DuneTerrain implements ConstructionGround, Destructable {

    public Concrete(Theme theme) {
        super(theme);
    }

    @Override
    protected int getTerrainType() {
        return DuneTerrain.CONCRETE;
    }

    public int getHealth() {
        return 0;
    }

    public void damage(int hitPoints) {
    }

}