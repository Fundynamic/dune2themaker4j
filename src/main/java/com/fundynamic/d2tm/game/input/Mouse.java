package com.fundynamic.d2tm.game.input;


import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Input;

public class Mouse {

    private Input input;

    public Mouse(Input input) {
        if (input == null)
            throw new IllegalArgumentException("Input may not be null");
        this.input = input;
    }

    public boolean isLeftMouseButtonPressed() {
        return input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
    }

    public boolean isRightMouseButtonPressed() {
        return input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
    }

    public int getXCoordinate() {
        return input.getMouseX();
    }

    public int getYCoordinate() {
        return input.getMouseY();
    }

    public String toString() {
        String retValue = "Mouse ( " + "isLeftMouseButtonPressed = ["
                + isLeftMouseButtonPressed() + "] "
                + "isRightMouseButtonPressed = [" + isRightMouseButtonPressed()
                + "] " + "isLeftMouseButtonPressed = [" + getXCoordinate()
                + "] " + "isLeftMouseButtonPressed = [" + getYCoordinate()
                + "] " + " )";

        return retValue;
    }

    public Vector2D getVector2D() {
        return new Vector2D(getXCoordinate(), getYCoordinate());
    }
}