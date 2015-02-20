package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.units.Unit;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class UnitRenderer implements Renderer<Unit> {

    @Override
    public void draw(Graphics graphics, Unit unit, int drawX, int drawY) {
        Image sprite = unit.getSprite();
        graphics.drawImage(sprite, drawX, drawY);

//        if (unit.isSelected()) {
//            float intensity = unit.getSelectedIntensity();
//            graphics.setColor(new Color(intensity, intensity, intensity));
//            graphics.setLineWidth(2.f);
//            graphics.drawRect(drawX, drawY, unit.getWidth() - 1, unit.getHeight() - 1);
//        }
    }

}
