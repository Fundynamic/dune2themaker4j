package com.fundynamic.d2tm.game.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class MapRenderer {

    private final int tileHeight;
    private final int tileWidth;

    public MapRenderer(int tileHeight, int tileWidth) {
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }

    public Image render(Map map) throws SlickException {
        Image image = makeImage(map.getWidth() * tileWidth, map.getHeight() * tileHeight);
        renderMap(image.getGraphics(), map);
        return image;
    }

    protected Image makeImage(int width, int height) throws SlickException {
        return new Image(width, height);
    }

    private void renderMap(Graphics graphics, Map map) throws SlickException {
        for (int x = 1; x <= map.getWidth(); x++) {
            for (int y = 1; y <= map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                graphics.drawImage(cell.getTileImage(), (x - 1) * tileWidth, (y - 1) * tileHeight);
            }
        }
    }
}