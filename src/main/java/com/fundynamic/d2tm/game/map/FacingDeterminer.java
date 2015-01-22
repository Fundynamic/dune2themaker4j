package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.graphics.TerrainFacing;

public class FacingDeterminer {
    private static final int BIT_MASK_NONE = 0;
    private static final int BIT_MASK_TOP = 0x0008;
    private static final int BIT_MASK_RIGHT = 0x0004;
    private static final int BIT_MASK_BOTTOM = 0x0002;
    private static final int BIT_MASK_LEFT = 0x0001;

    private static final TerrainFacing[] FACINGS = {
            TerrainFacing.MIDDLE,               // 0x0000
            TerrainFacing.TOP_RIGHT_BOTTOM,     // 0x0001
            TerrainFacing.TOP_RIGHT_LEFT,       // 0x0010
            TerrainFacing.TOP_RIGHT,            // 0x0011
            TerrainFacing.TOP_BOTTOM_LEFT,      // 0x0100
            TerrainFacing.TOP_BOTTOM,           // 0x0101
            TerrainFacing.TOP_LEFT,             // 0x0110
            TerrainFacing.TOP,                  // 0x0111
            TerrainFacing.RIGHT_BOTTOM_LEFT,    // 0x1000
            TerrainFacing.RIGHT_BOTTOM,         // 0x1001
            TerrainFacing.RIGHT_LEFT,           // 0x1010
            TerrainFacing.RIGHT,                // 0x1011
            TerrainFacing.BOTTOM_LEFT,          // 0x1100
            TerrainFacing.BOTTOM,               // 0x1101
            TerrainFacing.LEFT,                 // 0x1110
            TerrainFacing.FULL,                 // 0x1111
    };

    private boolean isTopSame;
    private boolean isRightSame;
    private boolean isBottomSame;
    private boolean isLeftSame;

    public void setTopSame(boolean topSame) {
        this.isTopSame = topSame;
    }

    public void setRightSame(boolean rightSame) {
        isRightSame = rightSame;
    }

    public void setBottomSame(boolean bottomSame) {
        this.isBottomSame = bottomSame;
    }

    public void setLeftSame(boolean leftSame) {
        isLeftSame = leftSame;
    }

    public TerrainFacing getFacing() {
        int value = 0;
        value |= isTopSame    ? BIT_MASK_TOP    : BIT_MASK_NONE;
        value |= isRightSame  ? BIT_MASK_RIGHT  : BIT_MASK_NONE;
        value |= isBottomSame ? BIT_MASK_BOTTOM : BIT_MASK_NONE;
        value |= isLeftSame   ? BIT_MASK_LEFT   : BIT_MASK_NONE;
        return FACINGS[value];
    }
}
