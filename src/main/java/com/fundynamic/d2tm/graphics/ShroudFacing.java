package com.fundynamic.d2tm.graphics;

/**
 * This enum has types for all kind of facing for the shroud.
 * <p/>
 * The types are explained by looking at the graphical representation, and then pointing out
 * at what sides the shroud is drawn.
 * <p/>
 * Example:
 * Looking at spritesheet, the very first tile is a full shroud tile, so the first type in the enum is FULL.
 * The second tile (one to the right), is a tile with shroud in the top left corner.
 * We go clockwise so the enum is TOP_LEFT
 */
public enum ShroudFacing {
    FULL,
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    RIGHT,
    RIGHT_BOTTOM,
    BOTTOM,
    BOTTOM_LEFT,
    LEFT,
    TOP_BOTTOM_LEFT,
    TOP_RIGHT_LEFT,
    TOP_RIGHT_BOTTOM,
    RIGHT_BOTTOM_LEFT,
    TOP_BOTTOM,
    RIGHT_LEFT,
    MIDDLE
}
