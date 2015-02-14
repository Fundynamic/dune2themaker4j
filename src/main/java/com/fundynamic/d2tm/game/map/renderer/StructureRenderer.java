package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.structures.Structure;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class StructureRenderer implements Renderer<Structure> {

    public StructureRenderer() {
    }

    @Override
    public void draw(Graphics graphics, Structure structure, int drawX, int drawY) {
        Image sprite = structure.getSprite();
        graphics.drawImage(sprite, drawX, drawY);

        // if selected, draw a rectangle
        // TODO: make it a fading rectangle aka Dune 2? (the feelz!)
        if (structure.isSelected()) {
            graphics.setColor(Color.white);
            graphics.setLineWidth(1.1f);
            graphics.drawRect(drawX, drawY, structure.getWidth() - 1, structure.getHeight() - 1);
        }
    }

}
