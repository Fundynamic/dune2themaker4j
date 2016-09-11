package com.fundynamic.d2tm.game.entities.entitybuilders;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.EntitiesData;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * A primitive entity builder
 */
public class EntityBuilderImpl implements EntityBuilder {

    private BuildableEntity buildingEntity = null;

    private List<BuildableEntity> buildableEntities = new ArrayList<>();

    public EntityBuilderImpl(List<EntityData> entityDatasToBuild) {
        for (EntityData entityDataToBuild : entityDatasToBuild ) {
            buildableEntities.add(new BuildableEntity(entityDataToBuild));
        }
    }

    @Override
    public List<BuildableEntity> getBuildList() {
        return buildableEntities;
    }

    @Override
    public boolean isBuildingEntity() {
        return buildingEntity != null;
    }

    @Override
    public void buildEntity(BuildableEntity buildableEntity) {
        this.buildingEntity = buildableEntity;
        // disable all buildable entities
        for (BuildableEntity be : buildableEntities) {
            be.disable();
        }
        // except this one, build it!
        this.buildingEntity.startBuilding();
    }

    @Override
    public boolean isAwaitingPlacement() {
        return isBuildingEntity() && buildingEntity.awaitsPlacement();
    }

    @Override
    public void entityIsDelivered(Entity entity) {
        buildingEntity = null;
        // enable all buildable entities again
        for (BuildableEntity be : buildableEntities) {
            be.enable();
        }
    }

    @Override
    public boolean isBuildingEntity(BuildableEntity buildableEntity) {
        return buildingEntity.equals(buildableEntity);
    }

    @Override
    public void update(float deltaInSeconds) {
        buildingEntity.update(deltaInSeconds);
    }
}
