package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class MovableSelectedMouseTest extends AbstractMouseBehaviorTest {

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        MovableSelectedMouse movableSelectedMouse = new MovableSelectedMouse(mouse, entityRepository);
        mouse.setMouseBehavior(movableSelectedMouse);
    }

    @Test
    public void leftClickedSelectsUnitOnHoverCell() throws SlickException {
        Unit unit = makeUnit(player, 100, Vector2D.create(32, 32));
        assertThat(unit.isSelected(), is(false));

        mouse.mouseMovedToCell(map.getCell(1, 1)); // equals 32, 32

        mouse.leftClicked();
        assertThat(unit.isSelected(), is(true));
    }

    @Test
    public void movesSelectedUnitsToCellThatIsNotOccupiedByOtherCell() throws SlickException {

        Unit unit = makeUnit(player, 100, Vector2D.create(32, 32));
        unit.select();

        // TODO: This is ugly because absolute coordinates are used here versus map coordinates above in test
        assertEquals(unit.getNextTargetToMoveTo(), Vector2D.create(32, 32));

        mouse.mouseMovedToCell(map.getCell(2, 2)); // equals 64, 64
        mouse.leftClicked();

        unit.update(1);

        assertEquals(unit.getNextTargetToMoveTo(), Vector2D.create(64, 64));
    }

    @Test
    public void attacksUnitOfOtherPlayer() {
        // create player for unit and select it
        Unit unit = makeUnit(player, 100, Vector2D.create(32, 32));
        unit.select();

        Player enemy = new Player("Enemy", Recolorer.FactionColor.RED);
        Unit enemyUnit = makeUnit(enemy, 100, Vector2D.create(64, 64));
        map.placeUnit(enemyUnit);

        Cell cell = map.getCellByAbsoluteMapCoordinates(Vector2D.create(64, 64));
        mouse.mouseMovedToCell(cell);
        mouse.leftClicked();
    }

}