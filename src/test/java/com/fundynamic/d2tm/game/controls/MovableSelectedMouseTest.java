package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.controls.MouseTest.makeTestableMouse;
import static com.fundynamic.d2tm.game.entities.EntityRepositoryTest.createUnit;
import static com.fundynamic.d2tm.game.map.MapTest.makeMap;
import static com.fundynamic.d2tm.game.rendering.CellBasedEntityViewportRendererTest.makeUnit;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class MovableSelectedMouseTest {

    private Player player;
    private Map map;
    private Mouse mouse;

    @Before
    public void setUp() throws SlickException {
        player = new Player("Stefan", Recolorer.FactionColor.BLUE);
        map = makeMap();
        mouse = makeTestableMouse(map, player);
    }

    @Test
    public void leftClickedSelectsUnitOnHoverCell() throws SlickException {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);
        mouse.setHoverCell(cell);

        Unit unit = makeUnit(map, player, Vector2D.create(1, 1));
        assertFalse(unit.isSelected());
        cell.setEntity(unit);

        MovableSelectedMouse movableSelectedMouse = new MovableSelectedMouse(mouse, mouse.getEntityRepository());

        movableSelectedMouse.leftClicked();
        assertTrue(unit.isSelected());
    }

    @Test
    public void movesSelectedUnitsToCellThatIsNotOccupiedByOtherCell() throws SlickException {
        Cell cell = new Cell(map, mock(Terrain.class), 2, 2);
        mouse.setHoverCell(cell);

        Vector2D mapCoordinates = Vector2D.create(1, 1);
        Unit unit = createUnit(mouse.getEntityRepository(), mapCoordinates.scale(32F), player);
        unit.select();

        // TODO: This is ugly because absolute coordinates are used here versus map coordinates above in test
        assertEquals(unit.getNextTargetToMoveTo(), Vector2D.create(32, 32));

        MovableSelectedMouse movableSelectedMouse = new MovableSelectedMouse(mouse, mouse.getEntityRepository());

        movableSelectedMouse.leftClicked();

        unit.update(1);

        assertEquals(unit.getNextTargetToMoveTo(), Vector2D.create(64, 64));
    }

}