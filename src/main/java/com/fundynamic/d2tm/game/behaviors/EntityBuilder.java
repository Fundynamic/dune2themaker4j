package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;

import java.util.List;

/**
 * The implementing class is able to 'build' another entity. This basically means it holds a reference to the
 * type of entity it is 'building'. It also holds timers to make a construction duration possible.
 */
public interface EntityBuilder extends Updateable {

    List<AbstractBuildableEntity> getBuildList();

    /**
     * Returns true if construction of an entity is still busy.
     * Returns true when construction is finished, but requires placement.
     * @return
     */
    boolean isBuildingAnEntity();

    AbstractBuildableEntity getBuildingEntity();

    /**
     * Start construction of entity.
     * @param placementBuildableEntity
     */
    void buildEntity(AbstractBuildableEntity placementBuildableEntity);

    /**
     * Returns true when construction is completed and awaits placements - ie, needs player interaction
     * @return
     */
    boolean isAwaitingPlacement();

    /**
     * Returns true when construction is completed and awaits being spawned
     * @return
     */
    boolean isAwaitingSpawning();

    /**
     * Signals the builder that an entity is delivered (or placed) (if applicable).
     * This will effectively
     * @param entity
     */
    void entityIsDelivered(Entity entity);

    /**
     * Returns true if the buildableEntity is being built
     * @param placementBuildableEntity
     * @return
     */
    boolean isBuildingAnEntity(AbstractBuildableEntity placementBuildableEntity);
}
