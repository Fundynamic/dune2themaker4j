package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An entities set is an extension of a {@link HashSet} with type {@link Entity}. It provides easy to use methods
 * and filters.
 */
public class EntitiesSet extends HashSet<Entity> {

    public static EntitiesSet fromSet(Set<Entity> entities) {
        EntitiesSet result = new EntitiesSet();
        result.addAll(entities);
        return result;
    }

    public static EntitiesSet fromSingle(Entity entity) {
        EntitiesSet result = new EntitiesSet();
        result.add(entity);
        return result;
    }

    public EntitiesSet filter(Predicate<Entity> predicate) {
        EntitiesSet result = new EntitiesSet();
        for (Entity entity : this) {
            if (predicate.test(entity)) {
                result.add(entity);
            }
        }
        return result;
    }

    /**
     * Short hand method that calls the `build` method on the predicateBuilder
     * @param predicateBuilder
     * @return
     */
    public EntitiesSet filter(PredicateBuilder predicateBuilder) {
        return filter(predicateBuilder.build());
    }

    public List<Entity> toList() {
        return new ArrayList<>(this);
    }

    /**
     *
     * Returns first element or null when size is 0.
     *
     * Be aware, that since this is a set, its order is not guaranteed.
     *
     * @return
     */
    public Entity getFirst() {
        if (hasAny()) {
            return toList().get(0);
        }
        return null;
    }

    public Entity getFirst(Predicate<Entity> predicate) {
        EntitiesSet filter = filter(predicate);
        if (filter.hasItems()) {
            return filter.getFirst();
        }
        return null;
    }


    @Override
    public boolean add(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("Cannot add null to entity set");
        return super.add(entity);
    }

    /**
     * This method iterates over all entities within this set, and passes them to the EntityHandler handle method.
     *
     * @param entityHandler
     */
    public void each(EntityHandler entityHandler) {
        for (Entity entity : this) {
            entityHandler.handle(entity);
        }
    }

    /**
     * Equivalent of doing <code>!isEmpty()</code>
     * @return
     */
    public boolean hasItems() {
        return !isEmpty();
    }

    /**
     * Synonym for {@link #hasItems()}
     * @return
     */
    public boolean hasAny() {
        return hasItems();
    }

    public boolean hasOne() {
        return size() == 1;
    }

    @Override
    public String toString() {
        String result = "";
        for (Entity entity : this) {
            result += entity.toString() + "\n";
        }
        return result;
    }

    public static EntitiesSet empty() {
        return new EntitiesSet();
    }

    public EntitiesSet exclude(Entity entityToExclude) {
        EntitiesSet entitiesSet = EntitiesSet.fromSet(this);
        entitiesSet.remove(entityToExclude);
        return entitiesSet;
    }

    public boolean sameSizeAs(EntitiesSet other) {
        return this.size() == other.size();
    }
}
