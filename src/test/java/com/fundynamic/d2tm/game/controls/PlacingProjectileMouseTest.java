package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class PlacingProjectileMouseTest {

    private PlacingProjectileMouse placingProjectileMouse;
    private Map map;
    private EntityRepository entityRepository;

    @Before
    public void setUp() throws SlickException {
        map = MapTest.makeMap();
        Player player = new Player("Stefan", Recolorer.FactionColor.RED);
        Mouse mouse = MouseTest.makeTestableMouse(map, player);
        entityRepository = mouse.getEntityRepository();
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
        Entity projectile = new ArrayList<>(entityRepository.allProjectiles()).get(0);

        assertThat(projectile.getAbsoluteMapCoordinates(), is(Vector2D.create(32, 32)));
    }

}