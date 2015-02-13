package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.MapCell;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class MapCellTerrainRenderer implements Renderer<MapCell> {

    @Override
    public void draw(Graphics graphics, MapCell mapCell, int drawX, int drawY) {
        try {
            graphics.drawImage(mapCell.getTileImage(), drawX, drawY);
        } catch (SlickException e) {
            // draw nothing!?
            System.err.println("Unable to get cell tile image on " + mapCell.getX() + "," + mapCell.getY() + ". Exception: " + e);
        }
    }

}
