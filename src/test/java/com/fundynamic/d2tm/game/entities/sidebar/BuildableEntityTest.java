package com.fundynamic.d2tm.game.entities.sidebar;

import com.fundynamic.d2tm.game.entities.EntityData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class BuildableEntityTest {

    private EntityData entityData;

    @Before
    public void setUp() {
        entityData = new EntityData();
        entityData.buildTimeInSeconds = 4.0f;
    }

    @Test
    public void buildStates() {
        BuildableEntity buildableEntity = new BuildableEntity(entityData);
        Assert.assertEquals(BuildableState.SELECTABLE, buildableEntity.getBuildableState());

        // start building
        buildableEntity.startBuilding();
        Assert.assertEquals(BuildableState.BUILDING, buildableEntity.getBuildableState());

        // update to 4 seconds
        buildableEntity.update(4.0f);
        Assert.assertEquals(BuildableState.BUILDING, buildableEntity.getBuildableState());

        // and after that it is awaiting placements
        buildableEntity.update(0.1f);
        Assert.assertEquals(BuildableState.AWAITSPLACEMENT, buildableEntity.getBuildableState());
        Assert.assertTrue(buildableEntity.awaitsPlacement());
    }

}