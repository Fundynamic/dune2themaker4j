package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

/**
 * Inverses the result of the given predicate.
 */
public class NotPredicate extends Predicate<Entity> {

    private final Predicate<Entity> predicateToNegate;

    public NotPredicate(Predicate<Entity> predicateToNegate) {
        this.predicateToNegate = predicateToNegate;
    }

    @Override
    public boolean test(Entity entity) {
        return !predicateToNegate.test(entity);
    }
}
