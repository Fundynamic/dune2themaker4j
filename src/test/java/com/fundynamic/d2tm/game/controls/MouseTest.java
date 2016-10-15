package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MouseTest extends AbstractD2TMTest {

    @Test
    public void createsMouse() throws SlickException {
        assertThat(mouse, is(notNullValue()));
        assertThat(mouse.getControllingPlayer(), is(player));
    }

}