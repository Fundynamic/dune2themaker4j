package com.fundynamic.d2tm.graphics;


import com.fundynamic.d2tm.game.map.MapEditor;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Theme {

    private SpriteSheet spriteSheet;

    // needed for spritesheet
    private final Image image;
    private int tileSize;

    public Theme(Image image, int tileSize) {
        this.image = image;
        this.tileSize = tileSize;
    }

    public Image getFacingTile(int row, MapEditor.TerrainFacing facing) {
        if (spriteSheet == null) {
            this.spriteSheet = new SpriteSheet(image, tileSize, tileSize);
        }
        int column = facing.ordinal();
        return this.spriteSheet.getSprite(column, row);
    }

}
