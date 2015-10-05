package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibility;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

public class Structure extends Entity implements Selectable, Destructible {

    public static int TILE_SIZE = 32; // TODO: remove HACK HACK

    // Behaviors
    private final FadingSelection fadingSelection;
    protected final HitPointBasedDestructibility hitPointBasedDestructibility;

    // Implementation
    private final int widthInCells, heightInCells;
    private float animationTimer;
    private static final int ANIMATION_FRAME_COUNT = 2;
    private static final int ANIMATION_FRAMES_PER_SECOND = 5;

    private boolean hasSpawnedExplosions;

    public Structure(Vector2D absoluteMapCoordinates, Image imageOfStructure, Player player, EntityData entityData, EntityRepository entityRepository) {
        this(absoluteMapCoordinates, new SpriteSheet(imageOfStructure, entityData.width, entityData.height), player, entityData, entityRepository);
    }

    public Structure(Vector2D absoluteMapCoordinates, SpriteSheet spriteSheet, Player player, EntityData entityData, EntityRepository entityRepository) {
        super(absoluteMapCoordinates, spriteSheet, entityData.sight, player, entityRepository);
        this.entityData = entityData;
        this.fadingSelection = new FadingSelection(entityData.width, entityData.height);
        this.hitPointBasedDestructibility = new HitPointBasedDestructibility(entityData.hitPoints);
        widthInCells = (int) Math.ceil(entityData.width / TILE_SIZE);
        heightInCells = (int) Math.ceil(entityData.height / TILE_SIZE);
    }

    public Image getSprite() {
        int animationFrame = (int)animationTimer;
        return spriteSheet.getSprite(0, animationFrame);
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
            for (Vector2D pos : getAllCellsAsVectors()) {
                entityRepository.placeOnMap(pos, EntityType.PARTICLE, entityData.explosionId, player);
            }
        }
    }

    public Vector2D getAbsoluteMapCoordinates() {
        return absoluteMapCoordinates;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.STRUCTURE;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
        fadingSelection.render(graphics, x, y);
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

    public int getHeightInCells() {
        return heightInCells;
    }

    public int getWidthInCells() {
        return widthInCells;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "fadingSelection=" + fadingSelection +
                ", widthInCells=" + widthInCells +
                ", heightInCells=" + heightInCells +
                ", animationTimer=" + animationTimer +
                ", hitPoints=" + hitPointBasedDestructibility +
                '}';
    }

    @Override
    public void takeDamage(int hitPoints) {
        hitPointBasedDestructibility.takeDamage(hitPoints);
    }

    @Override
    public boolean isDestroyed() {
        return hasSpawnedExplosions && hitPointBasedDestructibility.hasDied();
    }

    public List<Vector2D> getAllCellsAsVectors() {
        List<Vector2D> result = new ArrayList<>(widthInCells * heightInCells);
        for (int x = 0; x < widthInCells; x++) {
            for (int y = 0; y < heightInCells; y++) {
                int vecX = getX() + x * TILE_SIZE;
                int vecY = getY() + y * TILE_SIZE;
//                // center
//                vecX += 16;
//                vecY += 16;
                result.add(Vector2D.create(vecX, vecY));
            }
        }
        return result;
    }
}
