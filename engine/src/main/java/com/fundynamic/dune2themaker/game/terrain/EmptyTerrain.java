package com.fundynamic.dune2themaker.game.terrain;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.game.Tile;

public class EmptyTerrain implements Terrain {

	private final Image image;
	private static Image blackImage = null;

	public EmptyTerrain() {
		try {
			if (blackImage == null) {
				blackImage = new Image(Tile.WIDTH, Tile.HEIGHT);
				blackImage.getGraphics().setColor(Color.black);
				blackImage.getGraphics().fillRect(0, 0, Tile.WIDTH, Tile.HEIGHT);
			}
			this.image = blackImage;
		} catch (SlickException e) {
			throw new IllegalStateException("Cannot create empty image of 32x32");
		}
	}

	private EmptyTerrain(Image image) {
		this.image = image;
	}

	public static EmptyTerrain testInstance() {
		return new EmptyTerrain(null);
	}

	public Image getTileImage() {
		return image;
	}

	public void setFacing(TerrainFacing terrainFacing) {
	}

	public boolean isSame(Terrain terrain) {
		if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
		return true;
	}
}