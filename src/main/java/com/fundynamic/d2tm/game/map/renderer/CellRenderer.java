package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.MapCell;
import org.newdawn.slick.Graphics;

interface CellRenderer {

    void draw(Graphics graphics, MapCell mapCell, int drawX, int drawY);

}
