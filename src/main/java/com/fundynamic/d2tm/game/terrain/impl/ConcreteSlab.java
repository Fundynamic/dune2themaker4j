package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.terrain.ConstructionGround;
import com.fundynamic.d2tm.game.terrain.Destructable;
import com.fundynamic.d2tm.graphics.Theme;

public class ConcreteSlab extends DuneTerrain implements ConstructionGround, Destructable {

    public ConcreteSlab(Theme theme) {
        super(theme);
    }

    @Override
    public int getTerrainType() {
        return DuneTerrain.CONCRETE;
    }

    public int getHealth() {
        return 0;
    }

    public void doDamage(int hitPoints) {
    }

}