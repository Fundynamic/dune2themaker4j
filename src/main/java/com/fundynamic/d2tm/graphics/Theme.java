package com.fundynamic.d2tm.graphics;


import com.fundynamic.d2tm.game.map.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Theme {

    private final SpriteSheet spriteSheet;

    public Theme(Image image, int tileWidth, int tileHeight) {
        this.spriteSheet = new SpriteSheet(image, tileWidth, tileHeight);
    }

    public Image getTileImage(int row, Map.TerrainFacing facing) {
        int column = facing.ordinal();
        return this.spriteSheet.getSprite(column, row);
    }

}
