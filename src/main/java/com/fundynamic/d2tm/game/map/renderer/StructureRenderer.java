package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.structures.Structure;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class StructureRenderer implements Renderer<Structure> {

    @Override
    public void draw(Graphics graphics, Structure structure, int drawX, int drawY) {
        Image sprite = structure.getSprite();
        graphics.drawImage(sprite, drawX, drawY);

        if (structure.isSelected()) {
            float intensity = structure.getSelectedIntensity();
            graphics.setColor(new Color(intensity, intensity, intensity));
            graphics.setLineWidth(2.f);
            graphics.drawRect(drawX, drawY, structure.getWidth() - 1, structure.getHeight() - 1);
        }
    }

}
