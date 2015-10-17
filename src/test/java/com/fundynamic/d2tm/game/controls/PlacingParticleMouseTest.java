package com.fundynamic.d2tm.game.controls;


import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

public class PlacingParticleMouseTest extends AbstractMouseBehaviorTest {

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        mouse.setMouseBehavior(new PlacingParticleMouse(mouse, entityRepository));
    }

    @Test
    public void leftClicked() {
        mouse.leftClicked();
    }

    @Test
    public void render() {
        mouse.render(graphics);
    }
}
