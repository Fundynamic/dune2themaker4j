package com.fundynamic.d2tm.game.entities.entitybuilders;


import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableState;
import com.fundynamic.d2tm.game.types.EntityData;

/**
 * <h1>General</h1>
 * <p>
 * This is an object that holds a reference to the EntityData it is 'building'. The super methods of
 * {@link AbstractBuildableEntity} do the time-based progress logic. This `Placement` entity simply sets
 * the state to {@link BuildableState}<em>BUILDING_FINISHED_AWAITS_PLACEMENT</em>, so that an entity is not just
 * spawned, but awaiting action from the GUI to place this entity.
 * </p>
 * <p>
 *     Once this object is 'placed' on the map, to get the actual thing that is built, get the
 *     EntityData from this object. If you want to know 'who' (which entity) was 'building' this
 *     use the {@link entityWhoConstructsThis}
 * </p>
 */
public class PlacementBuildableEntity extends AbstractBuildableEntity {

    private Entity entityWhoConstructsThis;

    public PlacementBuildableEntity(EntityData entityData, Player player, Entity entityWhoConstructsThis) {
        super(entityData, player);
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
