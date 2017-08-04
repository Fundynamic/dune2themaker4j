package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.types.EntityData;

import java.util.List;

/**
 * The implementing class is able to 'build' another entity. This basically means it holds a reference to the
 * type of entity it is 'building'. It also holds timers to make a construction duration possible.
 */
public interface EntityBuilder extends Updateable {

    List<AbstractBuildableEntity> getBuildList();

    /**
     * Returns if construction of an entity is still busy or when construction is finished, but requires placement / spawning.
     *
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
     * Returns true when construction is completed and awaits placements - ie, needs player interaction.
     * Uses entityData comparison to confirm, taken from abstractBuildableEntity
     * @return
     */
    boolean isAwaitingPlacement(AbstractBuildableEntity placementBuildableEntity);

    /**
     * Same as above, by using entityData
     * @param entityData
     * @return
     */
    boolean isAwaitingPlacement(EntityData entityData);

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
