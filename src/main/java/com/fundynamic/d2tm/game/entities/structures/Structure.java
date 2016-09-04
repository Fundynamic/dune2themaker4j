package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.sidebar.BuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

public class Structure extends Entity implements Selectable, Destructible, Focusable, EntityBuilder {

    // Behaviors
    private final FadingSelection fadingSelection;
    protected final HitPointBasedDestructibility hitPointBasedDestructibility;

    // Implementation
    private float animationTimer;
    private static final int ANIMATION_FRAME_COUNT = 2;
    private static final int ANIMATION_FRAMES_PER_SECOND = 5;

    private boolean hasSpawnedExplosions;

    public Structure(Coordinate coordinate, SpriteSheet spritesheet, Player player, EntityData entityData, EntityRepository entityRepository) {
        super(coordinate,
                spritesheet,
                entityData,
                player,
                entityRepository
        );

        this.fadingSelection = new FadingSelection(entityData.getWidth(), entityData.getHeight());
        this.hitPointBasedDestructibility = new HitPointBasedDestructibility(entityData.hitPoints, entityData.getWidth());
    }

    public Image getSprite() {
        int animationFrame = (int)animationTimer;
        return spritesheet.getSprite(0, animationFrame);
    }

    public void update(float deltaInSeconds) {
        if (this.isDestroyed()) {
            System.out.println("I (" + this.toString() + ") am dead, so I won't update anymore.");
            return;
        }
        // REVIEW: maybe base the animation on a global timer, so all animations are in-sync?
        float offset = deltaInSeconds * ANIMATION_FRAMES_PER_SECOND;
        animationTimer = (animationTimer + offset) % ANIMATION_FRAME_COUNT;

        this.fadingSelection.update(deltaInSeconds);

        if (hitPointBasedDestructibility.hasDied()) {
            hasSpawnedExplosions = true;
            for (Coordinate centeredPos : entityData.getAllCellsAsCenteredCoordinates(coordinate)) {
                entityRepository.explodeAt(centeredPos, entityData, player);
            }
        }
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.STRUCTURE;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
    }

    public void select() {
        fadingSelection.select();
    }

    public void deselect() {
        fadingSelection.deselect();
    }

    @Override
    public boolean isSelected() {
        return fadingSelection.isSelected();
    }

    @Override
    public void getsFocus() {
        fadingSelection.getsFocus();
    }

    @Override
    public void lostFocus() {
        fadingSelection.lostFocus();
    }

    @Override
    public String toString() {
        return "Structure{" +
                "fadingSelection=" + fadingSelection +
                ", animationTimer=" + animationTimer +
                ", hitPoints=" + hitPointBasedDestructibility +
                '}';
    }

    @Override
    public void takeDamage(int hitPoints, Entity origin) {
        hitPointBasedDestructibility.takeDamage(hitPoints);
        System.out.println("I took damage " + hitPointBasedDestructibility);
    }

    @Override
    public boolean isDestroyed() {
        return hasSpawnedExplosions && hitPointBasedDestructibility.hasDied();
    }

    @Override
    public int getHitPoints() {
        return hitPointBasedDestructibility.getHitPoints();
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        if (isSelected()) {
            renderQueue.putEntityGui(this.hitPointBasedDestructibility, this.getCoordinate());
            renderQueue.putEntityGui(this.fadingSelection, this.getCoordinate());
        } else {
            if (fadingSelection.hasFocus()) {
                renderQueue.putEntityGui(this.hitPointBasedDestructibility, this.getCoordinate());
            }
        }
    }

    public boolean hasFocus() {
        return fadingSelection.hasFocus();
    }

    private BuildableEntity buildingEntity = null;
    private List<BuildableEntity> buildableEntities;

    @Override
    public List<BuildableEntity> getBuildList() {
        // TODO: Move to EntityBuilder logic class thingy later
        if (buildableEntities == null) {
            buildableEntities = new ArrayList<>();
            // TODO: load somehow what kind of entities can be build, from entity Data I suppose!?

            // const yard can build refinery
            buildableEntities.add(new BuildableEntity(entityRepository.getEntityData(EntityType.STRUCTURE, EntitiesData.REFINERY)));
            buildableEntities.add(new BuildableEntity(entityRepository.getEntityData(EntityType.STRUCTURE, EntitiesData.WINDTRAP)));
        }
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
        return isBuildingEntity();
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

}
