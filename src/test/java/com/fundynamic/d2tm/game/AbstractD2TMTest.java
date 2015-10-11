package com.fundynamic.d2tm.game;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.TestableMouse;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityRepositoryTest;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.structures.StructureFactory;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapTest;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.mock;

public abstract class AbstractD2TMTest {

    protected Map map;
    protected EntityRepository entityRepository;

    @Before
    public void setUp() throws SlickException {
        map = MapTest.makeMap();
        entityRepository = EntityRepositoryTest.makeTestableEntityRepository(map);
    }

    public Structure makeStructure(Player player, int hitPoints) {
        return StructureFactory.makeStructure(player, hitPoints, entityRepository);
    }

    public Unit makeUnit(Player player, int hitPoints) {
        return makeUnit(player, hitPoints, Vector2D.zero());
    }

    public Unit makeUnit(Player player, int hitPoints, Vector2D absoluteMapCoordinates) {
        if (entityRepository == null) throw new IllegalStateException("You forgot to set up the entityRepository, probably you need to do super.setUp()");
        if (map == null) throw new IllegalStateException("You forgot to set up the map, probably you need to do super.setUp()");
        EntityData entityData = new EntityData(32, 32, 2);
        entityData.hitPoints = hitPoints;
        Unit unit = new Unit(mock(Map.class), absoluteMapCoordinates, mock(Image.class), player, entityData, mock(EntityRepository.class)) {
            @Override
            public boolean isDestroyed() {
                // we do this so that we do not have to deal with spawning explosions (which is done in the
                // update method)
                return super.hitPointBasedDestructibility.hasDied();
            }
        };
        entityRepository.addEntityToList(unit);
        map.placeUnit(unit);
        return unit;
    }

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

}
