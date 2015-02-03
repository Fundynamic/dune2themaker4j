package com.fundynamic.d2tm.game.map;

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

    public Image render(Map map) throws SlickException {
        Image image = makeImage(map.getWidth() * tileWidth, map.getHeight() * tileHeight);
        renderMap(image, map);
        return image;
    }

    protected Image makeImage(int width, int height) throws SlickException {
        return new Image(width, height);
    }

    private void renderMap(Image imageToDrawOn, Map map) throws SlickException {
        Graphics graphics = imageToDrawOn.getGraphics();
        int startCellX = 1;
        int startCellY = 1;
        int endCellX = map.getWidth();
        int endCellY = map.getHeight();
        renderCells(map, graphics, startCellX, startCellY, endCellX, endCellY);
    }

    private void renderCells(Map map, Graphics graphics, int startCellX, int startCellY, int endCellX, int endCellY) throws SlickException {
        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                Cell cell = map.getCell(x, y);
                int cellX = (x - 1) * tileWidth;
                int cellY = (y - 1) * tileHeight;
                graphics.drawImage(cell.getTileImage(), cellX, cellY);

                ShroudFacing shroudFacing = determineShroudFacing(map, x, y);
                if (shroudFacing != null) {
                    graphics.drawImage(shroud.getShroudImage(shroudFacing), cellX, cellY);
                }
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