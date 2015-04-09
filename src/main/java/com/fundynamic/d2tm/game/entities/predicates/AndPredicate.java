package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

import java.util.LinkedList;
import java.util.List;

public class AndPredicate extends Predicate<Entity> {

    private List<Predicate<Entity>> predicates = new LinkedList<>();

    public AndPredicate(List<Predicate<Entity>> predicates) {
        this.predicates = predicates;
    }

    public AndPredicate() {
        this(new LinkedList<Predicate<Entity>>());
    }

    public AndPredicate addPredicate(Predicate<Entity> predicate) {
        this.predicates.add(predicate);
        return this;
    }

    @Override
    public boolean test(Entity entity) {
        for (Predicate predicate : predicates) {
            if (!predicate.test(entity)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AndPredicate{" +
                "predicates=" + predicates +
                '}';
    }
}
