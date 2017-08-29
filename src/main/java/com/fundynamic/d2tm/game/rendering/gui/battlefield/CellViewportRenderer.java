package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.UnitMoveIntents;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * Responsible for selecting cells in view and calling the renderer for those drawing positions.
 *
 */
public class CellViewportRenderer implements ViewportRenderer<Cell> {

    private final Map map;
    private final Vector2D windowDimensions;

    public CellViewportRenderer(Map map, Vector2D windowDimensions) {
        this.map = map;
        this.windowDimensions = windowDimensions;
    }

    public Rectangle getViewport(Vector2D viewingVector) {
        Vector2D bottomRight = viewingVector.add(windowDimensions);
        MapCoordinate cellAlignedTopLeft = MapCoordinate.create(
            viewingVector.getXAsInt() / TILE_SIZE,
            viewingVector.getYAsInt() / TILE_SIZE);
        MapCoordinate cellAlignedBottomRight = MapCoordinate.create(
            bottomRight.getXAsInt() / TILE_SIZE + (bottomRight.getXAsInt() % TILE_SIZE == 0 ? 0 : 1),
            bottomRight.getYAsInt() / TILE_SIZE + (bottomRight.getYAsInt() % TILE_SIZE == 0 ? 0 : 1));

        return new Rectangle(
            cellAlignedTopLeft.getXAsInt(), cellAlignedTopLeft.getYAsInt(),
            cellAlignedBottomRight.getXAsInt(), cellAlignedBottomRight.getYAsInt());
    }

    public void render(Graphics graphics, Vector2D viewingVector, Renderer<Cell> renderer) throws SlickException {
        if (graphics == null) throw new IllegalArgumentException("Graphics cannot be null");

        Rectangle visibleCellRange = getViewport(viewingVector);
        for (int x = visibleCellRange.getTopLeftX(); x < visibleCellRange.getBottomRightX(); x++) {
            for (int y = visibleCellRange.getTopLeftY(); y < visibleCellRange.getBottomRightY(); y++) {
                // calculate the draw offset
                int drawX = ((x - visibleCellRange.getTopLeftX()) * TILE_SIZE) - viewingVector.getXAsInt() % TILE_SIZE;
                int drawY = ((y - visibleCellRange.getTopLeftY()) * TILE_SIZE) - viewingVector.getYAsInt() % TILE_SIZE;

                // TODO: 2 responsibilities happening here, one is culling, one is drawing
                // it is better to separate the two
                renderer.draw(graphics, map.getCell(x, y), drawX, drawY);
                if (Game.DEBUG_INFO) {
                    boolean unitIntent = UnitMoveIntents.instance.hasIntent(MapCoordinate.create(x, y));
                    if (unitIntent) {
                        graphics.setColor(Colors.DARK_RED_ALPHA_128);
                        graphics.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    }
//                    SlickUtils.drawShadowedText(graphics, Colors.WHITE, "" + x + "," + y, drawX, drawY);
                }
            }
        }
    }


}
