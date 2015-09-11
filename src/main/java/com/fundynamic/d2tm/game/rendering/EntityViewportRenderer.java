package com.fundynamic.d2tm.game.rendering;


import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashSet;
import java.util.Set;

public class EntityViewportRenderer {

    private final int tileHeight;
    private final int tileWidth;
    private final int cellsThatFitHorizontally;
    private final int cellsThatFitVertically;
    private final Map map;

    public EntityViewportRenderer(Map map, int tileHeight, int tileWidth, Vector2D windowDimensions) {
        this.map = map;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        cellsThatFitHorizontally = (windowDimensions.getXAsInt() / tileWidth) + 1;
        cellsThatFitVertically = (windowDimensions.getYAsInt() / tileHeight) + 1;
    }

    public void render(Image imageToDrawOn, Vector2D viewingVector) throws SlickException {
        int startCellX = viewingVector.getXAsInt() / tileWidth;
        int startCellY = viewingVector.getYAsInt() / tileHeight;

        int endCellX = startCellX + cellsThatFitHorizontally;
        int endCellY = startCellY + cellsThatFitVertically;

        Set<EntityToDraw> entitiesToDraw = new HashSet<>();
        for (int x = startCellX; x <= endCellX; x++) {
            for (int y = startCellY; y <= endCellY; y++) {
                Cell cell = map.getCell(x, y);
                Entity entity = cell.getEntity();
                if (entity == null) continue;

                // TODO: try to find entity without the use of map (from cell), but rather from a 'list' of entities, which
                // TODO: we smartly query. Also, take order by z-order so we draw stuff over each other correctly
                // TODO: - use quadtree for fast lookup based on coordinates?
                Vector2D mapCoordinates = entity.getMapCoordinates();

                int drawX = ((mapCoordinates.getXAsInt() - startCellX) * tileWidth) - (viewingVector.getXAsInt() % tileWidth);
                int drawY = ((mapCoordinates.getYAsInt() - startCellY) * tileHeight) - (viewingVector.getYAsInt() % tileHeight);

                entitiesToDraw.add(new EntityToDraw(entity, drawX, drawY));
            }
        }

        for (EntityToDraw entityToDraw : entitiesToDraw) {
            entityToDraw.entity.render(imageToDrawOn.getGraphics(), entityToDraw.drawX, entityToDraw.drawY);
        }
    }

    private class EntityToDraw {
        private Entity entity;
        private int drawX, drawY;

        public EntityToDraw(Entity entity, int drawX, int drawY) {
            this.entity = entity;
            this.drawX = drawX;
            this.drawY = drawY;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EntityToDraw that = (EntityToDraw) o;

            if (drawX != that.drawX) return false;
            if (drawY != that.drawY) return false;
            if (entity != null ? !entity.equals(that.entity) : that.entity != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = entity != null ? entity.hashCode() : 0;
            result = 31 * result + drawX;
            result = 31 * result + drawY;
            return result;
        }
    }

}
