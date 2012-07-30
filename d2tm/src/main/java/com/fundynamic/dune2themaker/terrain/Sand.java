package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Sand implements Terrain {

	private final Theme theme;

	public Sand(Theme theme) {
		this.theme = theme;
	}


	public Image getTileImage() {
		return theme.getTileImage(this, null);
	}

}
