package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Mountain implements Terrain {

	private final Image tileImage;

	public Mountain(Image tileImage) {
		this.tileImage = tileImage;
	}

	public Image getTileImage() {
		return tileImage;
	}

}
