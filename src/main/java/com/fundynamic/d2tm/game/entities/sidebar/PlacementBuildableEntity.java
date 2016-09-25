package com.fundynamic.d2tm.game.entities.sidebar;


import com.fundynamic.d2tm.game.entities.EntityData;

/**
 * <h1>General</h1>
 * An entity that can be built by a {@link com.fundynamic.d2tm.game.behaviors.EntityBuilder}. It contains little
 * state giving hints if it can be built, its progress and so forth.
 */
public class PlacementBuildableEntity extends AbstractBuildableEntity {

    public PlacementBuildableEntity(EntityData entityData) {
        super(entityData);
    }

    @Override
    public void update(float deltaInSeconds) {
        super.update(deltaInSeconds);
        if (isDoneBuilding()) {
            this.buildableState = BuildableState.BUILDING_FINISHED_AWAITS_PLACEMENT;
        }
    }

}
