package com.fundynamic.d2tm.game.entities.projectiles;

import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.math.Vector2D.create;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class ProjectileTest {

    public static final Vector2D CENTER_FROM = create(64, 64);

    public static final Vector2D RIGHT = CENTER_FROM.add(create(32, 0));
    public static final Vector2D LEFT = CENTER_FROM.min(create(32, 0));
    public static final Vector2D UP = CENTER_FROM.min(create(0, 32));
    public static final Vector2D DOWN = CENTER_FROM.add(create(0, 32));

    public static final Vector2D UP_LEFT = UP.min(create(32, 0));
    public static final Vector2D UP_RIGHT = UP.add(create(32, 0));
    public static final Vector2D DOWN_LEFT = DOWN.min(create(32, 0));
    public static final Vector2D DOWN_RIGHT = DOWN.add(create(32, 0));

    public static final Vector2D UP_RIGHT_RIGHTX5 = UP_RIGHT.add(create(128, 0));
    public static final Vector2D UP_RIGHT_RIGHTX5_WITH_JUST_ENOUGH_RIGHT_JUST_BELOW_HALF_CHOP = UP_RIGHT.add(create(129, 0));

    public static final Vector2D UP_RIGHT_RIGHT = UP_RIGHT.add(create(32, 0)); // a bit more to the right (tilts rocket a bit downwards)
    public static final Vector2D UP_RIGHT_UP = UP_RIGHT.min(create(0, 32)); // a bit more to the up (tilts rocket a bit more upwards)

    public static final Vector2D UP_LEFT_LEFT = UP_LEFT.min(create(32, 0)); // a bit more to the left (tilts rocket a bit downwards)
    public static final Vector2D UP_LEFT_UP = UP_LEFT.min(create(0, 32)); // a bit more to the up (tilts rocket a bit more upwards)

    public static final Vector2D DOWN_LEFT_LEFT = DOWN_LEFT.min(create(32, 0));
    public static final Vector2D DOWN_LEFT_DOWN = DOWN_LEFT.add(create(0, 32));

    public static final Vector2D DOWN_RIGHT_DOWN = DOWN_RIGHT.add(create(0, 32));
    public static final Vector2D DOWN_RIGHT_RIGHT = DOWN_RIGHT.add(create(32, 0));
    public static final Vector2D DOWN_RIGHT_RIGHTX5 = DOWN_RIGHT.add(create(129, 0));

    private Projectile projectile;

    // this is the order as in LargeRocket.png
    @Before
    public void setUp() throws Exception {
        projectile = makeProjectileWithOnlyEntityData();
    }

    @Test
    public void fromCenterToRight() throws SlickException {
        assertFacingIs(CENTER_FROM, RIGHT, 0);
    }

    @Test
    public void fromCenterToUpRightRightX5() throws SlickException {
        assertFacingIs(CENTER_FROM, UP_RIGHT_RIGHTX5, 1);
    }

    @Test
    public void fromCenterToUpRightRightJustBelowHalfChopThreshold() throws SlickException {
        assertFacingIs(CENTER_FROM, UP_RIGHT_RIGHTX5_WITH_JUST_ENOUGH_RIGHT_JUST_BELOW_HALF_CHOP, 0);
    }

    @Test
    public void fromCenterToUpRightRight() throws SlickException {
        assertFacingIs(CENTER_FROM, UP_RIGHT_RIGHT, 1);
    }

    @Test
    public void fromCenterToUpRight() throws SlickException {
        assertFacingIs(CENTER_FROM, UP_RIGHT, 2);
    }

    @Test
    public void fromCenterToUpRightUp() throws SlickException {
        assertFacingIs(CENTER_FROM, UP_RIGHT_UP, 3);
    }

    @Test
    public void fromCenterToUp() throws SlickException {
        assertFacingIs(CENTER_FROM, UP, 4);
    }

    @Test
    public void fromCenterToUpLeftUp() throws SlickException {
        assertFacingIs(CENTER_FROM, UP_LEFT_UP, 5);
    }

    @Test
    public void fromCenterToUpLeft() throws SlickException {
        assertFacingIs(CENTER_FROM, UP_LEFT, 6);
    }

    @Test
    public void fromCenterToUpLeftLeft() throws SlickException {
        assertFacingIs(CENTER_FROM, UP_LEFT_LEFT, 7);
    }

    @Test
    public void fromCenterToLeft() throws SlickException {
        assertFacingIs(CENTER_FROM, LEFT, 8);
    }

    @Test
    public void fromCenterToDownLeftLeft() throws SlickException {
        assertFacingIs(CENTER_FROM, DOWN_LEFT_LEFT, 9);
    }

    @Test
    public void fromCenterToDownLeft() throws SlickException {
        assertFacingIs(CENTER_FROM, DOWN_LEFT, 10);
    }

    @Test
    public void fromCenterToDownLeftDown() throws SlickException {
        assertFacingIs(CENTER_FROM, DOWN_LEFT_DOWN, 11);
    }

    @Test
    public void fromCenterToDown() throws SlickException {
        assertFacingIs(CENTER_FROM, DOWN, 12);
    }

    @Test
    public void fromCenterToDownRightDown() throws SlickException {
        assertFacingIs(CENTER_FROM, DOWN_RIGHT_DOWN, 13);
    }

    @Test
    public void fromCenterToDownRight() throws SlickException {
        assertFacingIs(CENTER_FROM, DOWN_RIGHT, 14);
    }

    @Test
    public void fromCenterToDownRightRight() throws SlickException {
        assertFacingIs(CENTER_FROM, DOWN_RIGHT_RIGHT, 15);
    }

    @Test
    public void fromCenterToDownRightX5SoItWrapsAroundBackToFacingZero() throws SlickException {
        assertFacingIs(CENTER_FROM, DOWN_RIGHT_RIGHTX5, 0); // wraps from 16 to 0
    }

    public void assertFacingIs(Vector2D from, Vector2D to, int expectedFacing) {
        assertThat(projectile.getFacing(from, to), is(expectedFacing));
    }

    public Projectile makeProjectileWithOnlyEntityData() {
        // Given a Projectile with 16 facings, see LargeBullet.png
        EntityData entityData = new EntityData();
        entityData.setFacingsAndCalculateChops(16);

        return new Projectile(null, null, null, 0, null, entityData, null);
    }

}