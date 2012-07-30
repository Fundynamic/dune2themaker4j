package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Sand implements Terrain {

	private final Image tileImage;

	public Sand(Image tileImage) {
		this.tileImage = tileImage;
	}

	public Image getTileImage() {
		return tileImage;
	}

}
