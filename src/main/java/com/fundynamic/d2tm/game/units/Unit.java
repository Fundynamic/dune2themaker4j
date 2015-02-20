package com.fundynamic.d2tm.game.units;

import com.fundynamic.d2tm.game.math.Random;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Unit {
    private final Vector2D mapCoordinates;
    private final SpriteSheet spriteSheet;
    private final int width;
    private final int height;
    private final int facing;

    public Unit(Vector2D mapCoordinates, Image imageOfStructure, int width, int height) {
        this(mapCoordinates, new SpriteSheet(imageOfStructure, width, height), width, height);
    }

    public Unit(Vector2D mapCoordinates, SpriteSheet spriteSheet, int width, int height) {
        this.mapCoordinates = mapCoordinates;
        this.spriteSheet = spriteSheet;
        this.width = width;
        this.height = height;
        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
    }

    public Vector2D getMapCoordinates() {
        return mapCoordinates;
    }

    public Image getSprite() {
        return spriteSheet.getSprite(facing, 0);
    }
}
