package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class EntityTest extends AbstractD2TMTest {

    @Test
    public void randomPositionWithinDoesNotExceedBoundaries() {
        Vector2D absoluteCoordinates = Vector2D.create(100, 100);
        EntityData entityData = new EntityData();
        entityData.setWidth(64);
        entityData.setHeight(64);
        Entity entity = new Entity(absoluteCoordinates, mock(SpriteSheet.class), entityData, player, entityRepository) {
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

        // choose an arbitrary amount of calls (100) and just try it.
        // Yeah, this could be more efficient with using a seed and whatnot...
        for (int i = 0; i < 100; i++) {
            Vector2D randomPositionWithin = entity.getRandomPositionWithin();
            assertThat(randomPositionWithin.getX(), is(not(lessThan(absoluteCoordinates.getX() - 16))));
            assertThat(randomPositionWithin.getY(), is(not(lessThan(absoluteCoordinates.getY() - 16))));
            assertThat(randomPositionWithin.getX(), is(not(greaterThan(absoluteCoordinates.getX() + entityData.getWidth()))));
            assertThat(randomPositionWithin.getY(), is(not(greaterThan(absoluteCoordinates.getY() + entityData.getHeight()))));
        }
    }

}