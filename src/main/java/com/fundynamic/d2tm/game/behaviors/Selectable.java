package com.fundynamic.d2tm.game.behaviors;

public interface Selectable {

    void select();

    void deselect();

    boolean isSelected();

    void getsFocus();

    void lostFocus();
}
