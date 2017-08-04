package com.fundynamic.d2tm.game.entities.sidebar;

import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.entitybuilders.PlacementBuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.types.EntityData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PlacementBuildableEntityTest {

    private EntityData entityData;
    private Player player;

    @Before
    public void setUp() {
        player = new Player("Human player", Recolorer.FactionColor.BLUE);

        entityData = new EntityData(); // the thing that is being 'built'
        entityData.buildTimeInSeconds = 4.0f;
    }

    @Test
    public void buildStates() {
        PlacementBuildableEntity placementBuildableEntity = new PlacementBuildableEntity(entityData, player, null);
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
        PlacementBuildableEntity placementBuildableEntity = new PlacementBuildableEntity(entityData, player, null);
        placementBuildableEntity.startBuilding();
        Assert.assertEquals(0.0F, placementBuildableEntity.getProgress(), 000.1f);

        placementBuildableEntity.update(entityData.buildTimeInSeconds / 2);
        Assert.assertEquals(0.5F, placementBuildableEntity.getProgress(), 000.1f);

        placementBuildableEntity.update(entityData.buildTimeInSeconds / 2);
        Assert.assertEquals(1.0F, placementBuildableEntity.getProgress(), 000.1f);
    }

}