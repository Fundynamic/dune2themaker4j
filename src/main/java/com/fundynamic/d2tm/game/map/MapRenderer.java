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
        /**
         * some thoughts:
         *
         * Basically this is 'selecting' a bunch of cells worthy to be drawn, and then draws them.
         * What is to be drawed should not matter really, what matters is that something within the range is drawable, and then gets
         * drawed.
         *
         * For now there is 'terrain' and 'shroud'. Frankly these could be "Drawables" or some sort. Perhaps passed in this
         * method so the actual drawing is delegated ie:
         *
         * void draw(Graphics graphics, int mapX, int mapY, int drawX, int drawY) {
         *   Cell cell = map.getCell(mapX, mapY);
         *   graphics.drawImage(cell.getTileImage(), drawX, drawY); // draw terrain
         * }
         *
         * that could also be done for shroud, the same way, and then we could have 'layers' one level up in the stack
         * (in the playingState)?
         */
        int startCellX = (viewingVector.getX() / tileWidth);
        int startCellY = (viewingVector.getY() / tileHeight);
        int cellsThatFitHorizontally = (windowDimensions.getX() / tileWidth) + 1; // one extra
        int cellsThatFitVertically = (windowDimensions.getY() / tileHeight) + 1; // one extra!
        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        Graphics graphics = imageToDrawOn.getGraphics();

        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                int cellX = ((x - startCellX) * tileWidth) - (viewingVector.getX() % tileWidth);
                int cellY = ((y - startCellY) * tileHeight) - (viewingVector.getY() % tileHeight);

                // insert drawable/callback here? (cellX == drawX, cellY == drawY)
                // ie: drawable.draw(graphics, x, y, cellX, cellY);
                Cell cell = map.getCell(x, y);
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