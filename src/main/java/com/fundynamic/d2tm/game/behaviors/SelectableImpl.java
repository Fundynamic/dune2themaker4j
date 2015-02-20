package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.game.map.MapEntity;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class SelectableImpl {

    private float selectedIntensity;
    private boolean selectedDarkening;

    private boolean selected;

    private final int width;
    private final int height;

    public SelectableImpl(int width, int height) {
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

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void render(Graphics graphics, int drawX, int drawY) {
        if (selected) {
            graphics.setColor(new Color(selectedIntensity, selectedIntensity, selectedIntensity));
            graphics.setLineWidth(2.f);
            graphics.drawRect(drawX, drawY, width, height - 1);
        }
    }

    public static boolean isSelectable(MapEntity mapEntity) {
        return mapEntity.getClass().isInstance(SelectableImpl.class);
    }
}
