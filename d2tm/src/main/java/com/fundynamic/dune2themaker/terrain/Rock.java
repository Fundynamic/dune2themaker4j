package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.ConstructionGround;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Rock implements Terrain, ConstructionGround {

	private final Theme theme;

	public Rock(Theme theme) {
		this.theme = theme;
	}

	public Image getTileImage() {
		return theme.getTileImage(this, null);
	}

}
