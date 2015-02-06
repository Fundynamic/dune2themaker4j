package com.fundynamic.d2tm.game.structures;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ConstructionYard {

    private final SpriteSheet spriteSheet;

    public ConstructionYard(Image imageOfConstructionYard) {
        this.spriteSheet = new SpriteSheet(imageOfConstructionYard, 64, 64);
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }
}
