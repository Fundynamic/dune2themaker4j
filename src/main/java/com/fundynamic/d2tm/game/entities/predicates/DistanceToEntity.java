package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.math.Coordinate;

/**
 * Returns true when a given entity is within 'range' compared to 'coordinate'
 */
public class DistanceToEntity extends Predicate<Entity> {

    private Coordinate coordinate;
    private float range;

    public DistanceToEntity(Coordinate coordinate, float range) {
        this.coordinate = coordinate;
        this.range = range;
    }

    @Override
    public boolean test(Entity entity) {
        Coordinate entityCoordinate = entity.getCenteredCoordinate();
        float distance = entityCoordinate.distance(coordinate);
//        System.out.println("Distance from " + coordinate + " to " + entityCoordinate + " is " + distance + " which is within range? " + range);
        return distance < range;
    }

}
