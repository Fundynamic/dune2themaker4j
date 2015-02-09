package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class MouseCellInteractionRenderer implements CellRenderer {

    private final Mouse mouse;
    private final Map map;

    public MouseCellInteractionRenderer(Mouse mouse, Map map) {
        this.mouse = mouse;
        this.map = map;
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int drawX, int drawY) {
        Cell cell = map.getCell(x, y);
        if (cell.equals(mouse.getHoverCell())) {
            if (cell.hasStructure(mouse.getSelectedStructure())) {
                graphics.setColor(Color.red);
            } else {
                graphics.setColor(Color.white);
            }
            graphics.setLineWidth(1.1f);
            graphics.drawRect(drawX, drawY, 31, 31);
        }
    }
}
