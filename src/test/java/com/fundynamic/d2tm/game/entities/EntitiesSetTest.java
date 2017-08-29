package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.predicates.BelongsToPlayer;
import com.fundynamic.d2tm.game.entities.predicates.NotPredicate;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Rectangle;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class EntitiesSetTest extends AbstractD2TMTest {

    private EntitiesSet entitiesSet;

    private int playerOneUnitCount;
    private int playerOneStructureCount;
    private int playerOneBareEntitiesCount;
    private int destroyers;
    private int moveableUnitsOfPlayerOne;
    private MapCoordinate topLeftFirstQuad;
    private Unit quad;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        EntityData entityData = new EntityData(EntityType.UNIT, 32, 32, 10);

        entitiesSet = new EntitiesSet();

        player = new Player("Player one", Faction.GREEN);

        // player one has 4 units and 2 structures
        topLeftFirstQuad = MapCoordinate.create(10, 10);
        quad = makeUnit(player, topLeftFirstQuad, "QUAD");
        entitiesSet.add(quad);
        entitiesSet.add(makeUnit(player, topLeftFirstQuad.add(MapCoordinate.create(2, 0)), "QUAD"));
        entitiesSet.add(makeUnit(player, topLeftFirstQuad.add(MapCoordinate.create(0, 2)), "QUAD"));
        entitiesSet.add(makeUnit(player, topLeftFirstQuad.add(MapCoordinate.create(20, 20)), "QUAD"));

        playerOneUnitCount = 4;
        moveableUnitsOfPlayerOne = 4;
        destroyers = 4;

        entitiesSet.add(makeStructure(player, 200));
        entitiesSet.add(makeStructure(player, 200));
        playerOneStructureCount = 2;

        Player playerTwo = new Player("Player two", Faction.RED);

        // player two has 3 units and 3 structures
        entitiesSet.add(makeUnit(playerTwo));
        entitiesSet.add(makeUnit(playerTwo));
        entitiesSet.add(makeUnit(playerTwo));
        destroyers += 3;

        entitiesSet.add(makeStructure(playerTwo, 200));
        entitiesSet.add(makeStructure(playerTwo, 200));
        entitiesSet.add(makeStructure(playerTwo, 200));

        // Bare entities (with no behavior at all) - to test filtering
        entitiesSet.add(new DestroyedEntity(Coordinate.create(29, 30), mock(SpriteSheet.class), entityData, player, null));
        playerOneBareEntitiesCount = 1;
    }

    @Test
    public void filtersForPlayer() {
        Set<Entity> result = entitiesSet.filter(BelongsToPlayer.instance(player));
        assertEquals(playerOneStructureCount + playerOneUnitCount + playerOneBareEntitiesCount, result.size());
    }

    @Test
    public void filtersNotForPlayer() {
        // get everything except player one
        Set<Entity> result = entitiesSet.filter(new NotPredicate(BelongsToPlayer.instance(player)));
        // player 2 has 6 entities
        assertEquals(6, result.size());
    }

    @Test
    public void filtersSelectable() {
        Set<Entity> result = entitiesSet.filter(Predicate.isSelectable());
        assertEquals(entitiesSet.size() - playerOneBareEntitiesCount, result.size());
    }

    @Test
    public void filtersMovable() {
        Set<Entity> result = entitiesSet.filter(Predicate.builder().selectableMovableForPlayer(player));
        assertEquals(moveableUnitsOfPlayerOne, result.size());
    }

    @Test
    public void filtersUpdatable() {
        Set<Entity> result = entitiesSet.filter(Predicate.isUpdateable());
        assertEquals(entitiesSet.size() - playerOneBareEntitiesCount, result.size());
    }

    @Test
    public void filtersDestroyers() {
        Set<Entity> result = entitiesSet.filter(Predicate.isDestroyer());
        // only the units are capable of destroying stuff
        assertEquals(destroyers, result.size());
    }

    @Test
    public void filtersDestroyable() {
        Set<Entity> result = entitiesSet.filter(Predicate.isDestroyed());
        assertEquals(1, result.size());
    }

    @Test
    public void returnsThreeUnitsOfPlayerOneWithinRectangle() {
        Set<Entity> result = entitiesSet.filter(
                Predicate.builder().withinArea(
                        Rectangle.create(
                                topLeftFirstQuad.min(MapCoordinate.create(1, 1)).toCoordinate(),
                                topLeftFirstQuad
                                        .add(MapCoordinate.create(2, 2)) // topleft of quads area
                                        .toCoordinate() // to absolute coordinates
                                        .addHalfTile() // make sure we are at least over the center of the unit
                                        .add(Coordinate.create(1,1))) // and over it
                        )
                );
        assertEquals(3, result.size());
    }

    @Test
    public void returnsTenUnitsWithinPlayableMapBoundaries() throws Exception {
        Set<Entity> result = entitiesSet.filter(
                Predicate.builder().isWithinPlayableMapBoundaries(map));
        assertEquals(10, result.size());
    }

    @Test
    public void exclude() {
        assertTrue(entitiesSet.contains(quad));

        // Act
        EntitiesSet newSet = entitiesSet.exclude(quad);

        assertTrue(entitiesSet.contains(quad));
        assertFalse(newSet.contains(quad));
    }

    @Test
    public void getFirst() {
        assertNotNull(entitiesSet.getFirst()); // no order is guaranteed...
    }

    @Test
    public void hasItemsAndHasAny() {
        assertTrue(entitiesSet.hasItems());
        assertTrue(entitiesSet.hasAny());
    }

    class DestroyedEntity extends Entity implements Destructible {

        public DestroyedEntity(Coordinate mapCoordinates, SpriteSheet spriteSheet, EntityData entityData, Player player, EntityRepository entityRepository) {
            super(mapCoordinates, spriteSheet, entityData, player, entityRepository);
        }

        @Override
        public void render(Graphics graphics, int x, int y) {
            // leave empty so that we don't need openGL logic in our tests.
        }

        @Override
        public void update(float deltaInMs) {
            // leave empty, there is no update logic required for this simple entity
        }

        @Override
        public boolean isSelectable() {
            return false;
        }

        @Override
        public boolean isMovable() {
            return false;
        }

        @Override
        public boolean isUpdatable() {
            return false;
        }

        @Override
        public boolean isDestructible() {
            return true;
        }

        @Override
        public boolean isDestroyer() {
            return false;
        }

        @Override
        public EntityType getEntityType() {
            return EntityType.UNIT;
        }

        @Override
        public void die() {
            // empty
        }

        @Override
        public void takeDamage(int hitPoints, Entity origin) {
            // leave empty because we don't need to test damage taking logic
        }

        @Override
        public boolean isDestroyed() {
            return true;
        }

        @Override
        public int getHitPoints() {
            return 0;
        }
    }
}