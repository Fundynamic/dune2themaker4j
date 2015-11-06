package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.EntitiesData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;


public class DraggingSelectionBoxMouseTest extends AbstractMouseBehaviorTest {

    @Before
    public void setUp() throws SlickException {
        super.setUp();

        Viewport viewport = new Viewport(map, mouse, player, mock(Image.class));
        mouse.setViewport(viewport);

        // start dragging from 0,0
        DraggingSelectionBoxMouse draggingSelectionBoxMouse = new DraggingSelectionBoxMouse(mouse, Vector2D.zero());
        mouse.setMouseBehavior(draggingSelectionBoxMouse);
    }

    @Test
    public void draggingOverNothingSelectsNoUnits() throws SlickException {
        mouse.draggedToCoordinates(10, 10);

        mouse.leftButtonReleased();
    }

    @Test
    public void draggingOverUnitsSelectsUnits() {
        EntityRepository entityRepository = mouse.getEntityRepository();

        Coordinate coordinate = Coordinate.create(32, 32);
        Selectable entity = (Selectable) entityRepository.placeOnMap(coordinate, EntityType.UNIT, EntitiesData.QUAD, player);
        assertFalse(entity.isSelected());

        mouse.draggedToCoordinates(64, 64); // == 2, 2 on map

        mouse.leftButtonReleased();

        assertTrue(entity.isSelected());
    }

    @Test
    public void whenReleasingLeftButtonDeselectAllPreviouslySelectedUnitsFirst() {
        EntityRepository entityRepository = mouse.getEntityRepository();

        Coordinate coordinate = Coordinate.create(1, 1);
        Selectable entity = (Selectable) entityRepository.placeOnMap(coordinate, EntityType.UNIT, EntitiesData.QUAD, player);
        entity.select();
        assertTrue(entity.isSelected());

        mouse.draggedToCoordinates(0, 0);

        // this will select nothing
        mouse.leftButtonReleased();

        assertFalse(entity.isSelected());
    }

    @Test
    public void render() {
        mouse.render(graphics);
    }

}