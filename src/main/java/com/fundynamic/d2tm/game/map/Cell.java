package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cell {

    public static final int TILE_SIZE = 32;

    private final Map map;
    private final int x;
    private final int y;

    private Terrain terrain;

    private MapCoordinate mapCoordinate;
    private Image tileImage;

    public Cell(Map map, Terrain terrain, int mapX, int mapY) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        if (map == null) throw new IllegalArgumentException("Map argument may not be null");
        if (mapX < 0 || mapY < 0) throw new OutOfMapBoundsException("x may ot be lower than 0, for given x, y: " + mapX + "," + mapY);
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

    public Coordinate getCoordinates() {
        return getMapCoordinate().toCoordinate();
    }

    public boolean isAtSameLocationAs(Cell other) {
        if (other == null) return false;
        return this.x == other.getX() && y == other.getY();
    }

    public MapCoordinate getMapCoordinate() {
        return mapCoordinate;
    }

    public boolean isVisibleFor(Player controllingPlayer) {
        return !controllingPlayer.isShrouded(mapCoordinate);
    }

    public boolean isPassable(Entity entity) {
        return terrain.isPassable(entity);
    }
}
