package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.game.terrain.ConstructionGround;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Rock implements Terrain, ConstructionGround {

	private final Image tileImage;

	public Rock(Image tileImage) {
		this.tileImage = tileImage;
	}

	public Image getTileImage() {
		return tileImage;
	}

}
