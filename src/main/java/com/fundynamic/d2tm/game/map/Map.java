package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.map.renderer.TerrainFacingDeterminer;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.TerrainFacing;
import org.newdawn.slick.SlickException;

public class Map {

    private static final int TILE_SIZE = 32; // If possible, get rid of this!

    private final TerrainFactory terrainFactory;
    private Shroud shroud;
    private final int height, width;
    private final int heightWithInvisibleBorder, widthWithInvisibleBorder;

    private Cell[][] cells;

    public Map(TerrainFactory terrainFactory, Shroud shroud, int width, int height) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.shroud = shroud;
        this.height = height;
        this.width = width;
        this.heightWithInvisibleBorder = height + 2;
        this.widthWithInvisibleBorder = width + 2;

        initializeEmptyMap();
    }

    public static Map generateRandom(TerrainFactory terrainFactory, Shroud shroud, int width, int height) {
        try {
            System.out.println("Generating random map sized " + width + "x" + height);
            Map map = new Map(terrainFactory, shroud, width, height);
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

    /**
     * Get the exact cell on x,y directly from the internals. Only should be used for drawing (not for game logic).
     * The map class has an 'invisible border'. So a map of 64x64 is actually 66x66.
     * @param x
     * @param y
     * @return
     */
    public Cell getCell(int x, int y) {
        try {
            return cells[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException( "You're going out of bounds!\n" +
                    "Parameters given: x = " + x + ", y = " + y + ".\n" +
                    "You must keep within the dimensions:\n" +
                    "Width: 0 to (not on or over!) " + widthWithInvisibleBorder + "\n" +
                    "Height: 0 to (not on or over!) " + heightWithInvisibleBorder);
        }
    }

    public Cell getCellProtected(int x, int y) {
        int correctedX = x;
        if (correctedX < 0) correctedX = 0;
        if (correctedX >= widthWithInvisibleBorder) correctedX = widthWithInvisibleBorder - 1;
        int correctedY = y;
        if (correctedY < 0) correctedY = 0;
        if (correctedY >= heightWithInvisibleBorder) correctedY = heightWithInvisibleBorder - 1;
        return getCell(correctedX, correctedY);
    }


    private void initializeEmptyMap() {
        this.cells = new Cell[widthWithInvisibleBorder][heightWithInvisibleBorder];
        for (int x = 0; x < widthWithInvisibleBorder; x++) {
            for (int y = 0; y < heightWithInvisibleBorder; y++) {
                cells[x][y] = new Cell(terrainFactory.createEmptyTerrain());
                cells[x][y].setShrouded(false);
            }
        }
    }

    // @TODO: move this to a MapLoader / MapCreator / MapFactory / MapRepository
    private void putTerrainOnMap() {
        System.out.println("Putting terrain on map");
        for (int x = 1; x <= this.width; x++) {
            for (int y = 1; y <= this.height; y++) {
                final Cell cell = getCell(x, y);
                final Terrain terrain = terrainFactory.create((int) (Math.random() * 7), cell);
                cell.setShrouded((int)(Math.random() * 2) == 0);
                cell.changeTerrain(terrain);
            }
        }
    }

    public void smooth() {
        System.out.println("Smoothing all cells");
        for (int x = 1; x <= this.width; x++) {
            for (int y = 1; y <= this.height; y++) {
                final SquareCell cell = new SquareCell(x, y);
                final Terrain terrain = cell.getTerrain();
                terrain.setFacing(cell.determineFacing());
            }
        }
    }

    public int getWidthInPixels(int tileWidth) {
        return this.width * tileWidth;
    }

    public int getHeightInPixels(int tileHeight) {
        return this.height * tileHeight;
    }

    public Perimeter createViewablePerimeter(Vector2D screenResolution, int tileWidth, int tileHeight) {
        return new Perimeter(tileWidth,
                (getWidthInPixels(tileWidth) - tileWidth) - screenResolution.getX(),
                tileHeight,
                (getHeightInPixels(tileHeight) - tileHeight) - screenResolution.getY());
    }

    public Shroud getShroud() {
        return shroud;
    }

    public Cell getCellByPixelsCoordinates(int pixelX, int pixelY) {
        return cells[pixelX / TILE_SIZE][pixelY / TILE_SIZE];
    }

    public Vector2D getVector2DByPixelsCoordinates(int pixelX, int pixelY) {
        return Vector2D.create(pixelX / TILE_SIZE, pixelY / TILE_SIZE);
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