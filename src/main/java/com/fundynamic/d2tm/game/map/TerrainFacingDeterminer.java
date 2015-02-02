package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.graphics.TerrainFacing;

public class TerrainFacingDeterminer {
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

    public static TerrainFacing getFacing(boolean isTopSame, boolean isRightSame, boolean isBottomSame, boolean isLeftSame) {
        int value = 0;
        value |= isTopSame    ? BIT_MASK_TOP    : BIT_MASK_NONE;
        value |= isRightSame  ? BIT_MASK_RIGHT  : BIT_MASK_NONE;
        value |= isBottomSame ? BIT_MASK_BOTTOM : BIT_MASK_NONE;
        value |= isLeftSame   ? BIT_MASK_LEFT   : BIT_MASK_NONE;
        return FACINGS[value];
    }
}
