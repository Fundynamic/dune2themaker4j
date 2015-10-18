package com.fundynamic.d2tm.graphics;

import com.fundynamic.d2tm.game.rendering.CellShroudRenderer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Shroud {

    private SpriteSheet spriteSheet;

    // needed for spriteSheet
    private final Image image;
    private int tileSize;

    public Shroud(Image image, int tileSize) {
        this.image = image;
        this.tileSize = tileSize;
    }

    public Image getShroudImage(CellShroudRenderer.ShroudFacing facing) {
        if (spriteSheet == null) {
            this.spriteSheet = createSpriteSheetFromImage();
        }
        int column = facing.ordinal();
        return this.spriteSheet.getSprite(column, 0);
    }

    public SpriteSheet createSpriteSheetFromImage() {
        return new SpriteSheet(image, tileSize, tileSize);
    }
}
