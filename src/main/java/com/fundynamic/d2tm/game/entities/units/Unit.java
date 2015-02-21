package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.behaviors.SelectableImpl;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Unit extends Entity implements Selectable {
    private final SelectableImpl selectable;
    private final int facing;

    public Unit(Vector2D mapCoordinates, Image image, int width, int height) {
        this(mapCoordinates, new SpriteSheet(image, width, height), new SelectableImpl(width, height));
    }

    public Unit(Vector2D mapCoordinates, SpriteSheet spriteSheet, SelectableImpl selectable) {
        super(mapCoordinates, spriteSheet);
        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
        this.selectable = selectable;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
        this.selectable.render(graphics, x, y);
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
        selectable.select();
    }

    @Override
    public void deselect() {
        selectable.deselect();
    }

    @Override
    public boolean isSelected() {
        return selectable.isSelected();
    }

    @Override
    public void update(float deltaInMs) {
        this.selectable.update(deltaInMs);
    }
}
