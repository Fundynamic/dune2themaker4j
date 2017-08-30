package com.fundynamic.d2tm.game.scenario;

import com.fundynamic.d2tm.Game;
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
import org.newdawn.slick.SlickException;

public class RandomMapScenarioFactory extends ScenarioFactory {

    public RandomMapScenarioFactory(Shroud shroud, TerrainFactory terrainFactory, EntitiesData entitiesData) {
        super(shroud, terrainFactory, entitiesData);
    }

    @Override
    public Scenario create() {
        try {
            Scenario.ScenarioBuilder builder = Scenario.builder();

            MapEditor mapEditor = new MapEditor(terrainFactory);

            Player human = new Player("Human", Faction.GREEN);
            Player cpu = new Player("CPU", Faction.RED);

            builder.withHuman(human);
            builder.withCpuPlayer(cpu);

            if (Game.RECORDING_VIDEO) {
                human.setCredits(2200);
            } else {
                human.setCredits(3000);
            }

            cpu.setCredits(2000);

            Map map = getMap(mapEditor);

            MapCoordinate playerConstyard = MapCoordinate.create(5, 5);
            MapCoordinate cpuConstyard = MapCoordinate.create(57, 57);

            // create spice field nearby
            mapEditor.createCircularField(map, MapCoordinate.create(15, 15), DuneTerrain.TERRAIN_SPICE, 5);
            mapEditor.createCircularField(map, playerConstyard, DuneTerrain.TERRAIN_ROCK, 5);
            mapEditor.createCircularField(map, cpuConstyard, DuneTerrain.TERRAIN_ROCK, 5);
            mapEditor.smooth(map);

            builder.withMap(map);

            EntityRepository entityRepository = getEntityRepository(map);

            builder.withEntityRepository(entityRepository);
            entityRepository.placeStructureOnMap(playerConstyard, EntitiesData.CONSTRUCTION_YARD, human);
            entityRepository.placeStructureOnMap(cpuConstyard, EntitiesData.CONSTRUCTION_YARD, cpu);

            return builder.build();
        } catch (SlickException e) {
            throw new IllegalStateException("Unable to create random scenario, reason:", e);
        }
    }

    public Map getMap(MapEditor mapEditor) {
        return mapEditor.generateRandom(shroud, 128, 128);
    }
}
