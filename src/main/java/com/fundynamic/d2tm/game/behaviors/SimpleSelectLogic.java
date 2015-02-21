package com.fundynamic.d2tm.game.behaviors;

/**
 *
 * A very straightforward select logic using a boolean flag to determine if something is selected or not.
 *
 */
public class SimpleSelectLogic implements Selectable {

    protected boolean selected;

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

}
