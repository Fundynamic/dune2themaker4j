package com.fundynamic.d2tm.game.entities.predicates;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;

/**
 * Returns true when entity matches another entity.
 *
 * Useful to filter out an entity from a list of entities.
 *
 */
public class IsEntity extends Predicate<Entity> {

    private final Entity entityToMatch;

    public IsEntity(Entity entityToMatch) {
        this.entityToMatch = entityToMatch;
    }

    @Override
    public boolean test(Entity entity) {
        return false;
    }

}
