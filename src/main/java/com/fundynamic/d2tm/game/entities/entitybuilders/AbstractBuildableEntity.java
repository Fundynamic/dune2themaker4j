package com.fundynamic.d2tm.game.entities.entitybuilders;


import com.fundynamic.d2tm.game.behaviors.Updateable;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableState;
import org.newdawn.slick.Image;

/**
 * <h1>General</h1>
 * An entity that can be built by a {@link com.fundynamic.d2tm.game.behaviors.EntityBuilder}. It contains little
 * state giving hints if it can be built, its progress and so forth.
 */
public abstract class AbstractBuildableEntity implements Updateable {

    /**
     * The thing it is / can be building
     */
    private final EntityData entityData;

    private final Player player;

    private float secondsToBuildInMs;

    public BuildableState buildableState = BuildableState.SELECTABLE;

    public AbstractBuildableEntity(EntityData entityData, Player player) {
        this.entityData = entityData;
        this.player = player;
        secondsToBuildInMs = entityData.buildTimeInSeconds;
    }

    public EntityData getEntityData() {
        return entityData;
    }

    public boolean hasBuildIcon() {
        return entityData.buildIcon != null;
    }

    public Image getBuildIcon() {
        return entityData.buildIcon;
    }

    public boolean canBuildEntity() {
        return this.buildableState == BuildableState.SELECTABLE;
    }

    public void startBuilding() {
        player.spend(entityData.buildCost);
        resetBuildTime();
        buildableState = BuildableState.BUILDING;
    }

    public void disable() {
        buildableState = BuildableState.DISABLED;
    }

    public void enable() {
        resetBuildTime();
        buildableState = getSelectableStateBasedOnPrice();
    }

    public void resetBuildTime() {
        secondsToBuildInMs = entityData.buildTimeInSeconds;
    }

    public BuildableState getBuildableState() {
        return buildableState;
    }

    public boolean isDoneBuilding() {
        return secondsToBuildInMs < 0f;
    }

    public boolean isBuilding() {
        return buildableState == BuildableState.BUILDING;
    }

    @Override
    public void update(float deltaInSeconds) {
        if (isSelectable()) {
            // keep evaluating price
            buildableState = getSelectableStateBasedOnPrice();
        }

        if (isBuilding()) {
            secondsToBuildInMs -= deltaInSeconds;
            if (isDoneBuilding()) {
                buildableState = BuildableState.BUILDING_FINISHED_SPAWNABLE;
            }
        }
    }

    public boolean isSelectable() {
        return buildableState == BuildableState.SELECTABLE || buildableState == BuildableState.SELECTABLE_TOO_EXPENSIVE;
    }

    private BuildableState getSelectableStateBasedOnPrice() {
        if (player.canBuy(entityData.buildCost)) {
            return BuildableState.SELECTABLE;
        } else {
            return BuildableState.SELECTABLE_TOO_EXPENSIVE;
        }
    }

    public boolean awaitsPlacement() {
        return buildableState == BuildableState.BUILDING_FINISHED_AWAITS_PLACEMENT;
    }

    public boolean awaitsSpawning() {
        return buildableState == BuildableState.BUILDING_FINISHED_SPAWNABLE;
    }

    /**
     * Returns a number between 0.0 (started) and 1.0 (completed).
     *
     * @return
     */
    public float getProgress() {
        return (entityData.buildTimeInSeconds - secondsToBuildInMs) / entityData.buildTimeInSeconds;
    }


    public int getBuildCost() {
        return entityData.buildCost;
    }
}
