package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.controls.battlefield.MovableSelectedMouse;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class MovableSelectedMouseTest extends AbstractD2TMTest {

    private MovableSelectedMouse movableSelectedMouse;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        movableSelectedMouse = new MovableSelectedMouse(battleField);
    }

    @Test
    public void leftClickedSelectsUnitOnHoverCell() throws SlickException {
        Unit unit = makeUnit(player, Coordinate.create(32, 32), EntitiesData.QUAD);
        assertThat(unit.isSelected(), is(false));

        movableSelectedMouse.mouseMovedToCell(map.getCell(1, 1)); // equals 32, 32

        movableSelectedMouse.leftClicked();
        assertThat(unit.isSelected(), is(true));
    }

    @Test
    public void movesSelectedUnitsToCellThatIsNotOccupiedByOtherCell() throws SlickException {
        Unit unit = makeUnit(player, Coordinate.create(32, 32), EntitiesData.QUAD);
        unit.select();

        // TODO: This is ugly because absolute coordinates are used here versus map coordinates above in test
        assertEquals(unit.getNextTargetToMoveTo(), Vector2D.create(32, 32));

        movableSelectedMouse.mouseMovedToCell(map.getCell(2, 2)); // equals 64, 64
        movableSelectedMouse.leftClicked();

        unit.update(1);

        assertEquals(unit.getNextTargetToMoveTo(), Vector2D.create(64, 64));
    }

    @Test
    public void attacksUnitOfOtherPlayer() {
        // create player for unit and select it
        Unit unit = makeUnit(player, Coordinate.create(32, 32), EntitiesData.QUAD);
        unit.select();

        Player enemy = new Player("Enemy", Recolorer.FactionColor.RED);
        Unit enemyUnit = makeUnit(enemy, Coordinate.create(64, 64), EntitiesData.QUAD);
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