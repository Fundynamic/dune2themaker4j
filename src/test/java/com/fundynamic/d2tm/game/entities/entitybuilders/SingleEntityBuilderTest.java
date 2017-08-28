package com.fundynamic.d2tm.game.entities.entitybuilders;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Faction;
import com.fundynamic.d2tm.game.entities.NullEntity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.types.EntityData;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SingleEntityBuilderTest {

    @Test
    public void doesNotBuildWhenEntityCannotBeBuilt() {
        List<EntityData> entitiesDataToBuild = new ArrayList<>();
        EntityData entityData = new EntityData();
        Entity constructingEntity = new NullEntity();
        Player player = new Player("Human player", Faction.BLUE);

        SingleEntityBuilder singleEntityBuilder = new SingleEntityBuilder(entitiesDataToBuild, constructingEntity, player);

        // build entity
        Assert.assertFalse(singleEntityBuilder.isBuildingAnEntity());

        singleEntityBuilder.buildEntity(new AbstractBuildableEntity(entityData, player) {
            @Override
            public boolean canBuildEntity() {
                return false;
            }
        });

        // should still not build entity
        Assert.assertFalse(singleEntityBuilder.isBuildingAnEntity());
    }

}