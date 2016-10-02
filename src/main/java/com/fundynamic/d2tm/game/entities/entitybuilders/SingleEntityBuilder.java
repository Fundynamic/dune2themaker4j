package com.fundynamic.d2tm.game.entities.entitybuilders;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;

import java.util.ArrayList;
import java.util.List;

/**
 * A primitive entity builder that can build one thing at a time
 */
public class SingleEntityBuilder implements EntityBuilder {

    private Entity constructingForEntity;
    private AbstractBuildableEntity buildingEntity = null;

    private List<AbstractBuildableEntity> buildableEntities = new ArrayList<>();

    public SingleEntityBuilder(List<EntityData> entityDatasToBuild, Entity constructingForEntity) {
        this.constructingForEntity = constructingForEntity;
        for (EntityData entityDataToBuild : entityDatasToBuild) {
            // TODO: make more flexible!!
            if (entityDataToBuild.isTypeStructure()) {
                buildableEntities.add(new PlacementBuildableEntity(entityDataToBuild, constructingForEntity));
            } else {
                buildableEntities.add(new SpawningBuildableEntity(entityDataToBuild));
            }
        }
    }

    @Override
    public List<AbstractBuildableEntity> getBuildList() {
        return buildableEntities;
    }

    @Override
    public boolean hasBuildingEntity() {
        return buildingEntity != null;
    }

    @Override
    public AbstractBuildableEntity getBuildingEntity() {
        return buildingEntity;
    }

    @Override
    public void buildEntity(AbstractBuildableEntity abstractBuildableEntity) {
        this.buildingEntity = abstractBuildableEntity;
        // disable all buildable entities
        for (AbstractBuildableEntity be : buildableEntities) {
            be.disable();
        }
        // except this one, build it!
        this.buildingEntity.startBuilding();
    }

    @Override
    public boolean isAwaitingPlacement() {
        return hasBuildingEntity() && buildingEntity.awaitsPlacement();
    }

    @Override
    public boolean isAwaitingSpawning() {
        return hasBuildingEntity() && buildingEntity.awaitsSpawning();
    }

    @Override
    public void entityIsDelivered(Entity entity) {
        buildingEntity = null;
        // enable all buildable entities again
        for (AbstractBuildableEntity be : buildableEntities) {
            be.enable();
        }
    }

    @Override
    public boolean hasBuildingEntity(AbstractBuildableEntity placementBuildableEntity) {
        return buildingEntity.equals(placementBuildableEntity);
    }

    @Override
    public void update(float deltaInSeconds) {
        buildingEntity.update(deltaInSeconds);
    }
}
