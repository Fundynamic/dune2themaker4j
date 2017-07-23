package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

import java.util.LinkedList;
import java.util.List;

/**
 * The any predicate will always test all its internal predicates, if any of the predicates yields true the test method
 * of the AnyPredicate will return true.
 */
public class AnyPredicate extends Predicate<Entity> {

    private List<Predicate<Entity>> predicates = new LinkedList<>();

    public AnyPredicate(List<Predicate<Entity>> predicates) {
        this.predicates = predicates;
    }

    public AnyPredicate(Predicate<Entity> predicate) {
        this();
        addPredicate(predicate);
    }

    public AnyPredicate() {
        this(new LinkedList<Predicate<Entity>>());
    }

    public AnyPredicate addPredicate(Predicate<Entity> predicate) {
        this.predicates.add(predicate);
        return this;
    }

    @Override
    public boolean test(Entity entity) {
        boolean result = false;
        for (Predicate predicate : predicates) {
            if (predicate.test(entity)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "AnyPredicate{" +
                "predicates=" + predicates +
                '}';
    }
}
