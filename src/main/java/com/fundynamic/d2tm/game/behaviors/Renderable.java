package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.rendering.RenderQueue;
import org.newdawn.slick.Graphics;

/**
 * Interface that marks something that can be rendered on the screen.
 *
 * The x and y positions are absolute pixel positions
 */
public interface Renderable {

    void render(Graphics graphics, int x, int y);

    void enrichRenderQueue(RenderQueue renderQueue);
}
