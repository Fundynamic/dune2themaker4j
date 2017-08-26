package com.fundynamic.d2tm.game.entities.entitybuilders;


import com.fundynamic.d2tm.game.behaviors.EntityBuilder;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.types.EntityData;

import java.util.ArrayList;
import java.util.List;

/**
 * A primitive entity builder that can build one thing at a time
 */
public class SingleEntityBuilder implements EntityBuilder {

    private AbstractBuildableEntity buildingEntity = null;

    private List<AbstractBuildableEntity> buildableEntities = new ArrayList<>();

    public SingleEntityBuilder(List<EntityData> entityDatasToBuild, Entity constructingForEntity, Player player) {
        for (EntityData entityDataToBuild : entityDatasToBuild) {
            // TODO: make more flexible!!
            if (entityDataToBuild.isTypeStructure() || entityDataToBuild.isTypeSuperPower()) {
                buildableEntities.add(new PlacementBuildableEntity(entityDataToBuild, player, constructingForEntity));
            } else {
                buildableEntities.add(new SpawningBuildableEntity(entityDataToBuild, player));
            }
        }
    }

    @Override
    public List<AbstractBuildableEntity> getBuildList() {
        return buildableEntities;
    }

    @Override
    public boolean isBuildingAnEntity() {
        return buildingEntity != null;
    }

    @Override
    public AbstractBuildableEntity getBuildingEntity() {
        return buildingEntity;
    }

    @Override
    public void buildEntity(AbstractBuildableEntity abstractBuildableEntity) {
        if (!abstractBuildableEntity.canBuildEntity()) {
            // Unable to comply: this is not an entity that can be built now!
            return;
        }

        this.buildingEntity = abstractBuildableEntity;

        // disable all buildable entities
        for (AbstractBuildableEntity be : buildableEntities) {
            be.disable();
        }

        // start building this one
        this.buildingEntity.startBuilding();
    }

    @Override
    public boolean isAwaitingPlacement(AbstractBuildableEntity placementBuildableEntity) {
        return isBuildingAnEntity() && buildingEntity.isSameConstructedEntity(placementBuildableEntity) && buildingEntity.awaitsPlacement();
    }

    @Override
    public boolean isAwaitingPlacement(EntityData entityData) {
        return isBuildingAnEntity() && buildingEntity.getEntityData().equals(entityData) && buildingEntity.awaitsPlacement();
    }

    @Override
    public boolean isAwaitingSpawning() {
        return isBuildingAnEntity() && buildingEntity.awaitsSpawning();
    }

    @Override
    public void entityIsDelivered(Entity entity) {
        if (buildingEntity != null) {
            buildingEntity.enable();
            buildingEntity = null;
        }
        // enable all buildable entities again
        for (AbstractBuildableEntity be : buildableEntities) {
            be.enable();
        }
    }

    @Override
    public boolean isBuildingAnEntity(AbstractBuildableEntity placementBuildableEntity) {
        return buildingEntity.equals(placementBuildableEntity);
    }

    @Override
    public void update(float deltaInSeconds) {
        for (AbstractBuildableEntity be : buildableEntities) {
            be.update(deltaInSeconds);
        }
    }
}
