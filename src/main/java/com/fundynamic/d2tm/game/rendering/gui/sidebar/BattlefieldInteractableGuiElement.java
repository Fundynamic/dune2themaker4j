package com.fundynamic.d2tm.game.rendering.gui.sidebar;

import com.fundynamic.d2tm.game.rendering.gui.BattleFieldInteractable;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;


/**
 * A gui element that gets events from the BattleField and acts accordingly.
 */
public abstract class BattlefieldInteractableGuiElement extends GuiElement implements BattleFieldInteractable {

    public BattlefieldInteractableGuiElement(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

}
