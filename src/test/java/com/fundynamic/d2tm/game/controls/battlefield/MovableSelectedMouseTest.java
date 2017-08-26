package com.fundynamic.d2tm.game.controls.battlefield;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.entities.units.states.MoveToCellState;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class MovableSelectedMouseTest extends AbstractD2TMTest {

    private MovableSelectedMouse movableSelectedMouse;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        movableSelectedMouse = new MovableSelectedMouse(battleField);
    }

    @Test
    public void leftClickedSelectsUnitOnHoverCell() throws SlickException {
        Unit unit = makeUnit(player, MapCoordinate.create(1, 1), EntitiesData.QUAD);
        assertThat(unit.isSelected(), is(false));

        movableSelectedMouse.mouseMovedToCell(map.getCell(1, 1)); // equals 32, 32

        movableSelectedMouse.leftClicked();
        assertThat(unit.isSelected(), is(true));
    }

    @Test
    public void movesSelectedUnitsToCellThatIsNotOccupiedByOtherCell() throws SlickException {
        Unit unit = makeUnit(player, MapCoordinate.create(1, 1), EntitiesData.QUAD);
        unit.select();

        // next target should equal current map coordinate
        assertEquals(MapCoordinate.create(1, 1), unit.getNextTargetToMoveTo().toMapCoordinate());

        movableSelectedMouse.mouseMovedToCell(map.getCell(2, 2));
        movableSelectedMouse.leftClicked();

        unit.update(1);

        assertTrue(unit.getState() instanceof MoveToCellState);
        assertEquals(MapCoordinate.create(2, 2), unit.getNextTargetToMoveTo().toMapCoordinate());
    }

    @Test
    public void attacksUnitOfOtherPlayer() {
        // create player for unit and select it
        Unit unit = makeUnit(player, MapCoordinate.create(1, 1), EntitiesData.QUAD);
        unit.select();

        Player enemy = new Player("Enemy", Recolorer.FactionColor.RED);
        Unit enemyUnit = makeUnit(enemy, MapCoordinate.create(2, 2), EntitiesData.QUAD);
        map.revealShroudFor(enemyUnit);

        Cell cell = map.getCellByAbsoluteMapCoordinates(Coordinate.create(64, 64));
        movableSelectedMouse.mouseMovedToCell(cell);
        mouse.leftClicked();
    }

    @Test
    public void render() {
        movableSelectedMouse.render(graphics);
    }

}