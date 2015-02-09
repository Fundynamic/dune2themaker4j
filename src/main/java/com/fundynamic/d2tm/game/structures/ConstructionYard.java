package com.fundynamic.d2tm.game.structures;

import com.fundynamic.d2tm.game.math.Random;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ConstructionYard {

    private final SpriteSheet spriteSheet;
    private final int width;
    private final int height;

    public ConstructionYard(Image imageOfConstructionYard, int width, int height) {
        this.width = width;
        this.height = height;
        this.spriteSheet = new SpriteSheet(imageOfConstructionYard, width, height);
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public Image getSprite() {
        return getSpriteSheet().getSprite(0, Random.getRandomBetween(0, 2));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
