package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.ShroudFacing;
import org.newdawn.slick.Graphics;

public class ShroudRenderer implements CellRenderer {

    private final Map map;
    private final Shroud shroud;

    public ShroudRenderer(Map map) {
        this.map = map;
        this.shroud = map.getShroud();
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int drawX, int drawY) {
        ShroudFacing shroudFacing = determineShroudFacing(map, x, y);
        if (shroudFacing != null) {
            graphics.drawImage(shroud.getShroudImage(shroudFacing), drawX, drawY);
        }
    }

    protected ShroudFacing determineShroudFacing(Map map, int x, int y) {
        if (map.getCell(x,y).isShrouded()) {
            return ShroudFacing.FULL;
        }

        return ShroudFacingDeterminer.getFacing(
                isShrouded(map, x, y - 1),
                isShrouded(map, x + 1, y),
                isShrouded(map, x, y + 1),
                isShrouded(map, x - 1, y)
        );
    }

    private boolean isShrouded(Map map, int x, int y) {
        if (x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight()) {
            return true;
        }
        return map.getCell(x, y).isShrouded();
    }
}
