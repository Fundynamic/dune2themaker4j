package com.fundynamic.d2tm.game.behaviors;

import org.newdawn.slick.Graphics;

/**
 * Interface that marks something that can be rendered on the screen, the implementation has all knowledge
 * of where to draw things.
 */
public interface Renderable {

    /**
     * Calls render logic.
     *
     * @param graphics
     */
    void render(Graphics graphics);
}
