package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsDestroyed extends Predicate<Entity> {

    public static IsDestroyed instance = new IsDestroyed();

    @Override
    public boolean test(Entity entity) {
        return entity.isDestroyed();
    }

    @Override
    public String toString() {
        return "IsDestroyed";
    }

}
