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
    /**
     * Alias for {@link #isNotDestroyed()}
     * @return
     */
    public static Predicate<Entity> isAlive() {
        return isNotDestroyed();
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

    public static Predicate<Entity> ofType(EntityType entityType) {
        return OfType.instance(entityType);
    }

    public static Predicate<Entity> ofTypes(EntityType[] entityTypes) {
        OrPredicate orPredicate = new OrPredicate();
        for (EntityType entityType : entityTypes) {
            orPredicate.addPredicate(ofType(entityType));
        }
        return orPredicate;
    }

    public static Predicate<Entity> isEntityBuilder() {
        return IsEntityBuilder.instance;
    }
}
