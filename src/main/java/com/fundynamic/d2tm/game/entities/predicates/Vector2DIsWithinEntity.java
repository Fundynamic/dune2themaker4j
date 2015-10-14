package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;


public class Vector2DIsWithinEntity extends Predicate<Entity> {

    private Vector2D vector;

    public Vector2DIsWithinEntity(Vector2D vector) {
        this.vector = vector;
    }

    @Override
    public boolean test(Entity entity) {
        Rectangle rectangle = Rectangle.createWithDimensions(entity.getAbsoluteMapCoordinates(), entity.getDimensions());
        return rectangle.isVectorWithin(vector);
    }

}
