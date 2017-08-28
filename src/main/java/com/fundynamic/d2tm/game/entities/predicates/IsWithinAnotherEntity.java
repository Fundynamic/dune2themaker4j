package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsWithinAnotherEntity extends Predicate<Entity> {

    public static IsWithinAnotherEntity instance = new IsWithinAnotherEntity();

    @Override
    public boolean test(Entity entity) {
        return entity.isWithinOtherEntity();
    }

    @Override
    public String toString() {
        return "IsWithinAnotherEntity";
    }

}
