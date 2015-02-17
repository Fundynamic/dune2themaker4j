package com.fundynamic.d2tm.game.map.renderer;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * <p>
 *     A ViewportRenderer is a high level abstraction of drawing stuff on the screen from a viewport perspective. *
 * </p>
 * <p>
 *     Implementations of this interface will select things of type T on any source of data (Map, Structures list, etc).
 *     When they are a candidate to draw on the actual screen it should delegate this to a concrete implementation of
 *     {@link com.fundynamic.d2tm.game.map.renderer.Renderer} of same type T.
 * </p>
 * <p>
 *     Example: Selecting which MapCells should be drawn is a different logic than selecting which Structures must be drawn
 * </p>
 * @param <T>
 */
public interface ViewportRenderer<T> {

    public void render(Image imageToDrawOn, Vector2f viewingVector, Renderer<T> renderer) throws SlickException;

}
