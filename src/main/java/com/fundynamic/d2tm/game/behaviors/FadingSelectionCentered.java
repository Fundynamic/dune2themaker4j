package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * An implementation of selecting / deselecting something + with drawing effects
 */
public class FadingSelectionCentered extends FadingSelection {

    private Vector2D drawCorrectionVec;

    public FadingSelectionCentered(int width, int height) {
        this(width, height, DEFAULT_LINE_THICKNESS);
    }

    public FadingSelectionCentered(int width, int height, float lineWidth) {
        super(width, height, lineWidth);
        this.drawCorrectionVec = Vector2D.create(
                (TILE_SIZE - width) / 2,
                (TILE_SIZE - height) / 2
        );
    }


    @Override
    public void render(Graphics graphics, int drawX, int drawY) {
        super.render(graphics, drawX + drawCorrectionVec.getXAsInt(), drawY + drawCorrectionVec.getYAsInt());
    }

}
