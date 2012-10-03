package com.fundynamic.dune2themaker.terrain;

import com.fundynamic.dune2themaker.Theme;
import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.game.terrain.ConstructionGround;
import com.fundynamic.dune2themaker.game.terrain.Destructable;

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
