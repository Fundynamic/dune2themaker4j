package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.map.Cell;

public abstract class AbstractMouseBehavior implements MouseBehavior {
    protected final Mouse mouse;

    public AbstractMouseBehavior(Mouse mouse) {
        this.mouse = mouse;
    }

    public abstract void leftClicked();

    public abstract void rightClicked();

    public abstract void mouseMovedToCell(Cell cell);
}
