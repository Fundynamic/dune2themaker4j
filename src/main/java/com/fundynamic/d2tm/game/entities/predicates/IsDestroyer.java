package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsDestroyer extends Predicate<Entity> {

    public static IsDestroyer instance = new IsDestroyer();

    @Override
    public boolean test(Entity entity) {
        return entity.isDestroyer();
    }

    @Override
    public String toString() {
        return "IsDestroyer";
    }

}
