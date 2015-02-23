package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Structure extends Entity implements Selectable {

    public static int TILE_SIZE = 32; // TODO: remove HACK HACK

    // Behaviors
    private FadingSelection fadingSelection;

    // Implementation
    private final int widthInCells, heightInCells;
    private float animationTimer;
    private static final int ANIMATION_FRAME_COUNT = 2;
    private static final int ANIMATION_FRAMES_PER_SECOND = 5;

    public Structure(Vector2D mapCoordinates, Image imageOfStructure, int widthInPixels, int heightInPixels) {
        this(mapCoordinates, new SpriteSheet(imageOfStructure, widthInPixels, heightInPixels), widthInPixels, heightInPixels);
    }

    public Structure(Vector2D mapCoordinates, SpriteSheet spriteSheet, int widthInPixels, int heightInPixels) {
        super(mapCoordinates, spriteSheet);
        this.fadingSelection = new FadingSelection(widthInPixels, heightInPixels);
        widthInCells = (int) Math.ceil(widthInPixels / TILE_SIZE);
        heightInCells = (int) Math.ceil(heightInPixels / TILE_SIZE);
    }

    public Image getSprite() {
        int animationFrame = (int)animationTimer;
        return spriteSheet.getSprite(0, animationFrame);
    }

    public void update(float delta) {
        // REVIEW: maybe base the animation on a global timer, so all animations are in-sync?
        float offset = delta * ANIMATION_FRAMES_PER_SECOND;
        animationTimer = (animationTimer + offset) % ANIMATION_FRAME_COUNT;

        this.fadingSelection.update(delta);
    }

    public Vector2D getMapCoordinates() {
        return mapCoordinates;
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
                '}';
    }
}
