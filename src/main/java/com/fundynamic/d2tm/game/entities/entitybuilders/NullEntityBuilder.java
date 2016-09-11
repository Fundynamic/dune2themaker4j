package com.fundynamic.d2tm.game.entities.entitybuilders;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableEntity;

import java.util.Collections;
import java.util.List;

public class NullEntityBuilder implements EntityBuilder {

    @Override
    public List<BuildableEntity> getBuildList() {
        return Collections.emptyList();
    }

    @Override
    public boolean isBuildingEntity() {
        return false;
    }

    @Override
    public void buildEntity(BuildableEntity buildableEntity) {

    }

    @Override
    public boolean isAwaitingPlacement() {
        return false;
    }

    @Override
    public void entityIsDelivered(Entity entity) {

    }

    @Override
    public boolean isBuildingEntity(BuildableEntity buildableEntity) {
        return false;
    }

    @Override
    public void update(float deltaInSeconds) {

    }
}
