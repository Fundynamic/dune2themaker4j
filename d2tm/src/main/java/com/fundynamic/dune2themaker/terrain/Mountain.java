package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Mountain implements Terrain {

	private final Theme theme;

	public Mountain(Theme theme) {
		this.theme = theme;
	}


	public Image getTileImage() {
		return theme.getTileImage(this, TerrainFacing.FULL);
	}
}
