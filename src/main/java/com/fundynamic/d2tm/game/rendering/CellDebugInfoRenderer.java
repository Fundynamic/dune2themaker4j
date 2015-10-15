package com.fundynamic.d2tm.game.rendering;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.MouseBehavior;
import com.fundynamic.d2tm.game.controls.PlacingStructureMouse;
import com.fundynamic.d2tm.game.map.Cell;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class CellDebugInfoRenderer implements Renderer<Cell> {

    private final Mouse mouse;

    public CellDebugInfoRenderer(Mouse mouse) {
        this.mouse = mouse;
    }

    @Override
    public void draw(Graphics graphics, Cell mapCell, int drawX, int drawY) {
        if (mouse.getHoverCell() == null) return;

        if (mapCell.isAtSameLocationAs(mouse.getHoverCell())) {

            MouseBehavior mouseBehavior = mouse.getMouseBehavior();
            if (mouseBehavior instanceof PlacingStructureMouse) {
                ((PlacingStructureMouse) mouseBehavior).render(graphics, drawX, drawY);
            } else {
                graphics.setColor(Color.white);
                graphics.setLineWidth(1.1f);
                graphics.drawRect(drawX, drawY, 31, 31);
            }
        }
    }

}
