package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Responsible for selecting cells in view and calling the renderer for those drawing positions.
 *
 */
public class CellViewportRenderer implements ViewportRenderer<Cell> {

    private final int tileHeight;
    private final int tileWidth;
    private final int cellsThatFitHorizontally;
    private final int cellsThatFitVertically;
    private final Map map;

    public CellViewportRenderer(Map map, int tileHeight, int tileWidth, Vector2D windowDimensions) {
        this.map = map;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        cellsThatFitHorizontally = (windowDimensions.getXAsInt() / tileWidth) + 1;
        cellsThatFitVertically = (windowDimensions.getYAsInt() / tileHeight) + 1;
    }

    public void render(Image imageToDrawOn, Vector2D viewingVector, Renderer<Cell> renderer) throws SlickException {
        int startCellX = viewingVector.getXAsInt() / tileWidth;
        int startCellY = viewingVector.getYAsInt() / tileHeight;

        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                int drawX = ((x - startCellX) * tileWidth) - (viewingVector.getXAsInt() % tileWidth);
                int drawY = ((y - startCellY) * tileHeight) - (viewingVector.getYAsInt() % tileHeight);

                renderer.draw(imageToDrawOn.getGraphics(), map.getCell(x, y), drawX, drawY);
            }
        }
    }

}