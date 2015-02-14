package com.fundynamic.d2tm.game.structures;

import com.fundynamic.d2tm.game.math.Random;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Structure {

    private final Vector2D mapCoordinates;
    private final SpriteSheet spriteSheet;
    private final int width;
    private final int height;
    private boolean selected;

    public Structure(Vector2D mapCoordinates, Image imageOfStructure, int width, int height) {
        this(mapCoordinates, new SpriteSheet(imageOfStructure, width, height), width, height);
    }

    public Structure(Vector2D mapCoordinates, SpriteSheet spriteSheet, int width, int height) {
        this.width = width;
        this.height = height;
        this.spriteSheet = spriteSheet;
        this.mapCoordinates = mapCoordinates;
        this.selected = false;
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public Image getSprite() {
        // TODO: remove randomization here when we get a proper 'waving flag animation' done
        return getSpriteSheet().getSprite(0, Random.getRandomBetween(0, 2));
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
}
