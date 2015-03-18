package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class MovableSelectedMouse extends NormalMouse {

    public MovableSelectedMouse(Player controllingPlayer, Mouse mouse) {
        super(controllingPlayer, mouse);
    }

    @Override
    public void leftClicked() {
        Entity entity = hoveringOverSelectableEntity();
        if (entity != null) {
            deselectCurrentlySelectedEntity();
            selectEntity(entity);
        } else {
            if (selectedEntityBelongsToControllingPlayer() && selectedEntityIsMovable()) {
                ((Moveable) mouse.getLastSelectedEntity()).moveTo(hoverCell.getCoordinatesAsVector2D());
            }
        }
    }

    @Override
    public void rightClicked() {
        super.rightClicked();
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        graphics.setColor(Color.green);
        graphics.setLineWidth(1.1f);
        graphics.drawRect(x, y, 31, 31);
    }
}
