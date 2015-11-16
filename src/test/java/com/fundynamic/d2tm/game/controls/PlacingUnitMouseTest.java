package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class PlacingUnitMouseTest extends AbstractD2TMTest {

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        mouse.setMouseBehavior(new PlacingUnitMouse(mouse, entityRepository));
    }

    @Test
    public void leftClickedOnNoCellDoesNothing() throws SlickException {
        mouse.leftClicked();
    }

    @Test
    public void leftClickedOnNoHoverCellPlacesUnit() throws SlickException {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);
        mouse.mouseMovedToCell(cell);

        assertThat(entityRepository.allUnits(), is(empty()));

        mouse.leftClicked();

        EntitiesSet allUnits = entityRepository.allUnits();
        assertThat(allUnits.size(), is(1));

        Entity unit = allUnits.toList().get(0);

        assertThat(unit.getCoordinate(), is(Vector2D.create(32, 32)));
        assertThat(unit.getEntityType(), is(EntityType.UNIT));
    }

    @Test
    public void render() {
        mouse.render(graphics);
    }


}