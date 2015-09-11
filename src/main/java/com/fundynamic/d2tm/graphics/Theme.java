package com.fundynamic.d2tm.graphics;


import com.fundynamic.d2tm.game.map.MapEditor;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Theme {

    private SpriteSheet spriteSheet;

    // needed for spriteSheet
    private final Image image;
    private int tileWidth;
    private int tileHeight;

    public Theme() {
        this.image = null;
    }

    public Theme(Image image, int tileWidth, int tileHeight) {
        this.image = image;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }

    public Image getFacingTile(int row, MapEditor.TerrainFacing facing) {
        if (spriteSheet == null) {
            this.spriteSheet = new SpriteSheet(image, tileWidth, tileHeight);
        }
        int column = facing.ordinal();
        return this.spriteSheet.getSprite(column, row);
    }

}
