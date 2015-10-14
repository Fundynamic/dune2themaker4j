package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.AbstractD2TMTest;
import org.junit.Before;
import org.newdawn.slick.SlickException;

public abstract class AbstractMouseBehaviorTest extends AbstractD2TMTest {

    protected Mouse mouse;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        mouse = makeTestableMouse(player);
    }

}
