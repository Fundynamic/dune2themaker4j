package com.fundynamic.d2tm.game.behaviors;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * An implementation of selecting / deselecting something + with drawing effects
 */
public class FadingSelection extends SimpleSelectLogic implements Renderable, Updateable {

    private float selectedIntensity;
    private boolean selectedDarkening;

    private final int width;
    private final int height;

    public FadingSelection(int width, int height) {
        this.width = width;
        this.height = height;
        this.selectedIntensity = 1f;
        this.selectedDarkening = true;
    }

    public void update(float delta) {
        if (selected) {
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
        } else {
            selectedIntensity = 1f;
            selectedDarkening = true;
        }
    }

    @Override
    public void render(Graphics graphics, int drawX, int drawY) {
        if (selected) {
            graphics.setColor(new Color(selectedIntensity, selectedIntensity, selectedIntensity));
            graphics.setLineWidth(2.f);
            graphics.drawRect(drawX, drawY, width, height - 1);
        }
    }
}
