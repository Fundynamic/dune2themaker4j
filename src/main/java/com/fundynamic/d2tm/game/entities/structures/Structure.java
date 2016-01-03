package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibility;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.rendering.RenderQueue;
import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Structure extends Entity implements Selectable, Destructible {

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
}
