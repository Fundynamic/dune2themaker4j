package com.fundynamic.dune2themaker.game.map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.game.TerrainFactory;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

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
		if (this.mapImage == null) {
			mapImage = new MapRenderer().render(this);
		}
		return mapImage;
	}

	public void init() throws SlickException {
		if (!initialized) {
			// this does not work when we move the code in the constructor?
			initializeEmptyMap(width, height);
			putTerrainOnMap();
            smooth();
            initialized = true;
		}
	}

	private void initializeEmptyMap(int width, int height) {
		this.cells = new Cell[width + 2][height + 2];
		for (int x = 0; x < this.width + 2; x++) {
			for (int y = 0; y < this.height + 2; y++) {
				cells[x][y] = new Cell(terrainFactory.createEmptyTerrain());
			}
		}
	}

	// @TODO: move this to a MapLoader / MapCreator / MapFactory / MapRepository
	private void putTerrainOnMap() {
		for (int x = 1; x <= this.width; x++) {
			for (int y = 1; y <= this.height; y++) {
				final Cell cell = cells[x][y];
				final Terrain terrain = terrainFactory.create((int) (Math.random() * 7), cell);
				cell.changeTerrain(terrain);
			}
		}
	}

	public void smooth() {
		for (int x = 1; x <= this.width; x++) {
            for (int y = 1; y <= this.height; y++) {
                final SquareCell cell = new SquareCell(x, y);
                final Terrain terrain = cell.getTerrain();
                terrain.setFacing(cell.determineFacing());
			}
		}
	}

    private class SquareCell {
        private final int x, y;

        private SquareCell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Terrain getTerrain() {
            return cells[x][y].getTerrain();
        }

        private TerrainFacing determineFacing() {
            Terrain terrain = getTerrain();
            FacingDeterminer facingDeterminer = new FacingDeterminer();
            facingDeterminer.setTopSame(terrain.isSame(topTerrain()));
            facingDeterminer.setRightSame(terrain.isSame(rightTerrain()));
            facingDeterminer.setBottomSame(terrain.isSame(bottomTerrain()));
            facingDeterminer.setLeftSame(terrain.isSame(leftTerrain()));
            return facingDeterminer.getFacing();
        }

        private Cell getCell(int x, int y) {
            return cells[x][y];
        }

        private Terrain leftTerrain() {
            return getCell(x - 1, y).getTerrain();
        }

        private Terrain bottomTerrain() {
            return getCell(x, y + 1).getTerrain();
        }

        private Terrain rightTerrain() {
            return getCell(x + 1, y).getTerrain();
        }

        private Terrain topTerrain() {
            return getCell(x, y - 1).getTerrain();
        }
    }



}
