package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableEntity;

import java.util.List;

/**
 * The implementing class is able to 'build' another entity. This basically means it holds a reference to the
 * type of entity it is 'building'. It also holds timers to make a construction duration possible.
 */
public interface EntityBuilder {

    List<BuildableEntity> getBuildList();

    /**
     * Returns true if construction of an entity is still busy.
     * Returns true when construction is finished, but requires placement.
     * @return
     */
    boolean isBuildingEntity();

    /**
     * Start construction of entity.
     * @param buildableEntity
     */
    void buildEntity(BuildableEntity buildableEntity);

    /**
     * Returns true when construction is completed and awaits placements.
     * @return
     */
    boolean isAwaitingPlacement();

    /**
     * Signals the builder that an entity is delivered (or placed) (if applicable).
     * This will effectively
     * @param entity
     */
    void entityIsDelivered(Entity entity);

    /**
     * Returns true if the buildableEntity is being built
     * @param buildableEntity
     * @return
     */
    boolean isBuildingEntity(BuildableEntity buildableEntity);
}
