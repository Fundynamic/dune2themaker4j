package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapTest;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class PlacingUnitMouseTest {

    private PlacingUnitMouse placingUnitMouse;
    private Map map;
    private EntityRepository entityRepository;

    @Before
    public void setUp() throws SlickException {
        map = MapTest.makeMap();
        Player player = new Player("Stefan", Recolorer.FactionColor.RED);
        Mouse mouse = MouseTest.makeTestableMouse(map, player);
        entityRepository = mouse.getEntityRepository();
        placingUnitMouse = new PlacingUnitMouse(mouse, entityRepository);
    }

    @Test
    public void leftClickedOnNoCellDoesNothing() throws SlickException {
        placingUnitMouse.leftClicked();
    }

    @Test
    public void leftClickedOnNoHoverCellPlacesUnit() throws SlickException {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);
        placingUnitMouse.mouseMovedToCell(cell);

        assertThat(entityRepository.allUnits(), is(empty()));

        placingUnitMouse.leftClicked();

        EntitiesSet allUnits = entityRepository.allUnits();
        assertThat(allUnits.size(), is(1));

        Entity unit = allUnits.asList().get(0);

        assertThat(unit.getAbsoluteMapCoordinates(), is(Vector2D.create(32, 32)));
        assertThat(unit.getEntityType(), is (EntityType.UNIT));
    }

}