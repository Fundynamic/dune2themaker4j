package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.Structure;
import com.fundynamic.d2tm.game.terrain.Terrain;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

public class MouseTest {

    @Test
    public void returnsCellItHoversOver() {
        Cell hoverCell = makeCell();
        Mouse mouse = new Mouse(hoverCell);
        Assert.assertSame(hoverCell, mouse.getHoverCell());
    }

    @Test
    public void updatesHoverCell() {
        Mouse mouse = new Mouse(makeCell());
        Cell hoverCell = makeCell();
        mouse.setHoverCell(hoverCell, Vector2D.zero());
        Assert.assertSame(hoverCell, mouse.getHoverCell());
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentWhenHoverCellIsNull() {
        Mouse mouse = new Mouse(makeCell());
        mouse.setHoverCell(null, Vector2D.zero());
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentWhenVectorIsNull() {
        Mouse mouse = new Mouse(makeCell());
        mouse.setHoverCell(makeCell(), null);
    }

    @Test
    public void canSelectStructureOnCell() {
        Cell hoverCell = makeCell();
        Structure structure = new Structure(Mockito.mock(Image.class), 64, 64);
        hoverCell.setStructure(structure);
        Mouse mouse = new Mouse(hoverCell);
        mouse.selectStructure();
        Assert.assertEquals(structure, mouse.getSelectedStructure());
    }

    private Cell makeCell() {
        return new Cell(Mockito.mock(Terrain.class));
    }
}