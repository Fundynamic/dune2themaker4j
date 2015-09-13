package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.controls.MouseTest.makeMouse;
import static com.fundynamic.d2tm.game.entities.EntityRepositoryTest.createUnit;
import static com.fundynamic.d2tm.game.entities.EntityRepositoryTest.makeTestableEntityRepository;
import static com.fundynamic.d2tm.game.map.MapTest.makeMap;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class NormalMouseTest {

    @Test
    public void leftClickSelectsEntityOnHoverCell() throws SlickException {
        Map map = makeMap();
        Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);
        Mouse mouse = makeMouse(map, player);

        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);
        mouse.setHoverCell(cell);

        EntityRepository entityRepository = makeTestableEntityRepository(map);
        Unit unit = createUnit(entityRepository, Vector2D.create(1, 1), player);
        cell.setEntity(unit);
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