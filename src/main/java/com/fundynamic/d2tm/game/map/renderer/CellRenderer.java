package com.fundynamic.d2tm.game.map.renderer;

import org.newdawn.slick.Graphics;

interface CellRenderer {

    void draw(Graphics graphics, int x, int y, int drawX, int drawY);

}
