package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

public class IsUpdateable extends Predicate<Entity> {

    public static IsUpdateable instance = new IsUpdateable();

    @Override
    public boolean test(Entity entity) {
        return entity.isUpdatable();
    }

    @Override
    public String toString() {
        return "IsUpdateable";
    }
}
