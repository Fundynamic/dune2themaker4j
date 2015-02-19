package com.fundynamic.d2tm.game.map.renderer;


import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.Structure;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashSet;
import java.util.Set;

public class StructureViewportRenderer implements ViewportRenderer<Structure> {

    private final int tileHeight;
    private final int tileWidth;
    private final int cellsThatFitHorizontally;
    private final int cellsThatFitVertically;
    private final Map map;

    public StructureViewportRenderer(Map map, int tileHeight, int tileWidth, Vector2D windowDimensions) {
        this.map = map;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        cellsThatFitHorizontally = (windowDimensions.getXAsInt() / tileWidth) + 1;
        cellsThatFitVertically = (windowDimensions.getYAsInt() / tileHeight) + 1;
    }

    @Override
    public void render(Image imageToDrawOn, Vector2D viewingVector, Renderer<Structure> renderer) throws SlickException {
        int startCellX = viewingVector.getXAsInt() / tileWidth;
        int startCellY = viewingVector.getYAsInt() / tileHeight;

        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        Set<StructureToDraw> structuresToDraw = new HashSet<>();
        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getStructure() == null) continue;

                Vector2D mapCoordinates = cell.getStructure().getMapCoordinates();

                int drawX = ((mapCoordinates.getXAsInt() - startCellX) * tileWidth) - (viewingVector.getXAsInt() % tileWidth);
                int drawY = ((mapCoordinates.getYAsInt() - startCellY) * tileHeight) - (viewingVector.getYAsInt() % tileHeight);

                structuresToDraw.add(new StructureToDraw(cell.getStructure(), drawX, drawY));
            }
        }

        for (StructureToDraw structureToDraw : structuresToDraw) {
            renderer.draw(imageToDrawOn.getGraphics(), structureToDraw.structure, structureToDraw.drawX, structureToDraw.drawY);
        }
    }

    private class StructureToDraw {
        private Structure structure;
        private int drawX, drawY;

        public StructureToDraw(Structure structure, int drawX, int drawY) {
            this.structure = structure;
            this.drawX = drawX;
            this.drawY = drawY;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StructureToDraw that = (StructureToDraw) o;

            if (drawX != that.drawX) return false;
            if (drawY != that.drawY) return false;
            if (structure != null ? !structure.equals(that.structure) : that.structure != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = structure != null ? structure.hashCode() : 0;
            result = 31 * result + drawX;
            result = 31 * result + drawY;
            return result;
        }
    }

}
