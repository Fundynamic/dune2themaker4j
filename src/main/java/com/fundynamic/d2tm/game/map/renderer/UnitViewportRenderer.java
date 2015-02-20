package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.units.Unit;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashSet;
import java.util.Set;

public class UnitViewportRenderer implements ViewportRenderer<Unit>{

    private final int tileHeight;
    private final int tileWidth;
    private final int cellsThatFitHorizontally;
    private final int cellsThatFitVertically;
    private final Map map;

    public UnitViewportRenderer(Map map, int tileHeight, int tileWidth, Vector2D windowDimensions) {
        this.map = map;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        cellsThatFitHorizontally = (windowDimensions.getXAsInt() / tileWidth) + 1;
        cellsThatFitVertically = (windowDimensions.getYAsInt() / tileHeight) + 1;
    }


    @Override
    public void render(Image imageToDrawOn, Vector2D viewingVector, Renderer<Unit> renderer) throws SlickException {
        int startCellX = viewingVector.getXAsInt() / tileWidth;
        int startCellY = viewingVector.getYAsInt() / tileHeight;

        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        Set<UnitToDraw> unitsToDraw = new HashSet<>();
        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                Cell cell = map.getCell(x, y);
                Unit unit = cell.getUnit();
                if (unit == null) continue;

                Vector2D mapCoordinates = unit.getMapCoordinates();

                int drawX = ((mapCoordinates.getXAsInt() - startCellX) * tileWidth) - (viewingVector.getXAsInt() % tileWidth);
                int drawY = ((mapCoordinates.getYAsInt() - startCellY) * tileHeight) - (viewingVector.getYAsInt() % tileHeight);

                unitsToDraw.add(new UnitToDraw(unit, drawX, drawY));
            }
        }

        for (UnitToDraw unitToDraw : unitsToDraw) {
            renderer.draw(imageToDrawOn.getGraphics(), unitToDraw.unit, unitToDraw.drawX, unitToDraw.drawY);
        }
    }


    private class UnitToDraw {
        private Unit unit;
        private int drawX, drawY;

        public UnitToDraw(Unit unit, int drawX, int drawY) {
            this.unit = unit;
            this.drawX = drawX;
            this.drawY = drawY;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UnitToDraw that = (UnitToDraw) o;

            if (drawX != that.drawX) return false;
            if (drawY != that.drawY) return false;
            if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = unit != null ? unit.hashCode() : 0;
            result = 31 * result + drawX;
            result = 31 * result + drawY;
            return result;
        }
    }
}
