package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.map.MapEntity;
import com.fundynamic.d2tm.game.math.Random;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Unit extends MapEntity {
    private final int facing;

    public Unit(Vector2D mapCoordinates, Image image, int width, int height) {
        this(mapCoordinates, new SpriteSheet(image, width, height));
    }

    public Unit(Vector2D mapCoordinates, SpriteSheet spriteSheet) {
        super(mapCoordinates, spriteSheet);
        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
    }

    @Override
    public void render(Graphics graphics, int drawX, int drawY) {
        Image sprite = getSprite();
        graphics.drawImage(sprite, drawX, drawY);
    }

    @Override
    public void select() {

    }

    @Override
    public void deselect() {

    }

    @Override
    public boolean isSelected() {
        return false;
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
}
