package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.TerrainFacing;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Map {

    private final TerrainFactory terrainFactory;
    private Shroud shroud;
    private final int height, width;
    private final int tileHeight;
    private final int tileWidth;
    private Image mapImage;

    private Cell[][] cells;

    public Map(TerrainFactory terrainFactory, Shroud shroud, int width, int height, int tileWidth, int tileHeight) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.shroud = shroud;
        this.height = height;
        this.width = width;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        initializeEmptyMap(width, height);
    }

    public static Map generateRandom(TerrainFactory terrainFactory, Shroud shroud, int width, int height, int tileWidth, int tileHeight) {
        try {
            Map map = new Map(terrainFactory, shroud, width, height, tileWidth, tileHeight);
            map.putTerrainOnMap();
            map.smooth();
            return map;
        } catch (SlickException e) {
            System.err.println("Exception while creating map with terrainFactory: " + terrainFactory + ", width: " + width + ", height: " + height + ". --> " + e);
            return null;
        }
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

    public Image createOrGetMapImage() throws SlickException {
        if (this.mapImage == null) {
            this.mapImage = new MapRenderer(tileHeight, tileWidth, shroud).render(this); // Shroud is permanent here! :S
        }
        return mapImage;
    }

    private void initializeEmptyMap(int width, int height) {
        this.cells = new Cell[width + 2][height + 2];
        for (int x = 0; x < this.width + 2; x++) {
            for (int y = 0; y < this.height + 2; y++) {
                cells[x][y] = new Cell(terrainFactory.createEmptyTerrain());
                cells[x][y].setShrouded(false);
            }
        }
    }

    // @TODO: move this to a MapLoader / MapCreator / MapFactory / MapRepository
    private void putTerrainOnMap() {
        for (int x = 1; x <= this.width; x++) {
            for (int y = 1; y <= this.height; y++) {
                final Cell cell = cells[x][y];
                final Terrain terrain = terrainFactory.create((int) (Math.random() * 7), cell);
                cell.setShrouded((int)(Math.random() * 2) == 0);
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

    public Image getSubImage(int x, int y, int width, int height) throws SlickException {
        final Image mapImage = createOrGetMapImage();
        return mapImage.getSubImage(x, y, width, height);
    }

    public int getWidthInPixels() {
        return this.width * this.tileWidth;
    }

    public int getHeightInPixels() {
        return this.height * this.tileHeight;
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

            return TerrainFacingDeterminer.getFacing(
                    terrain.isSame(topTerrain()),
                    terrain.isSame(rightTerrain()),
                    terrain.isSame(bottomTerrain()),
                    terrain.isSame(leftTerrain())
            );
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