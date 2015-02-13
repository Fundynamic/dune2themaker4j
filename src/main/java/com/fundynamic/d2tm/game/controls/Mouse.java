package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.Structure;

/**
 * This class represents the state of the mouse. This is not about coordinates (we can get these via Slick listeners)
 * but about what structure is selected, the type of mouse being rendered (ie moving? attacking? placing structure? or
 * 'normal' ?)
 */
public class Mouse {

    private MapCell hoverCell;
    private Structure selectedStructure;

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
        selectedStructure = hoverCell.getStructure();
    }

    public boolean hasAnyStructureSelected() {
        return this.selectedStructure != null;
    }

    public boolean hasThisStructureSelected(Structure structureToCheck) {
        if (structureToCheck == null) return false;
        if (this.selectedStructure == null) return false;
        return this.selectedStructure == structureToCheck;
    }

    public Structure getSelectedStructure() {
        return selectedStructure;
    }
}
