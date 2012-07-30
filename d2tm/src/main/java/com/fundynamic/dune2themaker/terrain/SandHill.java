package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.DuneTerrainFactory;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class SandHill implements Terrain {

	private final Theme theme;

	public SandHill(Theme theme) {
		this.theme = theme;
	}


	public Image getTileImage() {
		return theme.getTileImage(DuneTerrainFactory.TERRAIN_SAND_HILL, null);
	}

}
