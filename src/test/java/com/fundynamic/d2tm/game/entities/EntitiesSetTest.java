package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.structures.StructureFactory;
import com.fundynamic.d2tm.game.entities.units.UnitFactory;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class EntitiesSetTest {

    public static final int TILE_SIZE = 32;
    private EntitiesSet entitiesSet;

    private Player playerOne;

    private int playerOneUnitCount;
    private int playerOneStructureCount;
    private int playerOneBareEntitiesCount;
    private int destroyers;
    private int moveableUnitsOfPlayerOne;

    @Before
    public void setUp() {
        entitiesSet = new EntitiesSet();

        playerOne = new Player("Player one", Recolorer.FactionColor.GREEN);
        Player playerTwo = new Player("Player two", Recolorer.FactionColor.RED);

        // player one has 4 units and 2 structures
        entitiesSet.add(UnitFactory.makeUnit(playerOne, 100, Vector2D.create(10, 10)));
        entitiesSet.add(UnitFactory.makeUnit(playerOne, 200, Vector2D.create(12, 10)));
        entitiesSet.add(UnitFactory.makeUnit(playerOne, 300, Vector2D.create(10, 12)));
        entitiesSet.add(UnitFactory.makeUnit(playerOne, 200, Vector2D.create(30, 30)));
        playerOneUnitCount = 4;
        moveableUnitsOfPlayerOne = 4;
        destroyers = 4;

        entitiesSet.add(StructureFactory.makeStructure(playerOne, 200));
        entitiesSet.add(StructureFactory.makeStructure(playerOne, 200));
        playerOneStructureCount = 2;

        // player two has 3 units and 3 structures
        entitiesSet.add(UnitFactory.makeUnit(playerTwo, 100));
        entitiesSet.add(UnitFactory.makeUnit(playerTwo, 200));
        entitiesSet.add(UnitFactory.makeUnit(playerTwo, 300));
        destroyers += 3;

        entitiesSet.add(StructureFactory.makeStructure(playerTwo, 200));
        entitiesSet.add(StructureFactory.makeStructure(playerTwo, 200));
        entitiesSet.add(StructureFactory.makeStructure(playerTwo, 200));

        // Bare entities (with no behavior at all) - to test filtering
        entitiesSet.add(new DestroyedEntity(Vector2D.create(29, 30), mock(SpriteSheet.class), 1, playerOne));
        playerOneBareEntitiesCount = 1;
    }

    @Test
    public void filtersForPlayer() {
        Set<Entity> result = entitiesSet.filter(Predicate.builder().forPlayer(playerOne));
        assertEquals(playerOneStructureCount + playerOneUnitCount + playerOneBareEntitiesCount, result.size());
    }

    @Test
    public void filtersSelectable() {
        Set<Entity> result = entitiesSet.filter(Predicate.isSelectable());
        assertEquals(entitiesSet.size() - playerOneBareEntitiesCount, result.size());
    }

    @Test
    public void filtersMovable() {
        Set<Entity> result = entitiesSet.filter(Predicate.builder().selectableMovableForPlayer(playerOne));
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
                        Rectangle.create(Vector2D.create(9 * TILE_SIZE, 9 * TILE_SIZE), Vector2D.create(13 * TILE_SIZE, 13 * TILE_SIZE))
                )
        );
        assertEquals(3, result.size());
    }

    class DestroyedEntity extends Entity implements Destructible {

        public DestroyedEntity(Vector2D mapCoordinates, SpriteSheet spriteSheet, int sight, Player player) {
            super(mapCoordinates, spriteSheet, sight, player);
        }

        @Override
        public void render(Graphics graphics, int x, int y) {

        }

        @Override
        public void update(float deltaInMs) {

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
        public void takeDamage(int hitPoints) {

        }

        @Override
        public boolean isDestroyed() {
            return true;
        }
    }
}