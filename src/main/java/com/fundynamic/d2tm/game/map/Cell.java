package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.terrain.Harvestable;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.impl.EmptyTerrain;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Cell {

    public static final int TILE_SIZE = 32;
    public static final int TILE_SIZE_ZERO_BASED = TILE_SIZE - 1;
    public static final int HALF_TILE = TILE_SIZE / 2;
    public static final int DOUBLE_TILE_SIZE = TILE_SIZE * 2;

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

    public Cell getNeighbouringCell(int relativeX, int relativeY) {
        return map.getCellWithinBoundariesOrNullObject(this.x + relativeX, this.y + relativeY);
    }

    public Cell getCellAbove() {
        return getNeighbouringCell(0, -1);
    }

    public Cell getCellLeft() {
        return getNeighbouringCell(-1, 0);
    }

    public Cell getCellBeneath() {
        return getNeighbouringCell(0, 1);
    }

    public Cell getCellRight() {
        return getNeighbouringCell(1, 0);
    }

    /**
     * Returns the absolute map coordinates in pixels (aka a {@link Coordinate} by converting its internal
     * {@link MapCoordinate} using {@link MapCoordinate#toCoordinate()}
     * @return
     */
    public Coordinate getCoordinate() {
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
                terrain.isSame(getCellTerrain(getCellAbove())),
                terrain.isSame(getCellTerrain(getCellRight())),
                terrain.isSame(getCellTerrain(getCellBeneath())),
                terrain.isSame(getCellTerrain(getCellLeft())));

        terrain.setFacing(facing);
    }

    private Terrain getCellTerrain(Cell cell) {
        if (cell == null) return EmptyTerrain.instance();
        return cell.getTerrain();
    }

    public void smoothSurroundingCells() {
        List<Cell> cells = getSurroundingCells();
        for (Cell cell : cells) {
            cell.smooth();
        }
    }

    public List<Cell> getSurroundingCells() {
        return Arrays.asList(
                getNeighbouringCell(-1, -1),
                getNeighbouringCell(0, -1),
                getNeighbouringCell(1, -1),
                getNeighbouringCell(1, 0),
                getNeighbouringCell(1, 1),
                getNeighbouringCell(0, 1),
                getNeighbouringCell(-1, 1),
                getNeighbouringCell(-1, 0)
            )
            .stream()
            .filter(el -> el != null)
            .collect(Collectors.toList());
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

    public float distance(Entity entity) {
        return this.mapCoordinate.toCoordinate().distance(entity.getCoordinate());
    }
}
