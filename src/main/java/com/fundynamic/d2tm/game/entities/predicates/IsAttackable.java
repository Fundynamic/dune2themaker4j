package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsAttackable extends Predicate<Entity> {

    public static IsAttackable instance = new IsAttackable();

    @Override
    public boolean test(Entity entity) {
        return entity.isAttackable();
    }

    @Override
    public String toString() {
        return "IsAttackable";
    }

}
