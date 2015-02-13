package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.map.MapCell;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class MouseCellInteractionRenderer implements CellRenderer {

    private final Mouse mouse;

    public MouseCellInteractionRenderer(Mouse mouse) {
        this.mouse = mouse;
    }

    @Override
    public void draw(Graphics graphics, MapCell mapCell, int drawX, int drawY) {
        if (mapCell.isAtSameLocationAs(mouse.getHoverCell())) {
            if (mapCell.hasStructure(mouse.getSelectedStructure())) {
                graphics.setColor(Color.red);
            } else {
                graphics.setColor(Color.white);
            }
            graphics.setLineWidth(1.1f);
            graphics.drawRect(drawX, drawY, 31, 31);
        }
    }
}
