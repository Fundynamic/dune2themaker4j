package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class EntityTest extends AbstractD2TMTest {

    private Entity entity;
    private Coordinate topLeftCoordinate;
    private EntityData entityData;

    @Before
    public void setup() {
        topLeftCoordinate = Coordinate.create(Game.TILE_SIZE * 4, Game.TILE_SIZE * 4); // 4X32 = 128
        entityData = new EntityData();
        entityData.setWidth(64);
        entityData.setHeight(64);

        entity = new Entity(topLeftCoordinate, mock(SpriteSheet.class), entityData, player, entityRepository) {
            @Override
            public EntityType getEntityType() {
                return null;
            }

            @Override
            public void render(Graphics graphics, int x, int y) {
                // no rendering, boring entity
            }

            @Override
            public void update(float deltaInSeconds) {
                // no updating, boring entity
            }
        };
    }

    @Test
    public void randomPositionWithinDoesNotExceedBoundaries() {

        // choose an arbitrary amount of calls (100) and just try it.
        // Yeah, this could be more efficient with using a seed and whatnot...
        for (int i = 0; i < 100; i++) {
            Vector2D randomPositionWithin = entity.getRandomPositionWithin();
            assertThat(randomPositionWithin.getX(), is(not(lessThan(topLeftCoordinate.getX() - 16))));
            assertThat(randomPositionWithin.getY(), is(not(lessThan(topLeftCoordinate.getY() - 16))));
            assertThat(randomPositionWithin.getX(), is(not(greaterThan(topLeftCoordinate.getX() + entityData.getWidth()))));
            assertThat(randomPositionWithin.getY(), is(not(greaterThan(topLeftCoordinate.getY() + entityData.getHeight()))));
        }
    }

    @Test
    public void getAllSurroundingCoordinatesOfAnEntity() {
        // entity is 64x64 pixels (ie, 2x2). Marked as X
        // The tiles we would expect are marked as E
        // EEEE  4
        // EXXE  2
        // EXXE  2
        // EEEE  4
        //
        // 4 + 2 + 2 + 4 => 12 surrounding cells
        List<MapCoordinate> allSurroundingCellsAsCoordinates = entity.getAllSurroundingCellsAsCoordinates();

        Assert.assertEquals(12, allSurroundingCellsAsCoordinates.size());

        // we expect the first tile to be at 1 tile above and 1 tile left to the entity:
        MapCoordinate upLeftOfTopLeft = allSurroundingCellsAsCoordinates.get(0);

        Assert.assertEquals(3, upLeftOfTopLeft.getXAsInt());
        Assert.assertEquals(3, upLeftOfTopLeft.getYAsInt());
    }

}