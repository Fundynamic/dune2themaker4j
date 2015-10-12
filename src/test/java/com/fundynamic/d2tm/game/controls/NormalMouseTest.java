package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.entities.EntityRepositoryTest.createUnit;
import static com.fundynamic.d2tm.game.entities.EntityRepositoryTest.makeTestableEntityRepository;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class NormalMouseTest extends AbstractMouseBehaviorTest {

    @Test
    public void leftClickSelectsEntityOnHoverCell() throws SlickException {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);
        mouse.setHoverCell(cell);

        EntityRepository entityRepository = makeTestableEntityRepository(map);
        Unit unit = createUnit(entityRepository, Vector2D.create(1, 1), player);
        assertFalse(unit.isSelected());

        NormalMouse normalMouse = new NormalMouse(mouse);

        // ACT: click left
        normalMouse.leftClicked();

        // ASSERT: the unit we hover over should be selected
        assertTrue(unit.isSelected());
        assertThat(mouse.getMouseBehavior(), is(instanceOf(MovableSelectedMouse.class)));

        // ACT: right click
        normalMouse.rightClicked();

        // ASSERT: the unit should be deselected again
        assertFalse(unit.isSelected());
    }

}