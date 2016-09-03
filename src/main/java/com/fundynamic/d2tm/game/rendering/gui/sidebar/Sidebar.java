package com.fundynamic.d2tm.game.rendering.gui.sidebar;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.rendering.gui.DummyGuiElement;
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

    private GuiElement guiElement;

    public Sidebar(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(Graphics graphics) {
        Vector2D topLeft = getTopLeft();
//        if (hasFocus) {
//            graphics.setColor(Color.red);
//        } else {
//            graphics.setColor(Color.darkGray);
//        }
        graphics.setColor(Color.darkGray);
        graphics.fillRect(topLeft.getXAsInt(), topLeft.getYAsInt(), getWidthAsInt(), getHeightAsInt());
        graphics.setColor(Color.white);

        if (guiElement != null) {
            guiElement.render(graphics);
        }
    }

    @Override
    public void leftClicked() {
        if (guiElement != null) {
            guiElement.leftClicked();
        }
    }

    @Override
    public void rightClicked() {
        if (guiElement != null) {
            guiElement.rightClicked();
        }
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        // nothing to do here
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        if (guiElement != null) {
            if (guiElement.isVectorWithin(coordinates)) {
                guiElement.getsFocus();
            } else {
                guiElement.lostFocus();
            }
            guiElement.movedTo(coordinates);
        }
    }

    @Override
    public void leftButtonReleased() {
        if (guiElement != null) {
            guiElement.leftButtonReleased();
        }
    }

    @Override
    public void update(float deltaInSeconds) {
        if (guiElement != null) {
            guiElement.update(deltaInSeconds);
        }
    }

    @Override
    public void lostFocus() {
        super.lostFocus();
        if (guiElement != null) {
            guiElement.lostFocus();
        }
    }

    /**
     * playing around still, I suppose this triggers a certain kind of 'gui element' to be drawn
     * which needs an Entity reference to show progress of and also base its offerings?
     * @param entityBuilder
     */
    public void showEntityBuilderGuiFor(EntityBuilder entityBuilder) {
        int parentX = getTopLeft().getXAsInt();
        int parentY = getTopLeft().getYAsInt();

        this.guiElement =
                new SidebarSelectBuildableEntityGuiElement (
                        parentX + 10,
                        parentY + 10,
                        getWidthAsInt() - 20,
                        getHeightAsInt() - 20,
                        entityBuilder
                );
    }
}
