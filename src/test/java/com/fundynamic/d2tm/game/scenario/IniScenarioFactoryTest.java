package com.fundynamic.d2tm.game.scenario;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class IniScenarioFactoryTest extends AbstractD2TMTest {

    @Test
    public void foo() {
        IniScenarioFactory iniScenarioFactory = new IniScenarioFactory(shroud, mock(TerrainFactory.class), entitiesData, "test-scenario.ini") {
            @Override
            public EntityRepository getEntityRepository(Map map) throws SlickException {
                return entityRepository;
            }
        };

        // ACT
        Scenario scenario = iniScenarioFactory.create();

        // Assert
        EntityRepository entityRepository = scenario.getEntityRepository();
        int entitiesCount = entityRepository.getEntitiesCount();

        // 3 units, 1 structure
        Assert.assertEquals(4, entitiesCount);

    }
}