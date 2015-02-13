package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.Structure;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

import static com.fundynamic.d2tm.game.map.CellFactory.makeCell;

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
    public void canSelectStructureOnCell() {
        MapCell hoverCell = makeCell();
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), 64, 64);
        hoverCell.setStructure(structure);
        Mouse mouse = new Mouse(hoverCell);
        mouse.selectStructure();
        Assert.assertEquals(structure, mouse.getSelectedStructure());
    }

}