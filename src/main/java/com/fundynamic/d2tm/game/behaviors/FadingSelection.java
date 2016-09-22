package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.utils.Colors;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * An implementation of selecting / deselecting something + with drawing effects
 */
public class FadingSelection extends SimpleSelectLogic implements EnrichableAbsoluteRenderable, Updateable {

    public static final float DEFAULT_LINE_THICKNESS = 2.0f;
    private float selectedIntensity;
    private boolean selectedDarkening;

    private final int width;
    private final int height;
    private float lineWidth;

    public FadingSelection(int width, int height) {
        this(width, height, DEFAULT_LINE_THICKNESS);
    }

    public FadingSelection(int width, int height, float lineWidth) {
        this.width = width;
        this.height = height;
        this.lineWidth = lineWidth;
        resetFading();
    }

    public void update(float delta) {
        if (selected) {
            updateFadingColor(delta);
        } else {
            resetFading();
        }
    }

    public void resetFading() {
        selectedIntensity = 1f;
        selectedDarkening = true;
    }

    public void updateFadingColor(float delta) {
        float intensityChange = .5f * delta;
        if (selectedDarkening) {
            selectedIntensity -= intensityChange;
        } else {
            selectedIntensity += intensityChange;
        }

        // fade back and forth
        if (selectedIntensity <= 0.0f) {
            selectedIntensity = 0.0f;
            selectedDarkening = false;
        } else if (selectedIntensity >= 1.0f) {
            selectedIntensity = 1.0f;
            selectedDarkening = true;
        }
    }

    @Override
    public void render(Graphics graphics, int drawX, int drawY) {
        if (selected) {
            float lineWidth = graphics.getLineWidth();
            graphics.setColor(getFadingColor(selectedIntensity));
            graphics.setLineWidth(this.lineWidth);
            graphics.drawRect(drawX, drawY, width, height - 1);
            graphics.setLineWidth(lineWidth);
        }
    }

    public Color getFadingColor(float selectedIntensity) {
        return Colors.create(selectedIntensity, this.selectedIntensity, this.selectedIntensity);
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // do nothing
    }
}
