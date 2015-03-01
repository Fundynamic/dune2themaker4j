package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Unit extends Entity implements Selectable {

    // Behaviors
    private final FadingSelection fadingSelection;

    // Implementation
    private final int facing;

    public Unit(Vector2D mapCoordinates, Image image, int width, int height, int sight) {
        this(mapCoordinates, new SpriteSheet(image, width, height), new FadingSelection(width, height), sight);
    }

    public Unit(Vector2D mapCoordinates, SpriteSheet spriteSheet, FadingSelection fadingSelection, int sight) {
        super(mapCoordinates, spriteSheet, sight);
        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
        this.fadingSelection = fadingSelection;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
        this.fadingSelection.render(graphics, x, y);
    }


    public Image getSprite() {
        return spriteSheet.getSprite(facing, 0);
    }

    @Override
    public String toString() {
        return "Unit{" +
                "facing=" + facing +
                '}';
    }

    @Override
    public void select() {
        fadingSelection.select();
    }

    @Override
    public void deselect() {
        fadingSelection.deselect();
    }

    @Override
    public boolean isSelected() {
        return fadingSelection.isSelected();
    }

    @Override
    public void update(float deltaInMs) {
        this.fadingSelection.update(deltaInMs);
    }
}
