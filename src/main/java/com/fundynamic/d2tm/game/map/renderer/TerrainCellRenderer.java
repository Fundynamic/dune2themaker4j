package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TerrainCellRenderer implements Renderer {

    private final Map map;

    public TerrainCellRenderer(Map map) {
        this.map = map;
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int drawX, int drawY) {
        Cell cell = map.getCell(x, y);
        try {
            graphics.drawImage(cell.getTileImage(), drawX, drawY);
        } catch (SlickException e) {
            // draw nothing!?
            System.err.println("Unable to get cell tile image on " + x + "," + y + ". Exception: " + e);
        }
    }

}
