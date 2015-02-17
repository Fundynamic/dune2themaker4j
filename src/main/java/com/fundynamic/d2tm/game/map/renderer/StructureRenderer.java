package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.structures.Structure;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class StructureRenderer implements Renderer<Structure> {

    private float selectedIntensity;
    private boolean selectedDarkening;
    
	public StructureRenderer() {
		selectedIntensity = 1.0f;
		selectedDarkening = true;
    }

    @Override
    public void draw(Graphics graphics, Structure structure, int drawX, int drawY) {
        Image sprite = structure.getSprite();
        graphics.drawImage(sprite, drawX, drawY);

        // if selected, draw a rectangle
        // TODO: make it a fading rectangle aka Dune 2? (the feelz!)
        if (structure.isSelected()) {
            graphics.setColor(new Color(selectedIntensity, selectedIntensity, selectedIntensity));
            graphics.setLineWidth(1.1f);
            graphics.drawRect(drawX, drawY, structure.getWidth() - 1, structure.getHeight() - 1);
            
            // Amount to change intensity by
            // TODO: Scale according to current FPS instead of fixed value
            float intensityChange = 0.5f/60;
            if ( selectedDarkening ) {
            	selectedIntensity -= intensityChange;
            }
            else {
            	selectedIntensity += intensityChange;
            }
            // Change if we're increasing or decreasing intensity
        	if ( selectedIntensity <= 0.0f ) {
        		selectedIntensity = 0.0f;
        		selectedDarkening = false;
        	}
        	else if ( selectedIntensity >= 1.0f ) {
        		selectedIntensity = 1.0f;
        		selectedDarkening = true;
        	}
        }
    }

}
