package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsSelectable extends Predicate<Entity> {

    public static IsSelectable instance = new IsSelectable();

    @Override
    public boolean test(Entity entity) {
        return entity.isSelectable();
    }

    @Override
    public String toString() {
        return "IsSelectable";
    }
}
