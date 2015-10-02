package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityRepositoryTest;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapTest;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


public class DraggingSelectionBoxMouseTest {

    private DraggingSelectionBoxMouse draggingSelectionBoxMouse;
    private Mouse mouse;
    private Player player;

    @Before
    public void setUp() throws SlickException {
        Map map = MapTest.makeMap();

        player = new Player("Stefan", Recolorer.FactionColor.BLUE);
        mouse = MouseTest.makeTestableMouse(map, player);

        Viewport viewport = new Viewport(map, mouse, player);
        mouse.setViewport(viewport);

        // start dragging from 0,0
        draggingSelectionBoxMouse = new DraggingSelectionBoxMouse(mouse, Vector2D.zero());
    }

    @Test
    public void draggingOverNothingSelectsNoUnits() throws SlickException {
        Vector2D draggingCoordinates = Vector2D.create(10, 10);

        draggingSelectionBoxMouse.draggedToCoordinates(draggingCoordinates);

        draggingSelectionBoxMouse.leftButtonReleased();
    }

    @Test
    public void draggingOverUnitsSelectsUnits() {
        Vector2D draggingCoordinates = Vector2D.create(64, 64); // == 2, 2 on map

        EntityRepository entityRepository = mouse.getEntityRepository();

        Vector2D mapCoordinate = Vector2D.create(1, 1); // == 32, 32 pixels
        Selectable entity = (Selectable) entityRepository.placeOnMap(mapCoordinate, EntityType.UNIT, EntityRepositoryTest.UNIT_FIRST_ID, player);
        assertFalse(entity.isSelected());

        draggingSelectionBoxMouse.draggedToCoordinates(draggingCoordinates);

        draggingSelectionBoxMouse.leftButtonReleased();

        assertTrue(entity.isSelected());
    }

    @Test
    public void whenReleasingLeftButtonDeselectAllPreviouslySelectedUnitsFirst() {
        Vector2D draggingCoordinates = Vector2D.zero();

        EntityRepository entityRepository = mouse.getEntityRepository();

        Vector2D mapCoordinate = Vector2D.create(1, 1);
        Selectable entity = (Selectable) entityRepository.placeOnMap(mapCoordinate, EntityType.UNIT, EntityRepositoryTest.UNIT_FIRST_ID, player);
        entity.select();
        assertTrue(entity.isSelected());

        draggingSelectionBoxMouse.draggedToCoordinates(draggingCoordinates);

        // this will select nothing
        draggingSelectionBoxMouse.leftButtonReleased();

        assertFalse(entity.isSelected());
    }

}