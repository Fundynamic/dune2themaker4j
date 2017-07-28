package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

/**
 * Convenience object to test mouse that does nothing with graphics
 */
public class TestableMouse extends Mouse {

    public TestableMouse(Player controllingPlayer, GameContainer gameContainer, GuiComposite guiComposite) {
        super(controllingPlayer, gameContainer, guiComposite);
    }

    @Override
    public void setMouseImage(Image image, int hotSpotX, int hotSpotY) {
        // do nothing
    }

    @Override
    public void setMouseImageHotSpotCentered(Image image) {
        // do nothing
    }
}
