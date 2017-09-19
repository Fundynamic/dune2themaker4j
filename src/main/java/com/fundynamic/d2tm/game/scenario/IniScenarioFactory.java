package com.fundynamic.d2tm.game.scenario;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Faction;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrain;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.utils.StringUtils;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.newdawn.slick.SlickException;

import java.io.InputStream;

public class IniScenarioFactory extends ScenarioFactory {

    private String filename;

    public IniScenarioFactory(Shroud shroud, TerrainFactory terrainFactory, EntitiesData entitiesData, String filename) {
        super(shroud, terrainFactory, entitiesData);
        this.filename = filename;
    }

    @Override
    public Scenario create() {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/" + filename);
            if (resourceAsStream == null) throw new IllegalArgumentException("Unable to open file: " + filename);

            Ini ini = new Ini(resourceAsStream);

            Scenario.ScenarioBuilder builder = Scenario.builder();

            Map map = readMap(ini);
            builder.withMap(map);

            Player human = readHumanPlayer(ini);
            builder.withHuman(human);

            Player cpu = getCpuPlayer(ini);
            builder.withCpuPlayer(cpu);

            EntityRepository entityRepository = getEntityRepository(map);
            builder.withEntityRepository(entityRepository);

            readStructures(ini, human, cpu, entityRepository);
            readUnits(ini, human, cpu, entityRepository);

            return builder.build();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create Scenario from file: " + filename, e);
        }
    }

    public void readUnits(Ini ini, Player human, Player cpu, EntityRepository entityRepository) {
        Profile.Section sounds = ini.get("UNITS");
        String[] strings = sounds.childrenNames();
        for (String id : strings) {
            Profile.Section struct = sounds.getChild(id);
            /**
             * MapCoordinate=1,1
             Player=HUMAN
             Type=QUAD
             State=GUARD
             */
            Player playerToUse = getPlayer(human, cpu, struct);

            MapCoordinate coordinate = getMapCoordinate(struct);

            String type = getType(struct);

            entityRepository.placeUnitOnMap(coordinate, type, playerToUse);
        }
    }

    public void readStructures(Ini ini, Player human, Player cpu, EntityRepository entityRepository) {
        Profile.Section sounds = ini.get("STRUCTURES");
        String[] strings = sounds.childrenNames();
        for (String id : strings) {
            Profile.Section struct = sounds.getChild(id);

            Player playerToUse = getPlayer(human, cpu, struct);

            MapCoordinate mapCoordinate = getMapCoordinate(struct);
            String type = getType(struct);

            entityRepository.placeStructureOnMap(mapCoordinate, type, playerToUse);
        }
    }

    public String getType(Profile.Section struct) {
        String type = struct.get("Type", String.class, "");
        if (StringUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Unable to put entity " + struct + " on map, because it lacks a Type");
        }
        return type;
    }

    public MapCoordinate getMapCoordinate(Profile.Section struct) {
        return getMapCoordinate(struct, "MapCoordinate");
    }

    public MapCoordinate getMapCoordinate(Profile.Section struct, String propertyNameForMapCoordinate) {
        String mapCoordinate = struct.get(propertyNameForMapCoordinate, String.class, "");
        if (StringUtils.isEmpty(mapCoordinate)) {
            throw new IllegalArgumentException("Unable to put read ["+ propertyNameForMapCoordinate + "] for [" + struct + "]");
        }
        return MapCoordinate.fromString(mapCoordinate);
    }

    public Player getPlayer(Player human, Player cpu, Profile.Section struct) {
        String playerId = struct.get("Player", String.class, "HUMAN");
        Player playerToUse = human;
        if ("CPU".equalsIgnoreCase(playerId)) {
            playerToUse = cpu;
        }
        return playerToUse;
    }

    public Map readMap(Ini ini) throws SlickException {
        Profile.Section iniMap = ini.get("MAP");
        int mapWidth = iniMap.get("Width", Integer.class, -1);
        int mapHeight = iniMap.get("Height", Integer.class, -1);
        if (mapWidth < 1 || mapHeight < 1) {
            throw new IllegalArgumentException("Invalid map dimensions given [" + mapWidth+"x"+mapHeight + "], must be greater than 1x1.");
        }

        Map map = new Map(shroud, mapWidth, mapHeight);
        MapEditor mapEditor = new MapEditor(terrainFactory);
        mapEditor.fillMapWithTerrain(map, DuneTerrain.TERRAIN_SAND);

        return map;
    }

    public Player getCpuPlayer(Ini ini) {
        Player cpu = new Player("CPU", Faction.RED);
        Profile.Section iniHuman = ini.get("CPU");
        int startingCredits = iniHuman.get("Credits", Integer.class, 2000);
        cpu.setCredits(startingCredits);
        return cpu;
    }

    public Player readHumanPlayer(Ini ini) {
        Player human = new Player("Human", Faction.GREEN);
        Profile.Section iniHuman = ini.get("HUMAN");
        int startingCredits = iniHuman.get("Credits", Integer.class, 2000);
        MapCoordinate mapCoordinate = getMapCoordinate(iniHuman, "Focus");
        human.setFocusMapCoordinate(mapCoordinate);
        human.setCredits(startingCredits);

        return human;
    }

}
