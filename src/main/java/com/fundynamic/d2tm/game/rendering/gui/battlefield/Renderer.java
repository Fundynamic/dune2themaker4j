package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import org.newdawn.slick.Graphics;

/**
 * <p>
 * Very abstract way of rendering 'things' on the screen.
 * </p>
 * <p>
 * This is used by the {@link ViewportRenderer} to draw actual stuff on the screen.
 * </p>
 * <p>
 * A {@link ViewportRenderer} is responsible for culling/selecting stuff to draw
 * on the screen, but delegates *what* to draw to a concrete implementation of this Renderer. The types (T) must be
 * the same for both ViewportRenderer and this Renderer.
 * </p>
 * <p>
 * The drawX and drawY positions are the topLeft pixel absolute coordinates on the screen to render. This means that
 * the Renderer implementations only need to concern themselves with game logic / animation.
 * </p>
 *
 * @param <T> Type of thing to render
 */
public interface Renderer<T> {

    void draw(Graphics graphics, T thingToRender, int drawX, int drawY);

}
