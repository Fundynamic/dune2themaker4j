package com.fundynamic.d2tm.game.rendering.gui.sidebar;


import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * <p>
 *     The sidebar is located at the right and offers interactions with entities. For example, structures
 *     can interact with the sidebar to offer things to build.
 * </p>
 * <p>
 *
 * </p>
 */
public class Sidebar extends GuiElement {

    public Sidebar(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(Graphics graphics) {
        Vector2D topLeft = getTopLeft();
        if (hasFocus) {
            graphics.setColor(Color.red);
        } else {
            graphics.setColor(Color.darkGray);
        }
        graphics.fillRect(topLeft.getXAsInt(), topLeft.getYAsInt(), getWidthAsInt(), getHeightAsInt());
        graphics.setColor(Color.white);
    }

    @Override
    public void leftClicked() {

    }

    @Override
    public void rightClicked() {

    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {

    }

    @Override
    public void movedTo(Vector2D coordinates) {

    }

    @Override
    public void leftButtonReleased() {

    }

    @Override
    public void update(float deltaInSeconds) {

    }
}
