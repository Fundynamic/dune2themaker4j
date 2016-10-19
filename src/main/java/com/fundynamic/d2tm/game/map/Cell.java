package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.terrain.Harvestable;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.impl.Spice;
import com.fundynamic.d2tm.game.terrain.impl.SpiceHill;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Cell {

    // TODO: decide which TILE_SIZE wins and refer to that one
    public static final int TILE_SIZE = 32;

    private final Map map;

    // TODO: Can be removed in favor of MapCoordinate?
    private final int x;
    private final int y;

    private Terrain terrain;

    private MapCoordinate mapCoordinate;
    private Image tileImage;

    public Cell(Map map, Terrain terrain, int mapX, int mapY) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        if (map == null) throw new IllegalArgumentException("Map argument may not be null");
        if (mapX < 0 || mapY < 0) throw new OutOfMapBoundsException("screenX may ot be lower than 0, for given screenX, screenY: " + mapX + "," + mapY);
        this.terrain = terrain;
        this.map = map;
        this.x = mapX;
        this.y = mapY;
        this.mapCoordinate = MapCoordinate.create(mapX, mapY);
    }

    public void changeTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    // this is an ugly seam required for testing
    public void setTileImage(Image image) {
        this.tileImage = image;
    }

    public Image getTileImage() throws SlickException {
        if (this.tileImage != null) {
            return this.tileImage;
        }
        return terrain.getTileImage();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell getCellAbove() {
        return map.getCell(this.x, this.y - 1);
    }

    public Cell getCellLeft() {
        return map.getCell(this.x - 1, this.y);
    }

    public Cell getCellBeneath() {
        return map.getCell(this.x, this.y + 1);
    }

    public Cell getCellRight() {
        return map.getCell(this.x + 1, this.y);
    }

    /**
     * Returns the absolute map coordinates in pixels (aka a {@link Coordinate} by converting its internal
     * {@link MapCoordinate} using {@link MapCoordinate#toCoordinate()}
     * @return
     */
    public Coordinate getCoordinates() {
        return mapCoordinate.toCoordinate();
    }

    public boolean isAtSameLocationAs(Cell other) {
        if (other == null) return false;
        return this.x == other.getX() && y == other.getY();
    }

    /**
     * Returns its internal {@link MapCoordinate} position (on the {@link Map}. No conversion is being done.
     * @return
     */
    public MapCoordinate getMapCoordinate() {
        return mapCoordinate;
    }

    public boolean isVisibleFor(Player controllingPlayer) {
        return !controllingPlayer.isShrouded(mapCoordinate);
    }

    public boolean isPassable(Entity entity) {
        return terrain.isPassable(entity);
    }

    public void smooth() {
        MapEditor.TerrainFacing facing = MapEditor.getFacing(
                terrain.isSame(getCellAbove().getTerrain()),
                terrain.isSame(getCellRight().getTerrain()),
                terrain.isSame(getCellBeneath().getTerrain()),
                terrain.isSame(getCellLeft().getTerrain()));

        terrain.setFacing(facing);
    }

    public void smoothSurroundingCells() {
        List<Cell> cells = getSurroundingCells();
        for (Cell cell : cells) {
            cell.smooth();
        }
    }

    public List<Cell> getSurroundingCells() {
        Cell cellAbove = getCellAbove();
        Cell cellBeneath = getCellBeneath();

        return Arrays.asList(
                cellAbove,
                cellAbove.getCellLeft(),
                cellAbove.getCellRight(),
                getCellLeft(),
                getCellRight(),
                cellBeneath,
                cellBeneath.getCellLeft(),
                cellBeneath.getCellRight()
        );
    }

    public boolean isHarvestable() {
        return terrain instanceof Harvestable;
    }

    public int harvest(int amount) {
        if (isHarvestable()) {
            return ((Harvestable) terrain).harvest(amount);
        }
        return 0;
    }
}
