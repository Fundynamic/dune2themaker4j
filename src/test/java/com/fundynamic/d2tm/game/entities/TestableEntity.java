package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

public class TestableEntity extends Entity {

    public TestableEntity(Coordinate coordinate, SpriteSheet spritesheet, EntityData entityData, Player player, EntityRepository entityRepository) {
        super(coordinate, spritesheet, entityData, player, entityRepository);
    }

    private String name;

    public String getName() {
        return name;
    }

    public TestableEntity setName(String name) {
        this.name = name;
        return this;
    }

    private int v;

    @Override
    public EntityType getEntityType() {
        return null;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        // no rendering, boring entity
    }

    @Override
    public void update(float deltaInSeconds) {
        // no updating, boring entity
    }

    public boolean containsSubscriberFor(EventType eventType) {
        if (!this.eventTypeListMap.containsKey(eventType)) return false;

        List<EventSubscription<? extends Entity>> eventSubscriptions = this.eventTypeListMap.get(eventType);
        return eventSubscriptions.size() > 0;
    }

    public List<EventSubscription<? extends Entity>> eventSubscriptionsFor(EventType eventType) {
        if (!this.eventTypeListMap.containsKey(eventType)) return new ArrayList<>();
        List<EventSubscription<? extends Entity>> eventSubscriptions = eventTypeListMap.get(eventType);
        return eventSubscriptions;
    }

    public Void eventMethod() {
        v++;
        return null;
    }

    public int getAmountEventMethodCalled() {
        return v;
    }

    @Override
    public String toString() {
        return "TestableEntity{" +
                "name='" + name + '\'' +
                ", v=" + v +
                '}';
    }
}
