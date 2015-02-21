package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.math.Vector2D;

/**
 * This class represents the state of the mouse. This is not about coordinates (we can get these via Slick listeners)
 * but about what structure is selected, the type of mouse being rendered (ie moving? attacking? placing structure? or
 * 'normal' ?)
 */
public class Mouse {

    private MapCell hoverCell;
    private Entity lastSelectedEntity;

    public Mouse(){}

    public Mouse(MapCell hoverCell) {
        this.hoverCell = hoverCell;
    }

    public void setHoverCell(MapCell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        this.hoverCell = cell;
    }

    public MapCell getHoverCell() {
        return hoverCell;
    }

    public Vector2D getHoverCellMapVector() {
        return hoverCell.getCoordinatesAsVector2D();
    }

    /**
     * When a structure is bound to a cell, this method makes sure that is the only selected structure.
     * If there is no structure bound to the cell then this automatically deselects the structure.
     */
    public void selectStructure() {
        if (hoverCell == null) return;
        Entity entity = hoverCell.getEntity();
        if (entity == null) return;
        if (!entity.isSelectable()) return;
        lastSelectedEntity = entity;
        ((Selectable)lastSelectedEntity).select();
    }

    public boolean hasAnyEntitySelected() {
        return this.lastSelectedEntity != null;
    }

    public Entity getLastSelectedEntity() {
        return lastSelectedEntity;
    }

    public void deselectStructure() {
        if (lastSelectedEntity != null) {
            if (lastSelectedEntity.isSelectable()) {
                ((Selectable) lastSelectedEntity).deselect();
            }
        }
        lastSelectedEntity = null;
    }

    public boolean hoversOverSelectableEntity() {
        Entity entity = hoverCell.getEntity();
        if (entity == null) return false;
        return entity.isSelectable();

    }
}
