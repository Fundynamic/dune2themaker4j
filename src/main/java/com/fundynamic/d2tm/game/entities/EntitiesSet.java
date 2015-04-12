package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;

import java.util.HashSet;
import java.util.Set;

public class EntitiesSet extends HashSet<Entity> {

    public Set<Entity> filter(Predicate<Entity> predicate) {
        Set<Entity> result = new HashSet<>();
        for (Entity entity : this) {
            if (predicate.test(entity)) {
                result.add(entity);
            }
        }
        return result;
    }

    public Set<Entity> filter(PredicateBuilder predicateBuilder) {
        Predicate predicate = predicateBuilder.build();
        Set<Entity> results = filter(predicate);
        System.out.println("Filter with predicate " + predicate + " - yields " + results.size() + " results: \n" + results);
        return results;
    }

}
