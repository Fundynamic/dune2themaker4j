package com.fundynamic.d2tm.game.behaviors;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class SimpleSelectLogicTest {

    @Test
    public void focus() {
        SimpleSelectLogic simpleSelectLogic = new SimpleSelectLogic();
        assertThat(simpleSelectLogic.hasFocus(), is(false));

        simpleSelectLogic.getsFocus();
        assertThat(simpleSelectLogic.hasFocus(), is(true));

        simpleSelectLogic.lostFocus();
        assertThat(simpleSelectLogic.hasFocus(), is(false));
    }

}