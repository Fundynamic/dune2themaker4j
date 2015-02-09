package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TerrainCellRenderer implements CellRenderer {

    private final Map map;

    public TerrainCellRenderer(Map map) {
        this.map = map;
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int drawX, int drawY) {
        Cell cell = map.getCell(x, y);
        try {
            graphics.drawImage(cell.getTileImage(), drawX, drawY);
            if (cell.isHoveredOver()) {
                graphics.setLineWidth(1.1f);
                graphics.drawRect(drawX, drawY, 31, 31);
            }
        } catch (SlickException e) {
            // draw nothing!?
            System.err.println("Unable to get cell tile image on " + x + "," + y + ". Exception: " + e);
        }
    }

}
