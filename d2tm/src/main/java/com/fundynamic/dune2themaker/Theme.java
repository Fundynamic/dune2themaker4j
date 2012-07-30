package com.fundynamic.dune2themaker;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import com.fundynamic.dune2themaker.game.Tile;
import com.fundynamic.dune2themaker.terrain.TerrainFacing;

public class Theme {

	private final SpriteSheet spriteSheet;

	public Theme(Image image) {
		this.spriteSheet = new SpriteSheet(image, Tile.WIDTH, Tile.HEIGHT);
	}

	public Image getTileImage(int rowOnSpriteSheet, TerrainFacing facing) {
		return this.spriteSheet.getSprite(0, rowOnSpriteSheet);
	}

}
