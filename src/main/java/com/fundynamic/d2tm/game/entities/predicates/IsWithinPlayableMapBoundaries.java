package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.map.Map;

public class IsWithinPlayableMapBoundaries extends Predicate<Entity> {

    private final Map map;

    public IsWithinPlayableMapBoundaries(Map map) {
        super();
        this.map = map;
    }

    @Override
    public boolean test(Entity entity) {
        return map.isWithinPlayableMapBoundaries(entity.getCoordinate().toMapCoordinate());
    }
}
