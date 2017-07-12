package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class EntityTest extends AbstractD2TMTest {

    private TestableEntity entity1;
    private TestableEntity entity2;
    private Coordinate topLeftCoordinate;
    private EntityData entityData;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        topLeftCoordinate = Coordinate.create(Game.TILE_SIZE * 4, Game.TILE_SIZE * 4); // 4X32 = 128
        entityData = new EntityData();
        entityData.setWidth(64);
        entityData.setHeight(64);

        entity1 = new TestableEntity(topLeftCoordinate, mock(SpriteSheet.class), entityData, player, entityRepository).
                setName("Entity1");
        entity2 = new TestableEntity(topLeftCoordinate, mock(SpriteSheet.class), entityData, player, entityRepository).
                setName("Entity2");
    }

    @Test
    public void randomPositionWithinDoesNotExceedBoundaries() {

        // choose an arbitrary amount of calls (100) and just try it.
        // Yeah, this could be more efficient with using a seed and whatnot...
        for (int i = 0; i < 100; i++) {
            Vector2D randomPositionWithin = entity1.getRandomPositionWithin();
            assertThat(randomPositionWithin.getX(), is(not(lessThan(topLeftCoordinate.getX() - 16))));
            assertThat(randomPositionWithin.getY(), is(not(lessThan(topLeftCoordinate.getY() - 16))));
            assertThat(randomPositionWithin.getX(), is(not(greaterThan(topLeftCoordinate.getX() + entityData.getWidth()))));
            assertThat(randomPositionWithin.getY(), is(not(greaterThan(topLeftCoordinate.getY() + entityData.getHeight()))));
        }
    }

    @Test
    public void getAllSurroundingCoordinatesOfAnEntity() {
        // entity1 is 64x64 pixels (ie, 2x2). Marked as X
        // The tiles we would expect are marked as E
        // EEEE  4
        // EXXE  2
        // EXXE  2
        // EEEE  4
        //
        // 4 + 2 + 2 + 4 => 12 surrounding cells
        List<MapCoordinate> allSurroundingCellsAsCoordinates = entity1.getAllSurroundingCellsAsCoordinates();

        Assert.assertEquals(12, allSurroundingCellsAsCoordinates.size());

        // we expect the first tile to be at 1 tile above and 1 tile left to the entity1:
        MapCoordinate upLeftOfTopLeft = allSurroundingCellsAsCoordinates.get(0);

        Assert.assertEquals(3, upLeftOfTopLeft.getXAsInt());
        Assert.assertEquals(3, upLeftOfTopLeft.getYAsInt());
    }

    //////////////////////////////////////////////////
    // Event handling, subscribers, etc
    /////////////////////////////////////////////////
    // This is how it is set up:
    // Event2, subscribes to an ENTITY_DESTROYED event for entity1. So when Entity1 gets destroyed, Entity2 will
    // be notified.
    //
    // By Event2's subscription on Event1, when Event2 gets destroyed, Event1 will know to stop holding the reference
    // to Event2 (allowing GC to clear it up).
    //
    // Thus both Entities are tied by an OnEvent: Entity2 -> Entity1, and Entity1 -> Entity2
    //
    // When Entity1 gets destroyed first, it will no longer notify other Entities, thus all subscriptions should
    // be cleared.

    @Test
    public void onEventOnOwnEntityWillHaveOnlyOneDestroySubscription() {
        entity1.onEvent(EventType.DUMMY, entity1, s -> s.eventMethod());

        Assert.assertEquals(0, entity1.getAmountEventMethodCalled()); // nothing triggered the DUMMY event yet

        // expect one subscription (because event triggers on own object)
        Assert.assertEquals(1, entity1.eventSubscriptionsFor(EventType.ENTITY_DESTROYED).size());
    }

    @Test
    public void Entity1EmitsDummyEventCallsExpectedMethodAndRaisesValue() {
        entity1.onEvent(EventType.DUMMY, entity2, s -> s.eventMethod());

        // at first we expect no methods have been called yet (no events happened)
        Assert.assertEquals(0, entity1.getAmountEventMethodCalled());
        Assert.assertEquals(0, entity2.getAmountEventMethodCalled());

        entity1.emitEvent(EventType.DUMMY); // some dummy event happened!

        Assert.assertEquals(0, entity1.getAmountEventMethodCalled()); // entity1 was not interested
        Assert.assertEquals(1, entity2.getAmountEventMethodCalled()); // entity2 was interested and should have raised value
    }

    @Test
    public void givenEntityDestroyedWillRemoveSubscriberFromDestroyedEntity() {
        // when event1 gets destroyed, call 'eventMethod' on event2
        entity1.onEvent(EventType.DUMMY, entity2, s -> s.eventMethod());

        // For entity1: expect the subscription of entity2 upon ENTITY_DESTROYED
        Assert.assertEquals(1, entity1.eventSubscriptionsFor(EventType.DUMMY).size()); // 1 subscription
        Entity.EventSubscription<? extends Entity> eventSubscription = entity1.eventSubscriptionsFor(EventType.DUMMY).get(0);
        Assert.assertSame(eventSubscription.getSubscriber(), entity2); // and its subscriber is entity2

        // For entity2: expect the subscription of entity1 upon ENTITY_DESTROYED, so that it knows no longer
        // to call entity2's method in case entity2 gets destroyed
        Assert.assertEquals(1, entity2.eventSubscriptionsFor(EventType.ENTITY_DESTROYED).size());
        eventSubscription = entity2.eventSubscriptionsFor(EventType.ENTITY_DESTROYED).get(0);
        Assert.assertSame(eventSubscription.getSubscriber(), entity1); // the entity1 to be notified if entity2 gets destroyed

        // ACT: Entity1 gets destroyed!
        entity1.destroy();

        // expect no subscriptions for Entity1, because they should be cleared
        Assert.assertEquals(0, entity1.eventSubscriptionsFor(EventType.ENTITY_DESTROYED).size());

        // Entity2 should no longer notify entity1 upon its destroy, because entity1 is no longer among us...
        Assert.assertEquals(0, entity2.eventSubscriptionsFor(EventType.ENTITY_DESTROYED).size());

        entity1 = null;
        System.gc(); // clear out all references to entity1, and clear memory.

        // should be able to destroy entity2 without problems now
        entity2.destroy();
    }

    @Test
    public void Entity1EmitsDummyEventCallsExpectedMethodAndRaisesValueAtMultipleSubscribers() {
        TestableEntity entity3 = new TestableEntity(topLeftCoordinate, mock(SpriteSheet.class), entityData, player, entityRepository).
                setName("Entity3");

        entity1.onEvent(EventType.DUMMY, entity2, s -> s.eventMethod());
        entity1.onEvent(EventType.DUMMY, entity3, s -> s.eventMethod());

        // at first we expect no methods have been called yet (no events happened)
        Assert.assertEquals(0, entity1.getAmountEventMethodCalled());
        Assert.assertEquals(0, entity2.getAmountEventMethodCalled());
        Assert.assertEquals(0, entity3.getAmountEventMethodCalled());

        entity1.emitEvent(EventType.DUMMY); // some dummy event happened!

        Assert.assertEquals(0, entity1.getAmountEventMethodCalled()); // entity1 was not interested
        Assert.assertEquals(1, entity2.getAmountEventMethodCalled()); // entity2 was interested and should have raised value
        Assert.assertEquals(1, entity3.getAmountEventMethodCalled()); // entity2 was interested and should have raised value
    }

    @Test
    public void SubscribingMoreThanOnceOnSameEntityWilCallMethodMultipleTimes() {
        // woops subscribing too much
        entity1.onEvent(EventType.DUMMY, entity2, s -> s.eventMethod());
        entity1.onEvent(EventType.DUMMY, entity2, s -> s.eventMethod());
        entity1.onEvent(EventType.DUMMY, entity2, s -> s.eventMethod());
        entity1.onEvent(EventType.DUMMY, entity2, s -> s.eventMethod());

        entity1.emitEvent(EventType.DUMMY); // some dummy event happened!

        // expected to be raised to 4
        Assert.assertEquals(4, entity2.getAmountEventMethodCalled());
    }

    // destroy without any events set, check NPE , etc
}