package com.fundynamic.d2tm.game.rendering.gui;


import com.fundynamic.d2tm.game.behaviors.Focusable;
import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.controls.MouseBehavior;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;

/**
 * A gui element has a drawing position (topleft) and size (width/height) - which basically is a rectangle.
 *
 */
public abstract class GuiElement extends Rectangle implements MouseBehavior, Updateable, Renderable, Focusable {

    /**
     * A guiElement belongs to a GuiComposite, and it needs this reference to communicate to other
     * GuiElements via the GuiComposite.
     */
    protected GuiComposite guiComposite;

    protected boolean hasFocus;

    public GuiElement(int x, int y, int width, int height) {
        super(x, y, new Vector2D(width, height));
        this.hasFocus = false;
    }

    public void setGuiComposite(GuiComposite guiComposite) {
        this.guiComposite = guiComposite;
    }

    @Override
    public void getsFocus() {
        hasFocus = true;
    }

    @Override
    public void lostFocus() {
        hasFocus = false;
    }

    public Player getPlayer() {
        return guiComposite.getPlayer();
    }
}
