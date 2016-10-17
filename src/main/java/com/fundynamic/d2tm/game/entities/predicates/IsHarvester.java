package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsHarvester extends Predicate<Entity> {

    public static IsHarvester instance = new IsHarvester();

    @Override
    public boolean test(Entity entity) {
        return entity.isHarvester();
    }

    @Override
    public String toString() {
        return "IsHarvester";
    }

}
