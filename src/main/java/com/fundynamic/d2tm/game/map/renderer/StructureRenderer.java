package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Random;
import com.fundynamic.d2tm.game.structures.ConstructionYard;
import org.newdawn.slick.Graphics;

public class StructureRenderer implements Renderer {

    private final Map map;

    public StructureRenderer(Map map) {
        this.map = map;
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int drawX, int drawY) {
        ConstructionYard constructionYard = map.getCell(x, y).getConstructionYard();
        if (constructionYard != null) {
            graphics.drawImage(constructionYard.getSpriteSheet().getSprite(0, Random.getRandomBetween(0, 2)), drawX, drawY);
        }
    }

}
