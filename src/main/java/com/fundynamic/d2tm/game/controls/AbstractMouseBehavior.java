package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.math.Vector2D;

/**
 * An AbstractMouseBehavior is a MouseBehavior with a relation to Mouse
 */
public abstract class AbstractMouseBehavior<T> implements MouseBehavior {
    protected final Mouse mouse;

    public AbstractMouseBehavior(Mouse mouse) {
        this.mouse = mouse;
    }

    public abstract void leftClicked();

    public abstract void rightClicked();

    public abstract void setMouseBehavior(T mouseBehavior);

    public void draggedToCoordinates(Vector2D coordinates) {
        // DO NOTHING
    }

    public void leftButtonReleased() {
        // DO NOTHING
    }

}
