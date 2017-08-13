package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.game.entities.UnitMoveIntents;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * Responsible for selecting cells in view and calling the renderer for those drawing positions.
 *
 */
public class CellViewportRenderer implements ViewportRenderer<Cell> {

    private final int tileSize;
    private final int cellsThatFitHorizontally;
    private final int cellsThatFitVertically;
    private final Map map;

    public CellViewportRenderer(Map map, Vector2D windowDimensions) {
        this.map = map;
        this.tileSize = TILE_SIZE;
        cellsThatFitHorizontally = (windowDimensions.getXAsInt() / tileSize) + 1;
        cellsThatFitVertically = (windowDimensions.getYAsInt() / tileSize) + 1;
    }

    public void render(Graphics graphics, Vector2D viewingVector, Renderer<Cell> renderer) throws SlickException {
        if (graphics == null) throw new IllegalArgumentException("Graphics cannot be null");

        int startCellX = viewingVector.getXAsInt() / tileSize;
        int startCellY = viewingVector.getYAsInt() / tileSize;

        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                int drawX = ((x - startCellX) * tileSize) - (viewingVector.getXAsInt() % tileSize);
                int drawY = ((y - startCellY) * tileSize) - (viewingVector.getYAsInt() % tileSize);

                // TODO: 2 responsibilities happening here, one is culling, one is drawing
                // it is better to separate the two
                renderer.draw(graphics, map.getCell(x, y), drawX, drawY);
                boolean unitIntent = UnitMoveIntents.instance.hasIntent(MapCoordinate.create(x, y));
                if (unitIntent) {
                    graphics.setColor(Colors.DARK_RED_ALPHA_128);
                    graphics.fillRect(drawX, drawY, tileSize, tileSize);
                }
//                SlickUtils.drawShadowedText(graphics, Colors.WHITE, "" + x + "," + y, drawX, drawY);
            }
        }
    }

}