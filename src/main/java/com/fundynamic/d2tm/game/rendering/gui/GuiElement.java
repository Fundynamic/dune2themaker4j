package com.fundynamic.d2tm.game.rendering.gui;


import com.fundynamic.d2tm.game.behaviors.Focusable;
import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.controls.MouseBehavior;
import com.fundynamic.d2tm.game.entities.Rectangle;

/**
 * A gui element has a drawing position (topleft) and size (width/height) - which basically is a rectangle.
 *
 */
public abstract class GuiElement extends Rectangle implements MouseBehavior, Updateable, Renderable, Focusable {

    protected boolean hasFocus;

    public GuiElement(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.hasFocus = false;
    }

    @Override
    public void getsFocus() {
        hasFocus = true;
    }

    @Override
    public void lostFocus() {
        hasFocus = false;
    }
}
