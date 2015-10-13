package com.fundynamic.d2tm.game;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.TestableMouse;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityRepositoryTest;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.structures.StructureFactory;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.entities.units.UnitFacings;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractD2TMTest {

    public static int TILE_SIZE = 32;

    public static int MAP_WIDTH = 64;
    public static int MAP_HEIGHT = 64;

    @Mock
    protected Graphics graphics;

    @Mock
    protected GameContainer gameContainer;

    protected ImageRepository imageRepository;
    protected Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);
    protected Map map;

    protected EntityRepository entityRepository;

    @Before
    public void setUp() throws SlickException {
        map = makeMap(MAP_WIDTH, MAP_HEIGHT); // create a default map
        imageRepository = makeImageRepository();
        entityRepository = EntityRepositoryTest.makeTestableEntityRepository(map);

        Input input = mock(Input.class);
        when(gameContainer.getInput()).thenReturn(input);
    }

    // RESOURCE LOADING
    ////////////////////////////////////////////////////////////////////////////////
    private ImageRepository makeImageRepository() {
        return new ImageRepository() {
            @Override
            public Image load(String path) {
                return Mockito.mock(Image.class);
            }

            @Override
            public Image createImage(Vector2D dimensions) throws SlickException {
                return Mockito.mock(Image.class);
            }
        };
    }

    // MOUSE
    ////////////////////////////////////////////////////////////////////////////////
    /**
     * Creates mouse with TestableEntityRepository and TestableImageRepository
     *
     * @param player - used in TestableMouse
     * @return
     * @throws SlickException
     */
    public Mouse makeTestableMouse(Player player) throws SlickException {
        GameContainer gameContainer = mock(GameContainer.class);
        return new TestableMouse(player, gameContainer, entityRepository);
    }

    // MAP
    ////////////////////////////////////////////////////////////////////////////////
    public Map makeMap(int width, int height) throws SlickException {
        return new Map(new Shroud(null, Game.TILE_WIDTH, Game.TILE_HEIGHT), width, height);
    }

    public Cell makeCell(int x, int y) {
        Terrain terrain = mock(Terrain.class);
        return new Cell(map, terrain, x, y);
    }

    // STRUCTURES
    ////////////////////////////////////////////////////////////////////////////////
    public Structure makeStructure(Player player, int hitPoints) {
        return StructureFactory.makeStructure(player, hitPoints, entityRepository);
    }

    // UNIT
    ////////////////////////////////////////////////////////////////////////////////
    public Unit makeUnit(UnitFacings facing, Vector2D unitAbsoluteMapCoordinates) {
        return makeUnit(facing, unitAbsoluteMapCoordinates, Vector2D.zero(), 100);
    }

    public Unit makeUnit(UnitFacings facing, Vector2D unitAbsoluteMapCoordinates, Vector2D offset, int hitPoints) {
        Unit unit = makeUnit(player, hitPoints, unitAbsoluteMapCoordinates);
        unit.setFacing(facing.getValue());
        unit.setOffset(offset);
        return unit;
    }

    public Unit makeUnit(Player player, int hitPoints) {
        return makeUnit(player, hitPoints, Vector2D.zero());
    }

    public Unit makeUnit(Player player, int hitPoints, Vector2D absoluteMapCoordinates) {
        if (entityRepository == null) throw new IllegalStateException("You forgot to set up the entityRepository, probably you need to do super.setUp()");
        if (map == null) throw new IllegalStateException("You forgot to set up the map, probably you need to do super.setUp()");
        EntityData entityData = new EntityData(32, 32, 2);
        entityData.moveSpeed = 1.0f; // 1 pixel per frame
        entityData.hitPoints = hitPoints;
        Unit unit = new Unit(
                map,
                absoluteMapCoordinates,
                mock(SpriteSheet.class),
                player,
                entityData,
                entityRepository) { //mock(EntityRepository.class)
            @Override
            public boolean isDestroyed() {
                // we do this so that we do not have to deal with spawning explosions (which is done in the
                // update method of the 'original' unit.)
                return super.hitPointBasedDestructibility.hasDied();
            }
        };
        entityRepository.addEntityToList(unit);
        map.placeUnit(unit);
        return unit;
    }

    public EntityRepository getTestableEntityRepository() {
        return entityRepository;
    }
}
