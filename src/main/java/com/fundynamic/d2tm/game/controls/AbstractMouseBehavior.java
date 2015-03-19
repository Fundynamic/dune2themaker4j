package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.map.Cell;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class AbstractMouseBehavior implements MouseBehavior {
    protected final Mouse mouse;

    public AbstractMouseBehavior(Mouse mouse) {
        this.mouse = mouse;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        // TODO: make real mouse cursor
        graphics.setColor(Color.white);
        graphics.setLineWidth(1.1f);
        graphics.drawRect(x, y, 31, 31);
    }

    public abstract void leftClicked();

    public abstract void rightClicked();

    public abstract void mouseMovedToCell(Cell cell);
}
