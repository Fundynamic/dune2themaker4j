package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.math.Coordinate;

public class CoordinateIsWithinEntity extends Predicate<Entity> {

    private Coordinate vector;

    public CoordinateIsWithinEntity(Coordinate coordinate) {
        this.vector = coordinate;
    }

    @Override
    public boolean test(Entity entity) {
        return entity.isVectorWithin(vector);
    }

    @Override
    public String toString() {
        return "CoordinateIsWithinEntity{" +
                "vector=" + vector +
                '}';
    }
}
