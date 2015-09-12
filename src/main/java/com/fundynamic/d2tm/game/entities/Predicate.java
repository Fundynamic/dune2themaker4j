package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.entities.predicates.*;

public abstract class Predicate<T> {

    public abstract boolean test(T t);

    public static PredicateBuilder builder() {
        return new PredicateBuilder();
    }

    public static Predicate<Entity> isNotDestroyed() {
        return IsNotDestroyed.instance;
    }

    public static Predicate<Entity> isDestroyer() {
        return IsDestroyer.instance;
    }

    public static Predicate<Entity> isDestroyed() {
        return IsDestroyed.instance;
    }

    public static Predicate<Entity> isSelected() {
        return IsSelected.instance;
    }

    public static Predicate<Entity> isUpdateable() {
        return IsUpdateable.instance;
    }

    public static Predicate<Entity> isSelectable() {
        return IsSelectable.instance;
    }
}
