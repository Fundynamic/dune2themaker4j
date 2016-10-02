package com.fundynamic.d2tm.game.entities.entitybuilders;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableState;

/**
 * <h1>General</h1>
 * An entity that can be built by a {@link com.fundynamic.d2tm.game.behaviors.EntityBuilder}. It contains little
 * state giving hints if it can be built, its progress and so forth.
 */
public class PlacementBuildableEntity extends AbstractBuildableEntity {

    private Entity entityWhoConstructsThis;

    public PlacementBuildableEntity(EntityData entityData, Entity entityWhoConstructsThis) {
        super(entityData);
        this.entityWhoConstructsThis = entityWhoConstructsThis;
    }

    @Override
    public void update(float deltaInSeconds) {
        super.update(deltaInSeconds);
        if (isDoneBuilding()) {
            this.buildableState = BuildableState.BUILDING_FINISHED_AWAITS_PLACEMENT;
        }
    }

    public Entity getEntityWhoConstructsThis() {
        return entityWhoConstructsThis;
    }
}
