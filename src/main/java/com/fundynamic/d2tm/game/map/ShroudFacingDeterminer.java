package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.graphics.ShroudFacing;

public class ShroudFacingDeterminer {

    private static final int BIT_MASK_NONE = 0;
    private static final int BIT_MASK_TOP = 0x0008;
    private static final int BIT_MASK_RIGHT = 0x0004;
    private static final int BIT_MASK_BOTTOM = 0x0002;
    private static final int BIT_MASK_LEFT = 0x0001;

    private static final ShroudFacing[] FACINGS = {
        null,                              // 0x0000
        ShroudFacing.LEFT,                 // 0x0001
        ShroudFacing.BOTTOM,               // 0x0010
        ShroudFacing.BOTTOM_LEFT,          // 0x0011
        ShroudFacing.RIGHT,                // 0x0100
        null,                              // 0x0101
        ShroudFacing.RIGHT_BOTTOM,         // 0x0110
        ShroudFacing.RIGHT_BOTTOM_LEFT,    // 0x0111
        ShroudFacing.TOP,                  // 0x1000
        ShroudFacing.TOP_LEFT,             // 0x1001
        null,                              // 0x1010
        ShroudFacing.TOP_BOTTOM_LEFT,      // 0x1011
        ShroudFacing.TOP_RIGHT,            // 0x1100
        ShroudFacing.TOP_RIGHT_LEFT,       // 0x1101
        ShroudFacing.TOP_RIGHT_BOTTOM,     // 0x1110
        null,                              // 0x1111
    };

    private boolean isTopShrouded;
    private boolean isRightShrouded;
    private boolean isBottomShrouded;
    private boolean isLeftShrouded;

    public void setTopShrouded(boolean topSame) {
        this.isTopShrouded = topSame;
    }

    public void setRightShrouded(boolean rightSame) {
        isRightShrouded = rightSame;
    }

    public void setBottomShrouded(boolean bottomSame) {
        this.isBottomShrouded = bottomSame;
    }

    public void setLeftShrouded(boolean leftSame) {
        isLeftShrouded = leftSame;
    }

    public ShroudFacing getFacing() {
        int value = 0;
        value |= isTopShrouded    ? BIT_MASK_TOP    : BIT_MASK_NONE;
        value |= isRightShrouded  ? BIT_MASK_RIGHT  : BIT_MASK_NONE;
        value |= isBottomShrouded ? BIT_MASK_BOTTOM : BIT_MASK_NONE;
        value |= isLeftShrouded   ? BIT_MASK_LEFT   : BIT_MASK_NONE;
        return FACINGS[value];
    }
}
