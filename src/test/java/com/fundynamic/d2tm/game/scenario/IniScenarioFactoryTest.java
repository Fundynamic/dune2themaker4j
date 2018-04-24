package com.fundynamic.d2tm.game.scenario;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.scenario.factory.IniScenarioFactory;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.mock;

public class IniScenarioFactoryTest extends AbstractD2TMTest {

    @Test
    public void smokeTest() {
        IniScenarioFactory iniScenarioFactory = new IniScenarioFactory(shroud, mock(TerrainFactory.class), entitiesData, "test-scenario.ini") {
            @Override
            public EntityRepository getEntityRepository(Map map) throws SlickException {
                return entityRepository;
            }

            @Override
            public Map makeMap(MapEditor mapEditor, int mapWidth, int mapHeight) throws SlickException {
                return IniScenarioFactoryTest.this.makeMap(mapWidth, mapHeight);
            }
        };

        // ACT
        Scenario scenario = iniScenarioFactory.create();

        // Assert
        EntityRepository entityRepository = scenario.getEntityRepository();
        int entitiesCount = entityRepository.getEntitiesCount();

        // 3 units, 1 structure
        Assert.assertEquals(4, entitiesCount);

        Map map = scenario.getMap();
        Assert.assertEquals(64, map.getWidth());
        Assert.assertEquals(64, map.getHeight());

    }
}