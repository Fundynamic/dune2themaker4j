package com.fundynamic.d2tm.game.controls.battlefield;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.entitybuilders.PlacementBuildableEntity;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

public class LaunchSuperPowerMouseTest extends AbstractD2TMTest {

    private LaunchSuperPowerMouse launchSuperPowerMouse;

    @Before
    public void setUp() throws SlickException {
        super.setUp();

        EntityData deathhand = entityRepository.getEntityData(EntityType.SUPERPOWER, "DEATHHAND");

        PlacementBuildableEntity placementBuildableEntity =
                new PlacementBuildableEntity(deathhand, player,null);

        launchSuperPowerMouse = new LaunchSuperPowerMouse(battleField, map.getCell(0,0), placementBuildableEntity);
        battleField.setMouseBehavior(launchSuperPowerMouse);
    }

    @Test
    public void smokeTest() {
        // move to a coordinate
        launchSuperPowerMouse.movedTo(Vector2D.create(320, 320));

        // spawn super power
        launchSuperPowerMouse.leftClicked();

        // check super power is being spawned
        Entity lastCreatedEntity = entityRepository.getLastCreatedEntity();
        Assert.assertTrue(lastCreatedEntity.isSuperPower());
        Assert.assertEquals(lastCreatedEntity.getEntityData().name, "DEATHHAND");
    }

    @Test
    public void rightClicked() {
        launchSuperPowerMouse.rightClicked();

        AbstractBattleFieldMouseBehavior mouseBehavior = battleField.getMouseBehavior();

        Assert.assertTrue(mouseBehavior instanceof NormalMouse);
    }

}