package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class HitPointBasedDestructibility implements EnrichableAbsoluteRenderable {

    private int maxHitpoints;
    private int widthInPixels;

    private int hitPoints;

    public HitPointBasedDestructibility(int maxHitpoints, int widthInPixels) {
        this.hitPoints = maxHitpoints;
        this.maxHitpoints = maxHitpoints;
        this.widthInPixels = widthInPixels;
    }

    public void takeDamage(int hitPoints) {
        this.hitPoints -= hitPoints;
    }

    public boolean hasDied() {
        return hitPoints < 1;
    }

    @Override
    public String toString() {
        return "HitPointBasedDestructibility{" +
                "hitPoints=" + hitPoints +
                '}';
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void render(Graphics graphics, int x, int y) {
        graphics.setColor(Color.white);
        graphics.setLineWidth(1.1f);
        graphics.fillRect(x - 1, y - 7, widthInPixels + 2, 7);

        graphics.setColor(Color.black);
        graphics.setLineWidth(1.1f);
        graphics.fillRect(x, y - 6, widthInPixels, 5);

        graphics.setColor(getHealthBarColor());
        graphics.setLineWidth(1.1f);
        graphics.fillRect(x, y - 6, getHealthBarPixelWidth(), 5);
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // do nothing
    }

    public Color getHealthBarColor() {
        if (getPercentage() > 0.5F) {
            return Color.green;
        } else if (getPercentage() > 0.15F) {
            return Color.yellow;
        }
        return Color.red;
    }

    public int getHealthBarPixelWidth() {
        float percentage = getPercentage();
        return (int)(percentage * widthInPixels);
    }

    public float getPercentage() {
        return (float)hitPoints / maxHitpoints;
    }

}
