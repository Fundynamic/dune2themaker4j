package com.fundynamic.dune2themaker;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import com.fundynamic.dune2themaker.game.Tile;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

public class Theme {

	private final SpriteSheet spriteSheet;

	public Theme(Image image) {
		this.spriteSheet = new SpriteSheet(image, Tile.WIDTH, Tile.HEIGHT);
	}

	public Image getTileImage(int row, TerrainFacing facing) {
        int column = facing.ordinal();
		return this.spriteSheet.getSprite(column, row);
	}

}
