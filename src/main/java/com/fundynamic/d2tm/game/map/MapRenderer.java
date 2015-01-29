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
        renderMap(image.getGraphics(), map);
        return image;
    }

    protected Image makeImage(int width, int height) throws SlickException {
        return new Image(width, height);
    }

    private void renderMap(Graphics graphics, Map map) throws SlickException {
        for (int x = 1; x <= map.getWidth(); x++) {
            for (int y = 1; y <= map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                int cellX = (x - 1) * tileWidth;
                int cellY = (y - 1) * tileHeight;
                graphics.drawImage(cell.getTileImage(), cellX, cellY);

                ShroudFacing shroudFacing = determineFacing(map, x, y);
                if (shroudFacing != null) {
                    graphics.drawImage(shroud.getShroudImage(shroudFacing), cellX, cellY);
                }
            }
        }
    }

    private ShroudFacing determineFacing(Map map, int x, int y) {
        if (map.getCell(x,y).isShrouded()) {
            return ShroudFacing.FULL;
        }

        ShroudFacingDeterminer facingDeterminer = new ShroudFacingDeterminer();
        facingDeterminer.setTopShrouded(isShrouded(map, x, y -1));
        facingDeterminer.setRightShrouded(isShrouded(map, x +1, y));
        facingDeterminer.setBottomShrouded(isShrouded(map, x, y +1));
        facingDeterminer.setLeftShrouded(isShrouded(map, x -1, y));
        return facingDeterminer.getFacing();
    }

    private boolean isShrouded(Map map, int x, int y) {
        if (x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight()) {
            return true;
        }
        return map.getCell(x, y).isShrouded();
    }
}