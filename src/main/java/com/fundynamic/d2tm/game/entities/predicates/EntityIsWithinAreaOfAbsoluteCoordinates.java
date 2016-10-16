package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.math.Rectangle;

/**
 * This predicate tests that the given entity's absolute pixel map coordinates are within a given Rectangle.
 * This means the coordinates passed in the Rectangle should also be absolute pixel map coordinates.
 *
 */
public class EntityIsWithinAreaOfAbsoluteCoordinates extends Predicate<Entity> {

    private final Rectangle rectangle;

    public EntityIsWithinAreaOfAbsoluteCoordinates(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public boolean test(Entity entity) {
        return rectangle.isVectorWithin(entity.getCenteredCoordinate());
    }

    @Override
    public String toString() {
        return "EntityIsWithinArea{" + rectangle + '}';
    }

}
