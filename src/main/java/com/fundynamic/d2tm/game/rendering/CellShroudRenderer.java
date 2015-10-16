package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.graphics.Shroud;
import org.newdawn.slick.Graphics;

public class CellShroudRenderer implements Renderer<Cell> {

    private final Shroud shroud;

    private final Player player;

    public CellShroudRenderer(Player player, Shroud shroud) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");
        this.player = player;
        this.shroud = shroud;
    }

    @Override
    public void draw(Graphics graphics, Cell cell, int drawX, int drawY) {
        ShroudFacing shroudFacing = determineShroudFacing(cell);
        if (shroudFacing != null) {
            graphics.drawImage(shroud.getShroudImage(shroudFacing), drawX, drawY);
        }
    }

    protected ShroudFacing determineShroudFacing(Cell cell) {
        if (player.isShrouded(cell.getPosition())) {
            return ShroudFacing.FULL;
        }

        return getFacing(
                player.isShrouded(cell.getCellAbove().getPosition()),
                player.isShrouded(cell.getCellRight().getPosition()),
                player.isShrouded(cell.getCellBeneath().getPosition()),
                player.isShrouded(cell.getCellLeft().getPosition()));
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
}
