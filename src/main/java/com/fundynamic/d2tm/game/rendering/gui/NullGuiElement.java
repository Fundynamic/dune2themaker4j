package com.fundynamic.d2tm.game.rendering.gui;

import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

/**
 * Null Object pattern for GuiElement
 */
public class NullGuiElement extends GuiElement {

    private static NullGuiElement instance;

    public static NullGuiElement getInstance() {
        if (instance == null) {
            instance = new NullGuiElement();
        }
        return instance;
    }

    public NullGuiElement() {
        super(-2, -2, 1, 1); // make if impossible for the mouse to ever get within this rectangle
    }

    @Override
    public void leftClicked() {
        // do nothing because null element
    }

    @Override
    public void rightClicked() {
        // do nothing because null element
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        // do nothing because null element
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        // do nothing because null element
    }

    @Override
    public void leftButtonReleased() {
        // do nothing because null element
    }

    @Override
    public void update(float deltaInSeconds) {
        // do nothing because null element
    }

    @Override
    public void render(Graphics graphics) {
        // do nothing because null element
    }

    @Override
    public String toString() {
        return "NullGuiElement";
    }
}
