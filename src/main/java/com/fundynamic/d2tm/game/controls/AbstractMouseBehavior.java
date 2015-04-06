package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

public abstract class AbstractMouseBehavior implements MouseBehavior {
    protected final Mouse mouse;

    public AbstractMouseBehavior(Mouse mouse) {
        this.mouse = mouse;
    }

    public abstract void leftClicked();

    public abstract void rightClicked();

    public abstract void mouseMovedToCell(Cell cell);

    public void render(Graphics g) {
    }

    public void draggedToCoordinates(Vector2D coordinates) {
        // DO NOTHING
    }

    public void leftButtonReleased() {
        // DO NOTHING
    }

}
