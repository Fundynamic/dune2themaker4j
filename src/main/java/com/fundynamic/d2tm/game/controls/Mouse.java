package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.Structure;

/**
 * This class represents the state of the mouse. This is not about coordinates (we can get these via Slick listeners)
 * but about what structure is selected, the type of mouse being rendered (ie moving? attacking? placing structure? or
 * 'normal' ?)
 */
public class Mouse {

    private Cell hoverCell;
    private Vector2D hoverCellMapVector;
    private Structure selectedStructure;

    public Mouse(Cell hoverCell) {
        this.hoverCell = hoverCell;
    }

    public void setHoverCell(Cell cell, Vector2D hoverCellMapVector) {
        if (cell == null) throw new IllegalArgumentException("argument cell may not be null");
        if (hoverCellMapVector == null) throw new IllegalArgumentException("argument hoverCellMapVector may not be null");
        this.hoverCell = cell;
        this.hoverCellMapVector = hoverCellMapVector;
    }

    public Cell getHoverCell() {
        return hoverCell;
    }

    public Vector2D getHoverCellMapVector() {
        return hoverCellMapVector;
    }

    /**
     * When a structure is bound to a cell, this method makes sure that is the only selected structure.
     * If there is no structure bound to the cell then this automatically deselects the structure.
     */
    public void selectStructure() {
        selectedStructure = hoverCell.getStructure(); // TODO: change "construction Yard" into "Structure"
        // TODO: tell structure it is being selected? (for perhaps other listeners GUI related!?)
        // ie: hoverCell.selectStructureOnCell();
        //       -- structure.select(); ...
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
