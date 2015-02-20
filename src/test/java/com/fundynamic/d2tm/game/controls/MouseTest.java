package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

import static com.fundynamic.d2tm.game.map.CellFactory.makeCell;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MouseTest {

    private MapCell defaultHoverCell;
    private Mouse mouse;

    @Before
    public void setUp() {
        defaultHoverCell = makeCell();
        mouse = new Mouse(defaultHoverCell);
    }

    @Test
    public void returnsCellItHoversOver() {
        Assert.assertSame(defaultHoverCell, mouse.getHoverCell());
    }

    @Test
    public void updatesHoverCell() {
        MapCell newHoverCell = makeCell();
        mouse.setHoverCell(newHoverCell);
        Assert.assertSame(newHoverCell, mouse.getHoverCell());
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentWhenHoverCellIsNull() {
        mouse.setHoverCell(null);
    }

    @Test
    public void selectStructureOnCell() {
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        defaultHoverCell.setMapEntity(structure);
        mouse.selectStructure();
        assertEquals(structure, mouse.getLastSelectedEntity());
    }

    @Test
    public void lastSelectedStructureIsNullWhenNoStructureIsSelected() {
        assertNull(mouse.getLastSelectedEntity());
    }

    @Test
    public void selectStructureOnCellWithoutStructureWillNotInfluenceLastSelectedStructure() {
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        defaultHoverCell.setMapEntity(structure);
        mouse.selectStructure();

        MapCell hoverCellWithoutStructure = makeCell();
        mouse.setHoverCell(hoverCellWithoutStructure);
        mouse.selectStructure(); // this should ignore the fact that the new hover cell has no structure

        assertEquals(structure, mouse.getLastSelectedEntity());
    }

    @Test
    public void deselectStructureDeselectsPreviouslySelectedStructure() {
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        defaultHoverCell.setMapEntity(structure);
        mouse.selectStructure();
        mouse.deselectStructure();

        assertNull(mouse.getLastSelectedEntity());
        assertFalse(structure.isSelected());
    }

    @Test
    public void deselectStructureIgnoresNullHoverCell() {
        mouse = new Mouse(null);
        mouse.deselectStructure();
        assertNull(mouse.getLastSelectedEntity());
    }

    @Test
    public void deselectStructureIgnoresWhenNoStructureWasSelectedInTheFirstPlace() {
        mouse.deselectStructure();
        assertNull(mouse.getLastSelectedEntity());
    }
}