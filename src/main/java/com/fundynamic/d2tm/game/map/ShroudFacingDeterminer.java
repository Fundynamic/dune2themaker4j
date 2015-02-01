package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.graphics.ShroudFacing;

public class ShroudFacingDeterminer {

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
        return null;
    }
}
