package com.fundynamic.d2tm.game.entities.superpowers;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

public class SuperPowerTest extends AbstractD2TMTest {

    private SuperPower superPower;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        EntityData weaponEntityData = entityRepository.getEntityData(EntityType.SUPERPOWER, "DEATHHAND");
        superPower = new SuperPower(new Coordinate(0, 0), weaponEntityData, player, entityRepository);
        superPower.setFireStarterCoordinate(Coordinate.create(100, 100));
    }

    @Test
    public void damage() {
        // 0 damage, too far away
        Assert.assertEquals(0, superPower.getDamageHitpoints(100, 100, 100));
        Assert.assertEquals(10, superPower.getDamageHitpoints(100, 100, 90));
        Assert.assertEquals(100, superPower.getDamageHitpoints(100, 100, 0));
    }

    @Test
    public void update() {
        Assert.assertEquals(SuperPower.SuperPowerState.INITIAL, superPower.getState());

        // launch missile
        superPower.update(0.0f);

        // expect launched
        Assert.assertEquals(SuperPower.SuperPowerState.LAUNCHED, superPower.getState());

        Entity lastCreatedEntity = entityRepository.getLastCreatedEntity();
        Assert.assertTrue(lastCreatedEntity instanceof Projectile); // the launched projectile

        Projectile launchedProjectile = (Projectile) lastCreatedEntity;

        // destroy it, this should cause the super power to be notified and put it in exploding state
        launchedProjectile.destroy();

        Assert.assertEquals(SuperPower.SuperPowerState.EXPLODING, superPower.getState());

        // creates ring of fire and smoke
        superPower.update(0.5f);

        // still exploding
        Assert.assertEquals(SuperPower.SuperPowerState.EXPLODING, superPower.getState());

        superPower.update(0.5f);

        Assert.assertEquals(SuperPower.SuperPowerState.EXPLODING, superPower.getState());

        // now we get past our duration time
        superPower.update(0.1f);
        Assert.assertEquals(SuperPower.SuperPowerState.DONE, superPower.getState());
    }
}