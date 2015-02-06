package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Responsible for selecting cells in view and calling the renderer for those drawing positions.
 *
 */
public class MapRenderer {

    private final int tileHeight;
    private final int tileWidth;
    private final int cellsThatFitHorizontally;
    private final int cellsThatFitVertically;

    public MapRenderer(int tileHeight, int tileWidth, Vector2D windowDimensions) {
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        cellsThatFitHorizontally = (windowDimensions.getX() / tileWidth) + 1;
        cellsThatFitVertically = (windowDimensions.getY() / tileHeight) + 1;
    }

    public void render(Image imageToDrawOn, Vector2D viewingVector, CellRenderer cellRenderer) throws SlickException {
        int startCellX = (viewingVector.getX() / tileWidth);
        int startCellY = (viewingVector.getY() / tileHeight);

        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                int drawX = ((x - startCellX) * tileWidth) - (viewingVector.getX() % tileWidth);
                int drawY = ((y - startCellY) * tileHeight) - (viewingVector.getY() % tileHeight);

                cellRenderer.draw(imageToDrawOn.getGraphics(), x, y, drawX, drawY);
            }
        }
    }

}