package com.fundynamic.d2tm.game.entities.superpowers;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Assert;
import org.junit.Test;

public class SuperPowerTest extends AbstractD2TMTest {

    @Test
    public void damage() {
        SuperPower superPower = new SuperPower(new Coordinate(0, 0), new EntityData(), player, entityRepository);

        // 0 damage, too far away
        Assert.assertEquals(0, superPower.getDamageHitpoints(100, 100, 100));
        Assert.assertEquals(10, superPower.getDamageHitpoints(100, 100, 90));
        Assert.assertEquals(100, superPower.getDamageHitpoints(100, 100, 0));
    }
}