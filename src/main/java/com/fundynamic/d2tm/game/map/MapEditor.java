package com.fundynamic.d2tm.game.map;


import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import org.newdawn.slick.SlickException;

public class MapEditor {

    private final TerrainFactory terrainFactory;

    public MapEditor(TerrainFactory terrainFactory) {
        this.terrainFactory = terrainFactory;
    }

    public Map generateRandom(TerrainFactory terrainFactory, Shroud shroud, int width, int height) {
        try {
            System.out.println("Generating random map sized " + width + "x" + height);
            Map map = new Map(terrainFactory, shroud, width, height);
            putTerrainOnMap(map);
            smooth(map);
            return map;
        } catch (SlickException e) {
            System.err.println("Exception while creating map with terrainFactory: " + terrainFactory + ", width: " + width + ", height: " + height + ". --> " + e);
            return null;
        }
    }

    public void putTerrainOnMap(Map map) {
        System.out.println("Putting terrain on map");
        for (int x = 1; x <= map.getWidth(); x++) {
            for (int y = 1; y <= map.getHeight(); y++) {
                final Cell cell = map.getCell(x, y);
                final Terrain terrain = terrainFactory.create((int) (Math.random() * 7), cell);
                cell.changeTerrain(terrain);
            }
        }
    }

    public void smooth(Map map) {
        System.out.println("Smoothing all cells");
        for (int x = 1; x <= map.getWidth(); x++) {
            for (int y = 1; y <= map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
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
