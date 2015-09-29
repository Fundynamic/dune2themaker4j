package com.fundynamic.d2tm.game.map;


import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrain;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.SlickException;

public class MapEditor {

    private static final int BIT_MASK_NONE = 0;
    private static final int BIT_MASK_TOP = 0x0008;
    private static final int BIT_MASK_RIGHT = 0x0004;
    private static final int BIT_MASK_BOTTOM = 0x0002;
    private static final int BIT_MASK_LEFT = 0x0001;

    private static final TerrainFacing[] FACINGS = {
            TerrainFacing.SINGLE,               // 0x0000
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

    private final TerrainFactory terrainFactory;

    public MapEditor(TerrainFactory terrainFactory) {
        this.terrainFactory = terrainFactory;
    }

    public Map generateRandom(Shroud shroud, int width, int height) {
        try {
            System.out.println("Generating random map sized " + width + "x" + height);
            Map map = new Map(shroud, width, height);
            fillMapWithRandomTerrainTypeFields(map);
            smooth(map);
            return map;
        } catch (SlickException e) {
            System.err.println("Exception while creating map with terrainFactory: " + terrainFactory + ", width: " + width + ", height: " + height + ". --> " + e);
            return null;
        }
    }

    public Map create(Shroud shroud, int width, int height, int terrainType) {
        try {
            System.out.println("Generating random map sized " + width + "x" + height);
            Map map = new Map(shroud, width, height);
            fillMapWithTerrain(map, terrainType);
            return map;
        } catch (SlickException e) {
            System.err.println("Exception while creating map with terrainFactory: " + terrainFactory + ", width: " + width + ", height: " + height + ". --> " + e);
            return null;
        }
    }

    private void fillMapWithRandomTerrainTypeFields(Map map) {
        System.out.println("Putting terrain on map");
        fillMapWithTerrain(map, DuneTerrain.TERRAIN_SAND);

        createCircularField(map, Vector2D.create(0, 0), DuneTerrain.TERRAIN_ROCK, 20);
        createCircularField(map, Vector2D.create(map.getWidth(), map.getHeight()), DuneTerrain.TERRAIN_ROCK, 20);

        for (int f = 0; f < 5; f++) {
            Vector2D randomVec = Vector2D.random(15, 45, 15, 45);
            createCircularField(map, randomVec, DuneTerrain.TERRAIN_SPICE, 6);
            createField(map, randomVec, DuneTerrain.TERRAIN_SPICE, 200);
            createField(map, randomVec, DuneTerrain.TERRAIN_SPICE_HILL, 50);
        }

        for (int f = 0; f < 5; f++) {
            Vector2D randomVec = Vector2D.random(15, 45, 15, 45);
            createCircularField(map, randomVec, DuneTerrain.TERRAIN_ROCK, 6);
            createField(map, randomVec, DuneTerrain.TERRAIN_ROCK, 100);
            createField(map, randomVec, DuneTerrain.TERRAIN_MOUNTAIN, 25);
        }

    }

    public void fillMapWithTerrain(Map map, int terrainType) {
        for (int x = 1; x <= map.getWidth(); x++) {
            for (int y = 1; y <= map.getHeight(); y++) {
                putTerrainOnCell(map, x, y, terrainType);
            }
        }
    }

    public void putTerrainOnCell(Map map, int x, int y, int terrainType) {
        final Cell cell = map.getCell(x, y);
        final Terrain terrain = terrainFactory.create(terrainType, cell);
        cell.changeTerrain(terrain);
    }

    public void createCircularField(Map map, Vector2D centerPosition, int terrainType, int size) {
        if (size < 1) return;

        int TILE_SIZE = 32;
        float halfATile = TILE_SIZE / 2;
        // convert to absolute pixel coordinates
        Vector2D asPixelsCentered = map.getCellCoordinatesInAbsolutePixels(centerPosition.getXAsInt(), centerPosition.getYAsInt()).
                add(Vector2D.create(halfATile, halfATile));

        double centerX = asPixelsCentered.getX();
        double centerY = asPixelsCentered.getY();

        for (int rangeStep=0; rangeStep < size; rangeStep++) { // range 'steps'

            for (int degrees=0; degrees < 360; degrees++) {

                // calculate as if we would draw a circle and remember the coordinates
                float rangeInPixels = (rangeStep * TILE_SIZE);
                double circleX = (centerX + (Trigonometry.cos[degrees] * rangeInPixels));
                double circleY = (centerY + (Trigonometry.sin[degrees] * rangeInPixels));

                // convert back the pixel coordinates back to a cell
                Cell cell = map.getCellByAbsoluteMapCoordinates(Vector2D.create((int) Math.ceil(circleX), (int) Math.ceil(circleY)));

                putTerrainOnCell(map, cell.getX(), cell.getY(), terrainType);
            }
        }

    }

    public void createField(Map map, Vector2D startVector, int terrainType, int size) {
        Vector2D position = startVector;

        for (int i = 0; i < size; i++) {
            position = position.add(Vector2D.create(-1 + Random.getInt(3), -1 + Random.getInt(3)));

            Cell cellProtected = map.getCellProtected(position.getXAsInt(), position.getYAsInt());
            if (!cellProtected.getCoordinatesAsVector2D().equals(position)) {
                position = cellProtected.getCoordinatesAsVector2D();
            }

            putTerrainOnCell(map, position.getXAsInt(), position.getYAsInt(), terrainType);
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
    public enum TerrainFacing {
        FULL, // completely surrounded
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        TOP_LEFT,
        RIGHT_BOTTOM,
        TOP_RIGHT,
        BOTTOM_LEFT,
        SINGLE, // no neighbours of same 'type'
        TOP_BOTTOM,
        TOP_RIGHT_BOTTOM,
        TOP_BOTTOM_LEFT,
        RIGHT_BOTTOM_LEFT,
        TOP_RIGHT_LEFT,
        RIGHT_LEFT
    }

}
