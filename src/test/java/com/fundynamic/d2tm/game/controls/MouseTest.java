package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.Structure;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

import static com.fundynamic.d2tm.game.map.CellFactory.makeCell;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MouseTest {

    @Test
    public void returnsCellItHoversOver() {
        MapCell hoverCell = makeCell();
        Mouse mouse = new Mouse(hoverCell);
        Assert.assertSame(hoverCell, mouse.getHoverCell());
    }

    @Test
    public void updatesHoverCell() {
        Mouse mouse = new Mouse(makeCell());
        MapCell hoverCell = makeCell();
        mouse.setHoverCell(hoverCell);
        Assert.assertSame(hoverCell, mouse.getHoverCell());
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentWhenHoverCellIsNull() {
        Mouse mouse = new Mouse(makeCell());
        mouse.setHoverCell(null);
    }

    @Test
    public void selectStructureOnCell() {
        MapCell hoverCell = makeCell();
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        hoverCell.setStructure(structure);
        Mouse mouse = new Mouse(hoverCell);
        mouse.selectStructure();
        assertEquals(structure, mouse.getLastSelectedStructure());
    }

    @Test
    public void lastSelectedStructureIsNullWhenNoStructureIsSelected() {
        assertNull(new Mouse(makeCell()).getLastSelectedStructure());
    }

    @Test
    public void selectStructureOnCellWithoutStructureWillNotInfluenceLastSelectedStructure() {
        MapCell hoverCell = makeCell();
        Mouse mouse = new Mouse(hoverCell);

        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        hoverCell.setStructure(structure);
        mouse.selectStructure();

        MapCell hoverCellWithoutStructure = makeCell();
        mouse.setHoverCell(hoverCellWithoutStructure);
        mouse.selectStructure(); // this should ignore the fact that the new hover cell has no structure

        assertEquals(structure, mouse.getLastSelectedStructure());
    }

    @Test
    public void deselectStructure() {
        MapCell hoverCell = makeCell();
        Mouse mouse = new Mouse(hoverCell);

        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        hoverCell.setStructure(structure);
        mouse.selectStructure();
        mouse.deselectStructure();

        assertNull(mouse.getLastSelectedStructure());
        assertFalse(structure.isSelected());
    }

}