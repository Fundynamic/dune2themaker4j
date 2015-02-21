package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.game.map.Cell;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CellTerrainRenderer implements Renderer<Cell> {

    @Override
    public void draw(Graphics graphics, Cell mapCell, int drawX, int drawY) {
        try {
            graphics.drawImage(mapCell.getTileImage(), drawX, drawY);
        } catch (SlickException e) {
            // draw nothing!?
            System.err.println("Unable to get cell tile image on " + mapCell.getX() + "," + mapCell.getY() + ". Exception: " + e);
        }
    }

}
