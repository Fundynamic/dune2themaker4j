package com.fundynamic.d2tm.game.entities.entitybuilders;

import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.NullEntity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableState;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AbstractBuildableEntityTest {

    public static final int BUILD_COST = 500;
    private EntityData entityData;
    private Player player;
    private AbstractBuildableEntity buildableEntity;

    @Before
    public void setUp() {
        entityData = new EntityData();
        entityData.buildTimeInSeconds = 1;
        entityData.buildCost = BUILD_COST;

        player = new Player("Human player", Recolorer.FactionColor.BLUE);

        buildableEntity = new BuildableEntity(entityData, player);
    }

    @Test
    public void givenEnoughMoneyWhenEnablingItIsSelectable() {
        player.setCredits(BUILD_COST);

        buildableEntity.enable();

        Assert.assertEquals(BuildableState.SELECTABLE, buildableEntity.getBuildableState());
    }

    @Test
    public void givenNotEnoughMoneyWhenEnablingItIsSelectableTooExpensive() {
        player.setCredits(BUILD_COST - 1);

        buildableEntity.enable();

        Assert.assertEquals(BuildableState.SELECTABLE_TOO_EXPENSIVE, buildableEntity.getBuildableState());
    }

    @Test
    public void givenAwaitsPlacementWhenEnoughMoneyAndEnablingItShouldBeSelectable() {
        player.setCredits(BUILD_COST);
        entityData.buildCost = BUILD_COST / 5;

        buildableEntity = new PlacementBuildableEntity(entityData, player, new NullEntity());

        buildableEntity.startBuilding();
        buildableEntity.update(1.1f);

        Assert.assertTrue(buildableEntity.awaitsPlacement());
        Assert.assertTrue(buildableEntity.isDoneBuilding());

        // after placement, it is re-enabled again
        buildableEntity.enable();
        Assert.assertFalse(buildableEntity.awaitsPlacement());
        Assert.assertFalse(buildableEntity.isDoneBuilding());

        Assert.assertEquals(BuildableState.SELECTABLE, buildableEntity.getBuildableState());
    }

    public class BuildableEntity extends AbstractBuildableEntity {

        public BuildableEntity(EntityData entityData, Player player) {
            super(entityData, player);
        }
    }

}