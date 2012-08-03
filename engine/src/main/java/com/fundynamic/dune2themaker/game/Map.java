package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Map {

	private final TerrainFactory terrainFactory;
	private final int height, width;
	private Image mapImage;

	private boolean initialized;

	private Cell[][] cells;

	public Map(TerrainFactory terrainFactory, int width, int height) throws SlickException {
		this.terrainFactory = terrainFactory;
		this.height = height;
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Cell getCell(int x, int y) {
		return cells[x][y];
	}

	public Image getMapImage() throws SlickException {
		return mapImage;
	}

	public void init() throws SlickException {
		if (!initialized) {
			// this does not work when we move the code in the constructor?
			initializeEmptyMap(width, height);
			putTerrainOnMap();
			setTerrainFacingsForCells();
			mapImage = new MapRenderer().render(this);
			initialized = true;
		}
	}

	private void initializeEmptyMap(int width, int height) {
		this.cells = new Cell[width + 2][height + 2];
		for (int x = 0; x < this.width + 2; x++) {
			for (int y = 0; y < this.height + 2; y++) {
				cells[x][y] = new Cell();
			}
		}
	}

	private void putTerrainOnMap() {
		for (int x = 1; x <= this.width; x++) {
			for (int y = 1; y <= this.height; y++) {
				final Cell cell = cells[x][y];
				final Terrain terrain = terrainFactory.create((int) (Math.random() * 7), cell);
				cell.changeTerrain(terrain);
			}
		}
	}

	private void setTerrainFacingsForCells() {

	}
}
