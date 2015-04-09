package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.Rectangle;

public class EntityIsWithinAreaOfAbsoluteCoordinates extends Predicate<Entity> {

    private final Rectangle rectangle;

    public EntityIsWithinAreaOfAbsoluteCoordinates(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public boolean test(Entity entity) {
        return rectangle.isWithin(entity.getAbsoluteMapPixelCoordinates());
    }

    @Override
    public String toString() {
        return "EntityIsWithinArea{" + rectangle + '}';
    }

}
