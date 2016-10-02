package com.fundynamic.d2tm.game.entities.sidebar;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.entitybuilders.PlacementBuildableEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PlacementBuildableEntityTest {

    private EntityData entityData;

    @Before
    public void setUp() {
        entityData = new EntityData();
        entityData.buildTimeInSeconds = 4.0f;
    }

    @Test
    public void buildStates() {
        PlacementBuildableEntity placementBuildableEntity = new PlacementBuildableEntity(entityData, null);
        Assert.assertEquals(BuildableState.SELECTABLE, placementBuildableEntity.getBuildableState());

        // start building
        placementBuildableEntity.startBuilding();
        Assert.assertEquals(BuildableState.BUILDING, placementBuildableEntity.getBuildableState());

        // update to 4 seconds
        placementBuildableEntity.update(4.0f);
        Assert.assertEquals(BuildableState.BUILDING, placementBuildableEntity.getBuildableState());

        // and after that it is awaiting placements
        placementBuildableEntity.update(0.1f);
        Assert.assertEquals(BuildableState.BUILDING_FINISHED_AWAITS_PLACEMENT, placementBuildableEntity.getBuildableState());
        Assert.assertTrue(placementBuildableEntity.awaitsPlacement());
    }

    @Test
    public void progress() {
        PlacementBuildableEntity placementBuildableEntity = new PlacementBuildableEntity(entityData, null);
        placementBuildableEntity.startBuilding();
        Assert.assertEquals(0.0F, placementBuildableEntity.getProgress(), 000.1f);

        placementBuildableEntity.update(entityData.buildTimeInSeconds / 2);
        Assert.assertEquals(0.5F, placementBuildableEntity.getProgress(), 000.1f);

        placementBuildableEntity.update(entityData.buildTimeInSeconds / 2);
        Assert.assertEquals(1.0F, placementBuildableEntity.getProgress(), 000.1f);
    }

}