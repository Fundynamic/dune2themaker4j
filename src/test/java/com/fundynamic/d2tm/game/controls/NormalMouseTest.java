package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class NormalMouseTest extends AbstractMouseBehaviorTest {

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        NormalMouse normalMouse = new NormalMouse(mouse);
        mouse.setMouseBehavior(normalMouse);
    }

    @Test
    public void leftClickSelectsEntityOnHoverCell() throws SlickException {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);
        mouse.setHoverCell(cell);

        Vector2D coordinatesAsAbsoluteVector2D = cell.getCoordinatesAsAbsoluteVector2D();
        Unit unit = makeUnit(player, 100, coordinatesAsAbsoluteVector2D);
        assertThat(unit.isSelected(), is(false));

        // ACT: click left
        mouse.leftClicked();

        // ASSERT: the unit we hover over should be selected
        assertTrue(unit.isSelected());
        assertThat(mouse.getMouseBehavior(), is(instanceOf(MovableSelectedMouse.class)));

        // ACT: right click
        mouse.rightClicked();

        // ASSERT: the unit should be deselected again
        assertFalse(unit.isSelected());
    }

    @Test
    public void render() {
        mouse.render(graphics);
    }

}