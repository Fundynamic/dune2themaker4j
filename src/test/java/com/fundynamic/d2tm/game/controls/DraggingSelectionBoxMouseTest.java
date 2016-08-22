package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.EntitiesData;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


public class DraggingSelectionBoxMouseTest extends AbstractD2TMTest {

    private DraggingSelectionBoxMouse draggingSelectionBoxMouse;

    @Before
    public void setUp() throws SlickException {
        super.setUp();

        // start dragging from 0,0
        Vector2D startingCoordinates = Vector2D.zero();
        draggingSelectionBoxMouse = new DraggingSelectionBoxMouse(battleField, entityRepository, startingCoordinates);
    }

    @Test
    public void draggingOverNothingSelectsNoUnits() throws SlickException {
        draggingSelectionBoxMouse.draggedToCoordinates(Vector2D.create(10, 10));

        draggingSelectionBoxMouse.leftButtonReleased();
    }

    @Test
    public void draggingOverUnitsSelectsUnits() {
        Coordinate coordinate = Coordinate.create(32, 32);
        Selectable entity = (Selectable) entityRepository.placeOnMap(coordinate, EntityType.UNIT, EntitiesData.QUAD, player);
        assertFalse(entity.isSelected());

        draggingSelectionBoxMouse.draggedToCoordinates(Vector2D.create(64, 64)); // == 2, 2 on map

        draggingSelectionBoxMouse.leftButtonReleased();

        assertTrue(entity.isSelected());
    }

    @Test
    public void whenReleasingLeftButtonDeselectAllPreviouslySelectedUnitsFirst() {
        Coordinate coordinate = Coordinate.create(1, 1);
        Selectable entity = (Selectable) entityRepository.placeOnMap(coordinate, EntityType.UNIT, EntitiesData.QUAD, player);
        entity.select();
        assertTrue(entity.isSelected());

        draggingSelectionBoxMouse.draggedToCoordinates(Vector2D.create(0, 0));

        // this will select nothing
        draggingSelectionBoxMouse.leftButtonReleased();

        assertFalse(entity.isSelected());
    }

    @Test
    public void render() {
        draggingSelectionBoxMouse.render(graphics);
    }

}