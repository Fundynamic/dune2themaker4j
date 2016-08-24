package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import org.newdawn.slick.SlickException;

public class TestableEntityRepository extends EntityRepository {

    public TestableEntityRepository(Map map, Recolorer recolorer, EntitiesData entitiesData) throws SlickException {
        super(map, recolorer, entitiesData);
    }

}
