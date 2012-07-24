package com.fundynamic.dune2themaker.dune;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import com.fundynamic.dune2themaker.dune.terrain.Terrain;
import com.fundynamic.dune2themaker.dune.terrain.TerrainType;
import com.fundynamic.dune2themaker.dune.terrain.TerrainTypeVariant;

public class Theme {

	private final SpriteSheet spriteSheet;

	public Theme(Image image) {
		this.spriteSheet = new SpriteSheet(image, Tile.WIDTH, Tile.HEIGHT);
	}

	public Image getTileImage(TerrainType terrainType, TerrainTypeVariant cornerType) throws SlickException {
		return this.spriteSheet.getSprite(0, 0);
	}

	public Image getTileImage(Terrain terrain, TerrainTypeVariant cornerType) throws SlickException {
		final int rowOnSpriteSheet = terrain.getRowOnSpriteSheet();
		return this.spriteSheet.getSprite(0, rowOnSpriteSheet);
	}

}
