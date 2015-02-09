package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.map.Cell;

/**
 * This class represents the state of the mouse. This is not about coordinates (we can get these via Slick listeners)
 * but about what structure is selected, the type of mouse being rendered (ie moving? attacking? placing structure? or
 * 'normal' ?)
 */
public class Mouse {

    private Cell hoverCell;

    public Mouse(Cell hoverCell) {
        this.hoverCell = hoverCell;
    }

    public void setHoverCell(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        this.hoverCell = cell;
    }

    public Cell getHoverCell() {
        return hoverCell;
    }
}
