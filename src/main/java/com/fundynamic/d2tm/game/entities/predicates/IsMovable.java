package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsMovable extends Predicate<Entity> {

    public static IsMovable instance = new IsMovable();

    @Override
    public boolean test(Entity entity) {
        return entity.isMovable();
    }

    @Override
    public String toString() {
        return "IsMovable";
    }

}
