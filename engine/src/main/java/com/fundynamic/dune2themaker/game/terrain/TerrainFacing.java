package com.fundynamic.dune2themaker.game.terrain;

/**
 * This enum has types for all kind of facing for a specific TerrainType. (non-walls)
 * <p/>
 * The types are explained by looking at the graphical representation, and then pointing out
 * at what sides the terrain type is drawn.
 * <p/>
 * Example:
 * TerrainType = Rock
 * Looking at spritesheet, of rock.
 * The very first tile is a full rock tile, so the first type in the enum is FULL
 * The second tile (one to the right), is a tile with sand on the left, rock on the top, right, bottom.
 * (we go clockwise), so the enum is TOP_RIGHT_BOTTOM
 */
public enum TerrainFacing {
	FULL,
	TOP_RIGHT_BOTTOM, TOP_BOTTOM_LEFT, RIGHT_BOTTOM_LEFT, TOP_RIGHT_LEFT,
	RIGHT_BOTTOM, TOP_LEFT, BOTTOM_LEFT, TOP_RIGHT,
	MIDDLE,
	RIGHT_LEFT,
	LEFT, RIGHT, TOP, BOTTOM,
	TOP_BOTTOM
}
