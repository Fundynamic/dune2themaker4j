package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.math.Vector2D;

/**
 * Any class implementing this interface should be able to listen to several mouse inputs and deal with it.
 */
public interface MouseBehavior {

    /**
     * Called when left mouse button has been clicked (hold and released)
     */
    void leftClicked();

    /**
     * Called when right mouse button has been clicked (hold and released)
     */
    void rightClicked();

    /**
     * Mouse has been dragged to the following coordinates
     * @param coordinates
     */
    void draggedToCoordinates(Vector2D coordinates);

    /**
     * Mouse has moved to the following coordinates
     * @param coordinates
     */
    void movedTo(Vector2D coordinates);

    /**
     * Called when left mouse button is released
     */
    void leftButtonReleased();

}
