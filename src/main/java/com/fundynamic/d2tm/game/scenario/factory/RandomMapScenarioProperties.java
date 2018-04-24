package com.fundynamic.d2tm.game.scenario.factory;

import com.fundynamic.d2tm.utils.StringUtils;

public class RandomMapScenarioProperties {
    public static final int DEFAULT_MAP_DIMENSION = 64;
    public static final int DEFAULT_CREDITS = 2000;

    private int mapWidth;
    private int mapHeight;
    private int humanCredits;
    private int cpuCredits;

    public RandomMapScenarioProperties() {
        mapWidth = DEFAULT_MAP_DIMENSION;
        mapHeight = DEFAULT_MAP_DIMENSION;
        humanCredits = DEFAULT_CREDITS;
        cpuCredits = DEFAULT_CREDITS;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getHumanCredits() {
        return humanCredits;
    }

    public int getCpuCredits() {
        return cpuCredits;
    }

    public static RandomMapScenarioProperties fromString(String propertiesString) {
        RandomMapScenarioProperties randomMapScenarioProperties = new RandomMapScenarioProperties();
        if (StringUtils.isEmpty(propertiesString)) return randomMapScenarioProperties;
        String[] parts = propertiesString.split(";");
        for (String part : parts) {
            String[] keyValPair = part.split("=");
            if (keyValPair.length < 2) continue;

            String key = keyValPair[0];
            String val = keyValPair[1];
            // read map property
            if ("map".equalsIgnoreCase(key)) {
                String[] widthHeight = val.split("x");
                randomMapScenarioProperties.mapWidth = StringUtils.parseIntOrDefault(widthHeight[0], DEFAULT_MAP_DIMENSION);
                randomMapScenarioProperties.mapHeight = StringUtils.parseIntOrDefault(widthHeight[1], DEFAULT_MAP_DIMENSION);
            }
            if ("human".equalsIgnoreCase(key)) {
                randomMapScenarioProperties.humanCredits = StringUtils.parseIntOrDefault(val, DEFAULT_CREDITS);
            }
            if ("cpu".equalsIgnoreCase(key)) {
                randomMapScenarioProperties.cpuCredits = StringUtils.parseIntOrDefault(val, DEFAULT_CREDITS);
            }
        }
        return randomMapScenarioProperties;
    }

    @Override
    public String toString() {
        return "RandomMapScenarioProperties{" +
                "mapWidth=" + mapWidth +
                ", mapHeight=" + mapHeight +
                ", humanCredits=" + humanCredits +
                ", cpuCredits=" + cpuCredits +
                '}';
    }
}
