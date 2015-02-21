package com.fundynamic.d2tm.graphics;

import com.fundynamic.d2tm.game.map.renderer.CellShroudRenderer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Shroud {

    private final SpriteSheet spriteSheet;

    public Shroud(Image image, int tileWidth, int tileHeight) {
        this.spriteSheet = new SpriteSheet(image, tileWidth, tileHeight);
    }

    public Image getShroudImage(CellShroudRenderer.ShroudFacing facing) {
        int column = facing.ordinal();
        return this.spriteSheet.getSprite(column, 0);
    }
}
