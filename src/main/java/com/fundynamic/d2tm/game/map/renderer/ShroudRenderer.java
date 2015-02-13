package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.ShroudFacing;
import org.newdawn.slick.Graphics;

public class ShroudRenderer implements CellRenderer {

    private final Shroud shroud;

    public ShroudRenderer(Map map) {
        this.shroud = map.getShroud();
    }

    @Override
    public void draw(Graphics graphics, MapCell mapCell, int drawX, int drawY) {
        ShroudFacing shroudFacing = determineShroudFacing(mapCell);
        if (shroudFacing != null) {
            graphics.drawImage(shroud.getShroudImage(shroudFacing), drawX, drawY);
        }
    }

    protected ShroudFacing determineShroudFacing(MapCell mapCell) {
        if (mapCell.isShrouded()) {
            return ShroudFacing.FULL;
        }

        return ShroudFacingDeterminer.getFacing(
                mapCell.getCellAbove().isShrouded(),
                mapCell.getCellLeft().isShrouded(),
                mapCell.getCellBeneath().isShrouded(),
                mapCell.getCellRight().isShrouded());
    }
}
