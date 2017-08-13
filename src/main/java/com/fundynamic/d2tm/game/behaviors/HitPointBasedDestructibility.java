package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * <h1>Concept of getting destroyed by using current</h1>
 */
public class HitPointBasedDestructibility implements EnrichableAbsoluteRenderable {

    private int maxHitpoints;
    private int widthInPixels;

    private float current;

    public HitPointBasedDestructibility(int max, int widthInPixels) {
        this.current = max;
        this.maxHitpoints = max;
        this.widthInPixels = widthInPixels;
    }

    public void reduce(float amount) {
        this.current = Math.max(this.current - amount, 0);
    }

    public void add(float amount) {
        this.current = Math.min(this.current + amount, this.maxHitpoints);
    }

    @Override
    public String toString() {
        return "HitPointBasedDestructibility{" +
                "current=" + current +
                '}';
    }

    public int getCurrent() {
        return (int) current;
    }

    public void render(Graphics graphics, int x, int y) {
        graphics.setColor(Color.white);
        graphics.setLineWidth(1.1f);
        graphics.fillRect(x - 1, y - 7, widthInPixels + 2, 7);

        graphics.setColor(Color.black);
        graphics.setLineWidth(1.1f);
        graphics.fillRect(x, y - 6, widthInPixels, 5);

        graphics.setColor(getBarColor());
        graphics.setLineWidth(1.1f);
        graphics.fillRect(x, y - 6, getHealthBarPixelWidth(), 5);
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // do nothing
    }

    public Color getBarColor() {
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
        return (float) current / maxHitpoints;
    }

    public void toZero() {
        current = 0;
    }

    public boolean isMaxed() {
        return current >= maxHitpoints;
    }

    public boolean isZero() {
        return current <= 0.0000001f;
    }
}
