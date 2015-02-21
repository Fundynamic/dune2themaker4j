package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.behaviors.SelectableImpl;
import com.fundynamic.d2tm.game.map.MapEntity;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Structure extends MapEntity implements Selectable {

    // Behaviors
    private SelectableImpl selectableImpl;

    // Implementation
    private float animationTimer;
    private static final int ANIMATION_FRAME_COUNT = 2;
    private static final int ANIMATION_FRAMES_PER_SECOND = 5;

    public Structure(Vector2D mapCoordinates, Image imageOfStructure, int width, int height) {
        this(mapCoordinates, new SpriteSheet(imageOfStructure, width, height), width, height);
    }

    public Structure(Vector2D mapCoordinates, SpriteSheet spriteSheet, int width, int height) {
        super(mapCoordinates, spriteSheet);
        this.selectableImpl = new SelectableImpl(width, height);
    }

    public Image getSprite() {
        // TODO: remove randomization here when we get a proper 'waving flag animation' done
        int animationFrame = (int)animationTimer;
        return spriteSheet.getSprite(0, animationFrame);
    }

    public void update(float delta) {
        // REVIEW: maybe base the animation on a global timer, so all animations are in-sync?
        float offset = delta * ANIMATION_FRAMES_PER_SECOND;
        animationTimer = (animationTimer + offset) % ANIMATION_FRAME_COUNT;

        this.selectableImpl.update(delta);
    }

    public Vector2D getMapCoordinates() {
        return mapCoordinates;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
        selectableImpl.render(graphics, x, y);
    }

    public void select() {
        selectableImpl.select();
    }

    public void deselect() {
        selectableImpl.deselect();
    }

    @Override
    public boolean isSelected() {
        return selectableImpl.isSelected();
    }

    @Override
    public String toString() {
        return "Structure{" +
                "animationTimer=" + animationTimer +
                ", selectable=" + selectableImpl +
                '}';
    }
}
