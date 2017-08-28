package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.TestableEntity;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.SpriteSheet;

import static org.mockito.Mockito.mock;

public class IsWithinAnotherEntityTest extends AbstractD2TMTest{

    @Test
    public void withinOther() {
        EntityData structure = new EntityData();
        structure.type = EntityType.STRUCTURE;
        structure.setWidth(64);
        structure.setHeight(64);

        EntityData unit = new EntityData();
        unit.type = EntityType.UNIT;
        unit.setWidth(32);
        unit.setHeight(32);

        Coordinate coordinate = Coordinate.create(32, 32);
        TestableEntity refinery = new TestableEntity(coordinate, mock(SpriteSheet.class), structure, player, entityRepository).
                setName("Refinery");

        TestableEntity harvester = new TestableEntity(coordinate, mock(SpriteSheet.class), unit, player, entityRepository).
                setName("Harvester");

        harvester.enterOtherEntity(refinery);

        Assert.assertFalse(IsWithinAnotherEntity.instance.test(refinery)); // refinery is NOT inside other entity
        Assert.assertTrue(IsWithinAnotherEntity.instance.test(harvester)); // harvester IS in another entity

        harvester.leaveOtherEntity();

        Assert.assertFalse(IsWithinAnotherEntity.instance.test(refinery));
        Assert.assertFalse(IsWithinAnotherEntity.instance.test(harvester));
    }
}