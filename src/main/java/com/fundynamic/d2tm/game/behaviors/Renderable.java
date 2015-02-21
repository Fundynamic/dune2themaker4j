package com.fundynamic.d2tm.game.behaviors;

import org.newdawn.slick.Graphics;

/**
 * Interface that marks something that can be rendered on the screen.
 *
 * The x and y positions are absolute pixel positionss
 */
public interface Renderable {

    public void render(Graphics graphics, int x, int y);

}
