package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Predicate;

import java.util.HashMap;

public class OfType extends Predicate<Entity> {

    private static HashMap<EntityType, OfType> instances = new HashMap<>();

    private final EntityType entityType;

    public OfType(EntityType entityType) {
        if (entityType == null) throw new IllegalArgumentException("Cannot test for null Entity type");
        this.entityType = entityType;
    }

    @Override
    public boolean test(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("Cannot test on null entity");
        return entityType.equals(entity.getEntityType());
    }

    public static Predicate<Entity> instance(EntityType entityType) {
        if (!instances.containsKey(entityType)) {
            instances.put(entityType, new OfType(entityType));
        }
        return instances.get(entityType);
    }
}
