package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsEntityBuilder extends Predicate<Entity> {

    public static IsEntityBuilder instance = new IsEntityBuilder();

    @Override
    public boolean test(Entity entity) {
        return entity.isEntityBuilder();
    }

    @Override
    public String toString() {
        return "IsEntityBuilder";
    }

}
