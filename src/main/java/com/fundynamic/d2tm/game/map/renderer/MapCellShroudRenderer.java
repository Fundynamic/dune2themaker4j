package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapCell;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.ShroudFacing;
import org.newdawn.slick.Graphics;

public class MapCellShroudRenderer implements Renderer<MapCell> {

    private final Shroud shroud;

    public MapCellShroudRenderer(Map map) {
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

        return getFacing(
                mapCell.getCellAbove().isShrouded(),
                mapCell.getCellRight().isShrouded(),
                mapCell.getCellBeneath().isShrouded(),
                mapCell.getCellLeft().isShrouded());
    }

    public static ShroudFacing getFacing(boolean isTopShrouded, boolean isRightShrouded, boolean isBottomShrouded, boolean isLeftShrouded) {
        if (isTopShrouded && isLeftShrouded && !isRightShrouded && !isBottomShrouded) return ShroudFacing.TOP_LEFT;
        if (isTopShrouded && !isLeftShrouded && !isRightShrouded && !isBottomShrouded) return ShroudFacing.TOP;
        if (isTopShrouded && !isLeftShrouded && isRightShrouded && !isBottomShrouded) return ShroudFacing.TOP_RIGHT;
        if (!isTopShrouded && !isLeftShrouded && isRightShrouded && !isBottomShrouded) return ShroudFacing.RIGHT;
        if (!isTopShrouded && !isLeftShrouded && isRightShrouded && isBottomShrouded) return ShroudFacing.RIGHT_BOTTOM;
        if (!isTopShrouded && !isLeftShrouded && !isRightShrouded && isBottomShrouded) return ShroudFacing.BOTTOM;
        if (!isTopShrouded && isLeftShrouded && !isRightShrouded && isBottomShrouded) return ShroudFacing.BOTTOM_LEFT;
        if (!isTopShrouded && isLeftShrouded && !isRightShrouded && !isBottomShrouded) return ShroudFacing.LEFT;
        if (isTopShrouded && isLeftShrouded && !isRightShrouded && isBottomShrouded) return ShroudFacing.TOP_BOTTOM_LEFT;
        if (isTopShrouded && isLeftShrouded && isRightShrouded && !isBottomShrouded) return ShroudFacing.TOP_RIGHT_LEFT;
        if (isTopShrouded && !isLeftShrouded && isRightShrouded && isBottomShrouded) return ShroudFacing.TOP_RIGHT_BOTTOM;
        if (!isTopShrouded && isLeftShrouded && isRightShrouded && isBottomShrouded) return ShroudFacing.RIGHT_BOTTOM_LEFT;
        if (isTopShrouded && !isLeftShrouded && !isRightShrouded && isBottomShrouded) return ShroudFacing.TOP_BOTTOM;
        if (!isTopShrouded && isLeftShrouded && isRightShrouded && !isBottomShrouded) return ShroudFacing.RIGHT_LEFT;
        if (isTopShrouded && isLeftShrouded && isRightShrouded && isBottomShrouded) return ShroudFacing.MIDDLE;
        return null;
    }

}
