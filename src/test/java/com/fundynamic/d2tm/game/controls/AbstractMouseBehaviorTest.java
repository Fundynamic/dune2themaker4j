package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import org.junit.Before;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.controls.MouseTest.makeTestableMouse;
import static com.fundynamic.d2tm.game.map.MapTest.makeMap;

public abstract class AbstractMouseBehaviorTest {

    protected Mouse mouse;
    protected Player player;
    protected Map map;
    protected EntityRepository entityRepository;

    @Before
    public void setUp() throws SlickException {
        player = new Player("Stefan", Recolorer.FactionColor.BLUE);
        map = makeMap();
        mouse = makeTestableMouse(map, player);
        entityRepository = mouse.getEntityRepository();
    }

}
