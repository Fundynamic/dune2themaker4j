package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.ConstructionGround;
import com.fundynamic.dune2themaker.game.terrain.Destructable;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Concrete implements Terrain, ConstructionGround, Destructable {

	private final Theme theme;

	public Concrete(Theme theme) {
		this.theme = theme;
	}

	public Image getTileImage() {
		return theme.getTileImage(this, null);
	}

	public int getHealth() {
		return 0;
	}

	public void damage(int hitPoints) {
	}

}
