package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import org.junit.Before;
import org.newdawn.slick.SlickException;

public abstract class AbstractMouseBehaviorTest extends AbstractD2TMTest {

    protected Mouse mouse;
    protected Player player;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        player = new Player("Stefan", Recolorer.FactionColor.BLUE);
        mouse = makeTestableMouse(player);
    }

}
