package com.fundynamic.d2tm.game.controls;

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


public class PlacingProjectileMouseTest extends AbstractMouseBehaviorTest {

    private PlacingProjectileMouse placingProjectileMouse;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        placingProjectileMouse = new PlacingProjectileMouse(mouse, entityRepository);
    }

    @Test
    public void leftClickedOnNoCellDoesNothing() throws SlickException {
        placingProjectileMouse.leftClicked();
    }

    @Test
    public void leftClickedOnNoHoverCellPlacesProjectile() throws SlickException {
        Cell cell = new Cell(map, mock(Terrain.class), 1, 1);
        placingProjectileMouse.mouseMovedToCell(cell);

        assertThat(entityRepository.allProjectiles(), is(empty()));

        placingProjectileMouse.leftClicked();

        assertThat(entityRepository.allProjectiles().size(), is(1));
        Entity projectile = entityRepository.allProjectiles().toList().get(0);

        assertThat(projectile.getAbsoluteCoordinates(), is(Vector2D.create(32, 32)));
        assertThat(projectile.getEntityType(), is (EntityType.PROJECTILE));
    }
}