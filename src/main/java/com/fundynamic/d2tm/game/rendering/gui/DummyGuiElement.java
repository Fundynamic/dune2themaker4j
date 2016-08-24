package com.fundynamic.d2tm.game.rendering.gui;


import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class DummyGuiElement extends GuiElement {

    public DummyGuiElement(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(Graphics graphics) {
        Vector2D topLeft = getTopLeft();
        if (hasFocus) {
            graphics.setColor(Color.red);
        } else {
            graphics.setColor(Color.gray);
        }
        graphics.fillRect(topLeft.getXAsInt(), topLeft.getYAsInt(), getWidthAsInt(), getHeightAsInt());
        graphics.setColor(Color.white);
        graphics.drawString("DUMMY", topLeft.getXAsInt(), topLeft.getYAsInt());
    }

    @Override
    public void leftClicked() {
        // do nothing because Dummy element
    }

    @Override
    public void rightClicked() {
        // do nothing because Dummy element
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        // do nothing because Dummy element
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        // do nothing because Dummy element
    }

    @Override
    public void leftButtonReleased() {
        // do nothing because Dummy element
    }

    @Override
    public void update(float deltaInSeconds) {
        // do nothing because Dummy element
    }
}
