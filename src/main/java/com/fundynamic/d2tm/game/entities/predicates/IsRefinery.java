package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsRefinery extends Predicate<Entity> {

    public static IsRefinery instance = new IsRefinery();

    @Override
    public boolean test(Entity entity) {
        return entity.isRefinery();
    }

    @Override
    public String toString() {
        return "IsRefinery";
    }

}
