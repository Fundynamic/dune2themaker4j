package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;

/**
 * Interface that marks something that can be rendered on the screen. It is told where it should render
 * itself by providing screen X and Y coordinates to the render function. This implies an AbsoluteRenderable
 * is drawn by something else which does a translation between its game {@link Coordinate} and
 * and screen coordinates.
 *
 */
public interface AbsoluteRenderable {

    /**
     * Call render logic.
     *
     * @param graphics
     */
    void render(Graphics graphics, int x, int y);

}
