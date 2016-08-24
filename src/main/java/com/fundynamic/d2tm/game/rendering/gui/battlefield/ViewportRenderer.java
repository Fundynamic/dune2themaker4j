package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * <p>
 *     A ViewportRenderer is a high level abstraction of drawing stuff on the screen from a viewport perspective. *
 * </p>
 * <p>
 *     Implementations of this interface will select things of type T on any source of data (Map, Structures list, etc).
 *     When they are a candidate to draw on the actual screen it should delegate this to a concrete implementation of
 *     {@link Renderer} of same type T.
 * </p>
 * <p>
 *     Example: Selecting which MapCells should be drawn is a different logic than selecting which Structures must be drawn
 * </p>
 * @param <T>
 */
public interface ViewportRenderer<T> {

    void render(Graphics graphics, Vector2D viewingVector, Renderer<T> renderer) throws SlickException;

}
