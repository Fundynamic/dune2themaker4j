package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.game.terrain.ConstructionGround;
import com.fundynamic.dune2themaker.game.terrain.Destructable;

public class Concrete extends DuneTerrain implements ConstructionGround, Destructable {

	public Concrete(Image tileImage) {
		super(tileImage);
	}

	public int getHealth() {
		return 0;
	}

	public void damage(int hitPoints) {
	}

}
