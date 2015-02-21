package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.map.Cell;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class MapCellMouseInteractionRenderer implements Renderer<Cell> {

    private final Mouse mouse;

    public MapCellMouseInteractionRenderer(Mouse mouse) {
        this.mouse = mouse;
    }

    @Override
    public void draw(Graphics graphics, Cell mapCell, int drawX, int drawY) {
        if (mapCell.isAtSameLocationAs(mouse.getHoverCell())) {
            graphics.setColor(Color.white);
            graphics.setLineWidth(1.1f);
            graphics.drawRect(drawX, drawY, 31, 31);
        }
    }
}
