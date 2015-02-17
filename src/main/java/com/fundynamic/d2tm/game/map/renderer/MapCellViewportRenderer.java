package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Responsible for selecting cells in view and calling the renderer for those drawing positions.
 *
 */
public class MapCellViewportRenderer implements ViewportRenderer<MapCell> {

    private final int tileHeight;
    private final int tileWidth;
    private final int cellsThatFitHorizontally;
    private final int cellsThatFitVertically;
    private final Map map;

    public MapCellViewportRenderer(Map map, int tileHeight, int tileWidth, Vector2D windowDimensions) {
        this.map = map;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        cellsThatFitHorizontally = (windowDimensions.getX() / tileWidth) + 1;
        cellsThatFitVertically = (windowDimensions.getY() / tileHeight) + 1;
    }

    public void render(Image imageToDrawOn, Vector2f viewingVector, Renderer<MapCell> renderer) throws SlickException {
        int startCellX = (int)(viewingVector.getX() / tileWidth);
        int startCellY = (int)(viewingVector.getY() / tileHeight);

        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                int drawX = ((x - startCellX) * tileWidth) - ((int)viewingVector.getX() % tileWidth);
                int drawY = ((y - startCellY) * tileHeight) - ((int)viewingVector.getY() % tileHeight);

                renderer.draw(imageToDrawOn.getGraphics(), new MapCell(map, x, y), drawX, drawY);
            }
        }
    }

}