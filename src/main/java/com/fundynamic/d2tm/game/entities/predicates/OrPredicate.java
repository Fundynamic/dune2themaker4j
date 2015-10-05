package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

import java.util.LinkedList;
import java.util.List;

public class OrPredicate extends Predicate<Entity> {

    private List<Predicate<Entity>> predicates = new LinkedList<>();

    public OrPredicate(List<Predicate<Entity>> predicates) {
        this.predicates = predicates;
    }

    public OrPredicate() {
        this(new LinkedList<Predicate<Entity>>());
    }

    public OrPredicate addPredicate(Predicate<Entity> predicate) {
        this.predicates.add(predicate);
        return this;
    }

    @Override
    public boolean test(Entity entity) {
        for (Predicate predicate : predicates) {
            if (predicate.test(entity)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "OrPredicate{" +
                "predicates=" + predicates +
                '}';
    }
}
