package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.SlickException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MouseTest extends AbstractD2TMTest {

    @Test
    public void createsMouse() throws SlickException {
        Mouse mouse = Mouse.create(player, gameContainer, entityRepository, imageRepository);

        assertThat(mouse, is(notNullValue()));
        assertThat(mouse.getControllingPlayer(), is(player));
        assertThat(mouse.getMouseBehavior(), instanceOf(NormalMouse.class));
        assertThat(mouse.getHoverCell(), is(nullValue()));
    }

}