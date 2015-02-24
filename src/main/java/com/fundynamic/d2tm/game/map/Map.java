package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Get the exact cell on x,y directly from the internals. Only should be used for drawing (not for game logic).
     * The map class has an 'invisible border'. So a map of 64x64 is actually 66x66.
     *
     * @param x
     * @param y
     * @return
     */
    public Cell getCell(int x, int y) {
        try {
            return cells[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("You're going out of bounds!\n" +
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
                Cell cell = new Cell(this, terrainFactory.createEmptyTerrain(), x, y);
                cell.setShrouded(true);
                cells[x][y] = cell; // This is quirky, but I think Cell will become part of a list and Map will no longer be an array then.
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

    public Cell getCellByAbsolutePixelCoordinates(int pixelX, int pixelY) {
        return getCellProtected(pixelX / TILE_SIZE, pixelY / TILE_SIZE);
    }

    public Vector2D getCellCoordinatesInAbsolutePixels(int cellX, int cellY) {
        return Vector2D.create(cellX * TILE_SIZE, cellY * TILE_SIZE);
    }

    public Unit placeUnit(Unit unit) {
        Vector2D mapCoordinates = unit.getMapCoordinates();
        getCell(mapCoordinates).setEntity(unit);
        return unit;
    }

    public Structure placeStructure(Structure structure) {
        Vector2D topLeftMapCoordinates = structure.getMapCoordinates();

        for (int x = 0; x < structure.getWidthInCells(); x++) {
            for (int y = 0; y < structure.getHeightInCells(); y++) {
                getCell(topLeftMapCoordinates.getXAsInt() + x, topLeftMapCoordinates.getYAsInt() + y).setEntity(structure);
            }
        }

        return structure;
    }

    public Cell getCell(Vector2D mapCoordinates) {
        return getCell(mapCoordinates.getXAsInt(), mapCoordinates.getYAsInt());
    }

    public String getAsciiShroudMap() {
        String result = "";
        for (int y = 0; y < height; y++) {
            String line = "";
            for (int x = 0; x < width; x++) {
                if (getCell(x, y).isShrouded()) {
                    line += "#";
                } else {
                    line += ".";
                }
            }
            result += line + "\n";
        }
        return result;
    }

    public void revealShroudFor(int x, int y) {
        getCell(x, y).setShrouded(false);
    }

    public void revealShroudFor(int x, int y, int range) {
        if (range < 1) return;


        float halfATile = TILE_SIZE / 2;
        // convert to absolute pixel coordinates
        Vector2D asPixelsCentered = getCellCoordinatesInAbsolutePixels(x, y).
                add(Vector2D.create(halfATile, halfATile));

        double centerX = asPixelsCentered.getX(), centerY = asPixelsCentered.getY();

        for (int rangeStep=0; rangeStep < range; rangeStep++) { // range 'steps'

            for (int degrees=0; degrees < 360; degrees++) {

                // calculate as if we would draw a circle and remember the coordinates
                float rangeInPixels = (rangeStep * TILE_SIZE);
                double radians = Math.toRadians(degrees);

                double circleX = (centerX + (Math.cos(radians) * rangeInPixels));
                double circleY = (centerY + (Math.sin(radians) * rangeInPixels));

                // convert back the pixel coordinates back to a cell
                Cell cell = getCellByAbsolutePixelCoordinates((int) Math.ceil(circleX), (int) Math.ceil(circleY));

                cell.setShrouded(false);
            }
        }
    }

}