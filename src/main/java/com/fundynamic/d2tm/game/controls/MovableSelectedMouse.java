package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.map.Cell;

public class MovableSelectedMouse extends NormalMouse {

    public MovableSelectedMouse(Mouse mouse) {
        super(mouse);
        mouse.setMouseImage(Mouse.MouseImages.MOVE, 16, 16);
    }

    @Override
    public void leftClicked() {
        Entity entity = hoveringOverSelectableEntity();
        if (entity != null) {
            deselectCurrentlySelectedEntity();
            selectEntity(entity);
        } else {
            if (selectedEntityBelongsToControllingPlayer() && selectedEntityIsMovable()) {
                ((Moveable) mouse.getLastSelectedEntity()).moveTo(mouse.getHoverCell().getCoordinatesAsVector2D());
            }
        }
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        mouse.setHoverCell(cell);
        Entity entity = hoveringOverSelectableEntity();
        if (entity == null) {
            mouse.setMouseImage(Mouse.MouseImages.MOVE, 16, 16);
            return;
        }
        if (entity.belongsToPlayer(mouse.getControllingPlayer())) {
            mouse.setMouseImage(Mouse.MouseImages.HOVER_OVER_SELECTABLE_ENTITY, 16, 16);
        } else {
            mouse.setMouseImage(Mouse.MouseImages.ATTACK, 16, 16);
        }
    }

    @Override
    public void rightClicked() {
        super.rightClicked();
    }
}
