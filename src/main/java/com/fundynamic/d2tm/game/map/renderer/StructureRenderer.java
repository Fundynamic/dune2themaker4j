package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.structures.Structure;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class StructureRenderer implements Renderer<Structure> {

    private float selectedIntensity;
    private boolean selectedDarkening;
    private final float intensityChange;

    public StructureRenderer() {
        selectedIntensity = 1.0f;
        selectedDarkening = true;
        intensityChange = 0.5f / 60; // TODO: make time based
    }

    @Override
    public void draw(Graphics graphics, Structure structure, int drawX, int drawY) {
        Image sprite = structure.getSprite();
        graphics.drawImage(sprite, drawX, drawY);

        if (structure.isSelected()) {
            graphics.setColor(new Color(selectedIntensity, selectedIntensity, selectedIntensity));
            graphics.setLineWidth(2.f);
            graphics.drawRect(drawX, drawY, structure.getWidth() - 1, structure.getHeight() - 1);

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
    }

}
