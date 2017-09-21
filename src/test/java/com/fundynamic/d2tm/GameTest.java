package com.fundynamic.d2tm;

import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.scenario.AbstractScenarioFactory;
import com.fundynamic.d2tm.game.scenario.IniScenarioFactory;
import com.fundynamic.d2tm.game.scenario.RandomMapScenarioFactory;
import com.fundynamic.d2tm.graphics.ImageRepository;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GameTest {

    @Test
    public void returnsRandomMapScenarioFactoryWithNoFileName() {
        Game myGame = new Game("my Game", "");

        AbstractScenarioFactory scenarioFactory = myGame.createScenarioFactory(mock(ImageRepository.class), new EntitiesData());

        Assert.assertTrue(scenarioFactory instanceof RandomMapScenarioFactory);
    }

    @Test
    public void returnsExpectedScenarioFactory() {
        Game myGame = new Game("my Game", "my-awesome-map.ini");

        AbstractScenarioFactory scenarioFactory = myGame.createScenarioFactory(mock(ImageRepository.class), new EntitiesData());

        Assert.assertTrue(scenarioFactory instanceof IniScenarioFactory);
    }
}