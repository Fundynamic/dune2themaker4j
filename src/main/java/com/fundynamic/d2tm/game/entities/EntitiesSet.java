package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EntitiesSet extends HashSet<Entity> {

    public EntitiesSet filter(Predicate<Entity> predicate) {
        EntitiesSet result = new EntitiesSet();
        for (Entity entity : this) {
            if (predicate.test(entity)) {
                result.add(entity);
            }
        }
        return result;
    }

    public EntitiesSet filter(PredicateBuilder predicateBuilder) {
        Predicate predicate = predicateBuilder.build();
        return filter(predicate);
    }

    public List<Entity> asList() {
        return new ArrayList<>(this);
    }

}
