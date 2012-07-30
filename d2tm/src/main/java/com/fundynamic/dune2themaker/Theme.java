package com.fundynamic.dune2themaker;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import com.fundynamic.dune2themaker.game.Tile;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.terrain.TerrainFacing;

public class Theme {

	private final SpriteSheet spriteSheet;

	public Theme(Image image) {
		this.spriteSheet = new SpriteSheet(image, Tile.WIDTH, Tile.HEIGHT);
	}

	public Image getTileImage(Terrain terrain, TerrainFacing corner) {
		final int rowOnSpriteSheet = terrain.getRowOnSpriteSheet();
		return this.spriteSheet.getSprite(0, rowOnSpriteSheet);
	}

}
