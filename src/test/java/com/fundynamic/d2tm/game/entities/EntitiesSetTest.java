package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EntitiesSetTest extends AbstractD2TMTest {

    private EntitiesSet entitiesSet;

    private int playerOneUnitCount;
    private int playerOneStructureCount;
    private int playerOneBareEntitiesCount;
    private int destroyers;
    private int moveableUnitsOfPlayerOne;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        EntityData entityData = new EntityData(32, 32, 10);

        entitiesSet = new EntitiesSet();

        player = new Player("Player one", Recolorer.FactionColor.GREEN);
        Player playerTwo = new Player("Player two", Recolorer.FactionColor.RED);

        // player one has 4 units and 2 structures
        entitiesSet.add(makeUnit(player, 100, Vector2D.create(320, 320)));
        entitiesSet.add(makeUnit(player, 200, Vector2D.create(384, 320)));
        entitiesSet.add(makeUnit(player, 300, Vector2D.create(320, 384)));
        entitiesSet.add(makeUnit(player, 200, Vector2D.create(960, 960)));
        playerOneUnitCount = 4;
        moveableUnitsOfPlayerOne = 4;
        destroyers = 4;

        entitiesSet.add(makeStructure(player, 200));
        entitiesSet.add(makeStructure(player, 200));
        playerOneStructureCount = 2;

        // player two has 3 units and 3 structures
        entitiesSet.add(makeUnit(playerTwo, 100));
        entitiesSet.add(makeUnit(playerTwo, 200));
        entitiesSet.add(makeUnit(playerTwo, 300));
        destroyers += 3;

        entitiesSet.add(makeStructure(playerTwo, 200));
        entitiesSet.add(makeStructure(playerTwo, 200));
        entitiesSet.add(makeStructure(playerTwo, 200));

        // Bare entities (with no behavior at all) - to test filtering
        entitiesSet.add(new DestroyedEntity(Vector2D.create(29, 30), mock(SpriteSheet.class), entityData, player, null));
        playerOneBareEntitiesCount = 1;
    }

    @Test
    public void filtersForPlayer() {
        Set<Entity> result = entitiesSet.filter(Predicate.builder().forPlayer(player));
        assertEquals(playerOneStructureCount + playerOneUnitCount + playerOneBareEntitiesCount, result.size());
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
                                Vector2D.create(319, 319),
                                Vector2D.create(385, 385)
                        )
                )
        );
        assertEquals(3, result.size());
    }

    class DestroyedEntity extends Entity implements Destructible {

        public DestroyedEntity(Vector2D mapCoordinates, SpriteSheet spriteSheet, EntityData entityData, Player player, EntityRepository entityRepository) {
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
        public void takeDamage(int hitPoints) {
            // leave empty because we don't need to test damage taking logic
        }

        @Override
        public boolean isDestroyed() {
            return true;
        }
    }
}