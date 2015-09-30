package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibility;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Structure extends Entity implements Selectable, Destructible {

    public static int TILE_SIZE = 32; // TODO: remove HACK HACK

    // Behaviors
    private final FadingSelection fadingSelection;
    private final HitPointBasedDestructibility hitPointBasedDestructibility;

    // Implementation
    private final int widthInCells, heightInCells;
    private float animationTimer;
    private static final int ANIMATION_FRAME_COUNT = 2;
    private static final int ANIMATION_FRAMES_PER_SECOND = 5;

    public Structure(Vector2D absoluteMapCoordinates, Image imageOfStructure, Player player, EntityData entityData) {
        this(absoluteMapCoordinates, new SpriteSheet(imageOfStructure, entityData.width, entityData.height), player, entityData);
    }

    public Structure(Vector2D absoluteMapCoordinates, SpriteSheet spriteSheet, Player player, EntityData entityData) {
        super(absoluteMapCoordinates, spriteSheet, entityData.sight, player);
        this.fadingSelection = new FadingSelection(entityData.width, entityData.height);
        this.hitPointBasedDestructibility = new HitPointBasedDestructibility(entityData.hitPoints);
        widthInCells = (int) Math.ceil(entityData.width / TILE_SIZE);
        heightInCells = (int) Math.ceil(entityData.height / TILE_SIZE);
    }

    public Image getSprite() {
        int animationFrame = (int)animationTimer;
        return spriteSheet.getSprite(0, animationFrame);
    }

    public void update(float delta) {
        if (this.isDestroyed()) {
            System.out.println("I (" + this.toString() + ") am dead, so I won't update anymore.");
            return;
        }
        // REVIEW: maybe base the animation on a global timer, so all animations are in-sync?
        float offset = delta * ANIMATION_FRAMES_PER_SECOND;
        animationTimer = (animationTimer + offset) % ANIMATION_FRAME_COUNT;

        this.fadingSelection.update(delta);
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
        return hitPointBasedDestructibility.isDestroyed();
    }
}
