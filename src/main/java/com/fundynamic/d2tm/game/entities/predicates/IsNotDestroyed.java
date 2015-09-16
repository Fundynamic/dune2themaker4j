package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsNotDestroyed extends Predicate<Entity> {

    public static IsNotDestroyed instance = new IsNotDestroyed();

    @Override
    public boolean test(Entity entity) {
        return entity.isDestructible() && !((Destructible) entity).isDestroyed();
    }

    @Override
    public String toString() {
        return "IsNotDestroyed";
    }

}
