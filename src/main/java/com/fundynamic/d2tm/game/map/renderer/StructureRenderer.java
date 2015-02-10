package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.structures.ConstructionYard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class StructureRenderer implements CellRenderer {

    private final Map map;
    private final Mouse mouse;

    public StructureRenderer(Map map, Mouse mouse) {
        this.map = map;
        this.mouse = mouse;
    }

    @Override
    public void draw(Graphics graphics, int x, int y, int drawX, int drawY) {
        Cell cell = map.getCell(x, y);
        ConstructionYard constructionYard = cell.getConstructionYard();
        if (cell.isTopLeftOfStructure() && constructionYard != null) {
            Image sprite = constructionYard.getSprite();
            graphics.drawImage(sprite, drawX, drawY);

            // if selected, draw a rectangle
            // TODO: make it a fading rectangle aka Dune 2? (the feelz!)
            if (mouse.hasThisStructureSelected(constructionYard)) {
                graphics.setColor(Color.white);
                graphics.setLineWidth(1.1f);
                graphics.drawRect(drawX, drawY, constructionYard.getWidth() - 1, constructionYard.getHeight() - 1);
            }
        }
    }

}
