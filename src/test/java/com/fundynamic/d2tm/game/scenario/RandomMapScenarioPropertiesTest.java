package com.fundynamic.d2tm.game.scenario;

import org.junit.Assert;
import org.junit.Test;

public class RandomMapScenarioPropertiesTest {

    @Test
    public void fromStringSmokeTest() {
        RandomMapScenarioProperties randomMapScenarioProperties = RandomMapScenarioProperties.fromString("map=32x45;human=1000;cpu=2000");
        Assert.assertEquals(32, randomMapScenarioProperties.getMapWidth());
        Assert.assertEquals(45, randomMapScenarioProperties.getMapHeight());
        Assert.assertEquals(1000, randomMapScenarioProperties.getHumanCredits());
        Assert.assertEquals(2000, randomMapScenarioProperties.getCpuCredits());
    }

    @Test
    public void fromStringWithOnlyMapProperty() {
        RandomMapScenarioProperties randomMapScenarioProperties = RandomMapScenarioProperties.fromString("map=32x45");
        Assert.assertEquals(32, randomMapScenarioProperties.getMapWidth());
        Assert.assertEquals(45, randomMapScenarioProperties.getMapHeight());

        // defaults
        Assert.assertEquals(2000, randomMapScenarioProperties.getHumanCredits());
        Assert.assertEquals(2000, randomMapScenarioProperties.getCpuCredits());
    }

    @Test
    public void fromStringMapPropertyIsCaseInsensitive() {
        RandomMapScenarioProperties randomMapScenarioProperties = RandomMapScenarioProperties.fromString("MaP=32x45");
        Assert.assertEquals(32, randomMapScenarioProperties.getMapWidth());
        Assert.assertEquals(45, randomMapScenarioProperties.getMapHeight());
    }

    @Test
    public void fromStringWithBullCrap() {
        RandomMapScenarioProperties randomMapScenarioProperties = RandomMapScenarioProperties.fromString("foo=bar;baz=zoo;;;dsa;;dsa;dsa;d;;sad;;");
        // defaults
        Assert.assertEquals(64, randomMapScenarioProperties.getMapWidth());
        Assert.assertEquals(64, randomMapScenarioProperties.getMapHeight());
    }

    @Test
    public void fromStringWithNoProperties() {
        RandomMapScenarioProperties randomMapScenarioProperties = RandomMapScenarioProperties.fromString("");
        Assert.assertEquals(64, randomMapScenarioProperties.getMapWidth());
        Assert.assertEquals(64, randomMapScenarioProperties.getMapHeight());
        Assert.assertEquals(2000, randomMapScenarioProperties.getHumanCredits());
        Assert.assertEquals(2000, randomMapScenarioProperties.getCpuCredits());

        randomMapScenarioProperties = RandomMapScenarioProperties.fromString(null);
        Assert.assertEquals(64, randomMapScenarioProperties.getMapWidth());
        Assert.assertEquals(64, randomMapScenarioProperties.getMapHeight());
        Assert.assertEquals(2000, randomMapScenarioProperties.getHumanCredits());
        Assert.assertEquals(2000, randomMapScenarioProperties.getCpuCredits());
    }

    @Test
    public void fromStringWithUnParsableMapDimensionsFallsBackToDefaults() {
        RandomMapScenarioProperties randomMapScenarioProperties = RandomMapScenarioProperties.fromString("map=foox23");
        Assert.assertEquals(64, randomMapScenarioProperties.getMapWidth());
        Assert.assertEquals(23, randomMapScenarioProperties.getMapHeight());

        randomMapScenarioProperties = RandomMapScenarioProperties.fromString("map=23xbar");
        Assert.assertEquals(23, randomMapScenarioProperties.getMapWidth());
        Assert.assertEquals(64, randomMapScenarioProperties.getMapHeight());
    }

    @Test
    public void fromStringWithUnParsableHumanOrCpuCreditsFallsBackToDefaults() {
        RandomMapScenarioProperties randomMapScenarioProperties = RandomMapScenarioProperties.fromString("human=1000;cpu=fooz");
        Assert.assertEquals(1000, randomMapScenarioProperties.getHumanCredits());
        Assert.assertEquals(2000, randomMapScenarioProperties.getCpuCredits());

        randomMapScenarioProperties = RandomMapScenarioProperties.fromString("human=#@;cpu=1500");
        Assert.assertEquals(2000, randomMapScenarioProperties.getHumanCredits());
        Assert.assertEquals(1500, randomMapScenarioProperties.getCpuCredits());
    }

    @Test
    public void fromStringCreditPropertiesAreCaseInsensitive() {
        RandomMapScenarioProperties randomMapScenarioProperties = RandomMapScenarioProperties.fromString("HuMaN=1000;CpU=2321");
        Assert.assertEquals(1000, randomMapScenarioProperties.getHumanCredits());
        Assert.assertEquals(2321, randomMapScenarioProperties.getCpuCredits());
    }

}