package com.fundynamic.d2tm.game.controls.battlefield;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.controls.battlefield.MovableSelectedMouse;
import com.fundynamic.d2tm.game.controls.battlefield.NormalMouse;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class NormalMouseTest extends AbstractD2TMTest {

    private NormalMouse normalMouse;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        normalMouse = new NormalMouse(battleField);
        battleField.setMouseBehavior(normalMouse);
    }

    @Test
    public void leftClickSelectsEntityOnHoverCell() throws SlickException {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);
        normalMouse.setHoverCell(cell);

        Coordinate coordinate = cell.getCoordinates();
        Unit unit = makeUnit(player, coordinate, "QUAD");
        assertThat(unit.isSelected(), is(false));

        // ACT: click left
        battleField.leftClicked();

        // ASSERT: the unit we hover over should be selected
        assertTrue(unit.isSelected());
        assertThat(battleField.getMouseBehavior(), is(instanceOf(MovableSelectedMouse.class)));

        // ACT: right click
        battleField.rightClicked();

        // ASSERT: the unit should be deselected again
        assertFalse(unit.isSelected());
    }

    @Test
    public void mouseMovedToCellWithUnitGivesFocusToUnit() {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);

        Coordinate coordinate = cell.getCoordinates();
        Unit unit = makeUnit(player, coordinate, "QUAD");
        assertThat(unit.hasFocus(), is(false));

        // ACT: move to
        battleField.mouseMovedToCell(cell);

        // ASSERT: the unit we hover over has focus
        assertTrue(unit.hasFocus());

        Cell otherCell = new Cell(map, mock(Terrain.class), 1, 2);

        // ACT: move to other cell without unit
        battleField.mouseMovedToCell(otherCell);

        // ASSERT: the unit should have no focus anymore
        assertFalse(unit.hasFocus());
    }

    @Test
    public void mouseMovedToCellWithUnitGivesFocusToVisibleStructure() {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);

        Coordinate coordinate = cell.getCoordinates();
        Structure structure = makeStructure(player, 1000, coordinate);
        assertThat(structure.hasFocus(), is(false));

        // ACT: move to
        battleField.mouseMovedToCell(cell);

        // ASSERT: the structure we hover over has focus
        assertTrue(structure.hasFocus());

        Cell otherCell = new Cell(map, mock(Terrain.class), 10, 10); // make sure we are way out of range due structure width/height

        // ACT: move to other cell without structure
        battleField.mouseMovedToCell(otherCell);

        // ASSERT: the structure should have no focus anymore
        assertFalse(structure.hasFocus());
    }

    @Test
    public void render() {
        normalMouse.render(graphics);
    }

}