package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Cell;


public class NormalMouse extends AbstractMouseBehavior {

    protected Cell hoverCell;

    public NormalMouse(Player controllingPlayer, Mouse mouse) {
        super(controllingPlayer, mouse);
    }

    public NormalMouse(Cell hoverCell, Mouse mouse) {
        this((Player) null, mouse);
        this.hoverCell = hoverCell;
    }

    @Override
    public void leftClicked() {
        Entity entity = hoveringOverSelectableEntity();
        if (entity == null) return;

        selectEntity(entity);

        if (selectedEntityBelongsToControllingPlayer() && selectedEntityIsMovable()) {
            mouse.setMouseBehavior(new MovableSelectedMouse(controllingPlayer, mouse));
        }
    }

    protected void selectEntity(Entity entity) {
        deselectCurrentlySelectedEntity();
        mouse.setLastSelectedEntity(entity);
        ((Selectable) entity).select();
    }

    protected Entity hoveringOverSelectableEntity() {
        if (hoverCell == null) return null;
        Entity entity = hoverCell.getEntity();
        if (entity == null) return null;
        if (!entity.isSelectable()) return null;
        return entity;
    }

    @Override
    public void rightClicked() {
        deselectCurrentlySelectedEntity();
        if (selectedEntityBelongsToControllingPlayer()) {
            mouse.setMouseBehavior(new NormalMouse(controllingPlayer, mouse));
        }
        mouse.setLastSelectedEntity(null);
    }

    protected void deselectCurrentlySelectedEntity() {
        Entity lastSelectedEntity = mouse.getLastSelectedEntity();
        if (lastSelectedEntity != null) {
            if (lastSelectedEntity.isSelectable()) {
                ((Selectable) lastSelectedEntity).deselect();
            }
        }
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        this.hoverCell = cell;
    }

    protected boolean selectedEntityBelongsToControllingPlayer() {
        Entity lastSelectedEntity = mouse.getLastSelectedEntity();
        if (lastSelectedEntity == null) return false;
        if (controllingPlayer == null) return false;
        return lastSelectedEntity.getPlayer().equals(controllingPlayer);
    }


    protected boolean selectedEntityIsMovable() {
        Entity lastSelectedEntity = mouse.getLastSelectedEntity();
        return lastSelectedEntity.isMovable();
    }

}
