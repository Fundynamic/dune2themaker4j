package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.game.terrain.ConstructionGround;
import com.fundynamic.dune2themaker.game.terrain.Destructable;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Concrete implements Terrain, ConstructionGround, Destructable {

	private final Image tileImage;

	public Concrete(Image tileImage) {
		this.tileImage = tileImage;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public int getHealth() {
		return 0;
	}

	public void damage(int hitPoints) {
	}

}
