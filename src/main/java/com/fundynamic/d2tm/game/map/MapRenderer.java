package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.ShroudFacing;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class MapRenderer {

    private final int tileHeight;
    private final int tileWidth;
    private final Shroud shroud;

    public MapRenderer(int tileHeight, int tileWidth, Shroud shroud) {
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.shroud = shroud;
    }

    public void render(Image imageToDrawOn, Vector2D viewingVector, Vector2D windowDimensions, Map map) throws SlickException {
        int startCellX = (viewingVector.getX() / tileWidth);
        int startCellY = (viewingVector.getY() / tileHeight);
        int cellsThatFitHorizontally = (windowDimensions.getX() / tileWidth) + 1; // one extra
        int cellsThatFitVertically = (windowDimensions.getY() / tileHeight) + 1; // one extra!
        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        Graphics graphics = imageToDrawOn.getGraphics();

        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                Cell cell = map.getCell(x, y);
                int cellX = ((x - startCellX) * tileWidth) - (viewingVector.getX() % tileWidth);
                int cellY = ((y - startCellY) * tileHeight) - (viewingVector.getY() % tileHeight);
                graphics.drawImage(cell.getTileImage(), cellX, cellY);
            }
        }
    }

    private ShroudFacing determineShroudFacing(Map map, int x, int y) {
        if (map.getCell(x,y).isShrouded()) {
            return ShroudFacing.FULL;
        }

        return ShroudFacingDeterminer.getFacing(
                isShrouded(map, x, y -1),
                isShrouded(map, x +1, y),
                isShrouded(map, x, y +1),
                isShrouded(map, x -1, y)
        );
    }

    private boolean isShrouded(Map map, int x, int y) {
        if (x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight()) {
            return true;
        }
        return map.getCell(x, y).isShrouded();
    }
}