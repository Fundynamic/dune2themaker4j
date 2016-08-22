package com.fundynamic.d2tm.game.behaviors;

/**
 * Something that can be selected and deselected.
 */
public interface Selectable extends Focusable {

    void select();

    void deselect();

    boolean isSelected();

}
