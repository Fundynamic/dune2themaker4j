package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Terrain;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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
        mouse.setHoverCell(hoverCell);
        Assert.assertSame(hoverCell, mouse.getHoverCell());
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentWhenHoverCellIsNull() {
        Mouse mouse = new Mouse(makeCell());
        mouse.setHoverCell(null);
    }

    private Cell makeCell() {
        return new Cell(Mockito.mock(Terrain.class));
    }
}