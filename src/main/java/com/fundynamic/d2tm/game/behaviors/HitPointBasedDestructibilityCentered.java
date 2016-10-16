package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

/**
 * <h1>Concept of getting destroyed by using hitpoints</h1>
 */
public class HitPointBasedDestructibilityCentered extends HitPointBasedDestructibility {

    private Vector2D drawCorrectionVec;

    public HitPointBasedDestructibilityCentered(int maxHitpoints, int widthInPixels, int heightInPixels) {
        super(maxHitpoints, widthInPixels);
        this.drawCorrectionVec = Vector2D.create(
                (Game.TILE_SIZE - widthInPixels) / 2,
                (Game.TILE_SIZE - heightInPixels) / 2
        );
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        super.render(graphics, x + drawCorrectionVec.getXAsInt(), y + drawCorrectionVec.getYAsInt());
    }
}
