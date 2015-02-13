package com.fundynamic.d2tm.game.map.renderer;

import org.newdawn.slick.Graphics;

interface Renderer<T> {

    void draw(Graphics graphics, T thingToRender, int drawX, int drawY);

}
