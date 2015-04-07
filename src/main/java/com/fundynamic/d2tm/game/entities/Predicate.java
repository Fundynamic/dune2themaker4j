package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;

public abstract class Predicate<T> {

    public abstract boolean test(T t);

    public static PredicateBuilder builder() {
        return new PredicateBuilder();
    }

}
