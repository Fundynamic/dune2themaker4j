package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.terrain.Terrain;
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
                cell.setShrouded(false);
                cells[x][y] = cell; // This is quirky, but I think Cell will become part of a list and Map will no longer be an array then.
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
                cell.setShrouded((int) (Math.random() * 2) == 0);
                cell.changeTerrain(terrain);
            }
        }
    }

    // TODO: This is more of a MapEditor method
    public void smooth() {
        System.out.println("Smoothing all cells");
        for (int x = 1; x <= this.width; x++) {
            for (int y = 1; y <= this.height; y++) {
                Cell cell = getCell(x, y);
                final Terrain terrain = cell.getTerrain();

                TerrainFacing facing = getFacing(
                        terrain.isSame(cell.getCellAbove().getTerrain()),
                        terrain.isSame(cell.getCellRight().getTerrain()),
                        terrain.isSame(cell.getCellBeneath().getTerrain()),
                        terrain.isSame(cell.getCellLeft().getTerrain()));

                terrain.setFacing(facing);
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
        return getCell(pixelX / TILE_SIZE, pixelY / TILE_SIZE);
    }

    // the reference to StructuresRepository.StructureData is a bit awkward
    public Unit placeUnit(Unit unit) {
        Vector2D mapCoordinates = unit.getMapCoordinates();
        getCell(mapCoordinates).setEntity(unit);
        return unit;
    }

    // the reference to StructuresRepository.StructureData is a bit awkward
    // TODO: Add test for this method!
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


    private static final int BIT_MASK_NONE = 0;
    private static final int BIT_MASK_TOP = 0x0008;
    private static final int BIT_MASK_RIGHT = 0x0004;
    private static final int BIT_MASK_BOTTOM = 0x0002;
    private static final int BIT_MASK_LEFT = 0x0001;

    private static final TerrainFacing[] FACINGS = {
            TerrainFacing.MIDDLE,               // 0x0000
            TerrainFacing.TOP_RIGHT_BOTTOM,     // 0x0001
            TerrainFacing.TOP_RIGHT_LEFT,       // 0x0010
            TerrainFacing.TOP_RIGHT,            // 0x0011
            TerrainFacing.TOP_BOTTOM_LEFT,      // 0x0100
            TerrainFacing.TOP_BOTTOM,           // 0x0101
            TerrainFacing.TOP_LEFT,             // 0x0110
            TerrainFacing.TOP,                  // 0x0111
            TerrainFacing.RIGHT_BOTTOM_LEFT,    // 0x1000
            TerrainFacing.RIGHT_BOTTOM,         // 0x1001
            TerrainFacing.RIGHT_LEFT,           // 0x1010
            TerrainFacing.RIGHT,                // 0x1011
            TerrainFacing.BOTTOM_LEFT,          // 0x1100
            TerrainFacing.BOTTOM,               // 0x1101
            TerrainFacing.LEFT,                 // 0x1110
            TerrainFacing.FULL,                 // 0x1111
    };

    public static TerrainFacing getFacing(boolean isTopSame, boolean isRightSame, boolean isBottomSame, boolean isLeftSame) {
        int value = 0;
        value |= isTopSame ? BIT_MASK_TOP : BIT_MASK_NONE;
        value |= isRightSame ? BIT_MASK_RIGHT : BIT_MASK_NONE;
        value |= isBottomSame ? BIT_MASK_BOTTOM : BIT_MASK_NONE;
        value |= isLeftSame ? BIT_MASK_LEFT : BIT_MASK_NONE;
        return FACINGS[value];
    }

    /**
     * This enum has types for all kind of facing for a specific TerrainType. (non-walls)
     * <p/>
     * The types are explained by looking at the graphical representation, and then pointing out
     * at what sides the terrain type is drawn.
     * <p/>
     * Example:
     * TerrainType = Rock
     * Looking at spritesheet, of rock.
     * The very first tile is a full rock tile, so the first type in the enum is FULL
     * The second tile (one to the right), is a tile with sand on the left, rock on the top, right, bottom.
     * (we go clockwise), so the enum is TOP_RIGHT_BOTTOM
     */
    public static enum TerrainFacing {
        FULL,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        TOP_LEFT,
        RIGHT_BOTTOM,
        TOP_RIGHT,
        BOTTOM_LEFT,
        MIDDLE,
        TOP_BOTTOM,
        TOP_RIGHT_BOTTOM,
        TOP_BOTTOM_LEFT,
        RIGHT_BOTTOM_LEFT,
        TOP_RIGHT_LEFT,
        RIGHT_LEFT
    }

}