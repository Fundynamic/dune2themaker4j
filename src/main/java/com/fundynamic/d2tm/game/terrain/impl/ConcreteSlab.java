package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.terrain.ConstructionGround;
import com.fundynamic.d2tm.game.terrain.Destructable;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.Color;

public class ConcreteSlab extends DuneTerrain implements ConstructionGround, Destructable {

    private static final Color terrainColor = new Color(101, 101, 76);

    public ConcreteSlab(Theme theme) {
        super(theme);
    }

    @Override
    public int getTerrainType() {
        return DuneTerrain.CONCRETE;
    }

    @Override
    public Color getTerrainColor() {
        return terrainColor;
    }

    public int getHealth() {
        return 0;
    }

    public void doDamage(int hitPoints) {
        // nothing happens yet when damage is taken
    }

}