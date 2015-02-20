package com.fundynamic.d2tm.game.structures;

import com.fundynamic.d2tm.game.map.MapEntity;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Structure extends MapEntity {

    private final int width;
    private final int height;
    private boolean selected;
    private float selectedIntensity;
    private boolean selectedDarkening;
    private float animationTimer;

    private static final int ANIMATION_FRAME_COUNT = 2;
    private static final int ANIMATION_FRAMES_PER_SECOND = 5;

    public Structure(Vector2D mapCoordinates, Image imageOfStructure, int width, int height) {
        this(mapCoordinates, new SpriteSheet(imageOfStructure, width, height), width, height);
    }

    public Structure(Vector2D mapCoordinates, SpriteSheet spriteSheet, int width, int height) {
        super(mapCoordinates, spriteSheet);
        this.width = width;
        this.height = height;
        this.selected = false;
        this.selectedIntensity = 1f;
        this.selectedDarkening = true;
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public Image getSprite() {
        // TODO: remove randomization here when we get a proper 'waving flag animation' done
        int animationFrame = (int)animationTimer;
        return getSpriteSheet().getSprite(0, animationFrame);
    }

    public void update(float delta) {
        // REVIEW: maybe base the animation on a global timer, so all animations are in-sync?
        float offset = delta * ANIMATION_FRAMES_PER_SECOND;
        animationTimer = (animationTimer + offset) % ANIMATION_FRAME_COUNT;

        if (isSelected()) {
            float intensityChange = .5f * delta;
            if (selectedDarkening) {
                selectedIntensity -= intensityChange;
            } else {
                selectedIntensity += intensityChange;
            }

            // fade back and forth
            if (selectedIntensity <= 0.0f) {
                selectedIntensity = 0.0f;
                selectedDarkening = false;
            } else if (selectedIntensity >= 1.0f) {
                selectedIntensity = 1.0f;
                selectedDarkening = true;
            }
        } else {
            selectedIntensity = 1f;
            selectedDarkening = true;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2D getMapCoordinates() {
        return mapCoordinates;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public float getSelectedIntensity() { return selectedIntensity; }
}
