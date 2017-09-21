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
import org.newdawn.slick.SlickException;

public class RandomMapScenarioFactory extends AbstractScenarioFactory {

    private RandomMapScenarioProperties randomMapScenarioProperties;

    public RandomMapScenarioFactory(Shroud shroud, TerrainFactory terrainFactory, EntitiesData entitiesData, RandomMapScenarioProperties randomMapScenarioProperties) {
        super(shroud, terrainFactory, entitiesData);
        this.randomMapScenarioProperties = randomMapScenarioProperties;
    }

    @Override
    public Scenario create() {
        try {
            Scenario.ScenarioBuilder builder = Scenario.builder();

            Player human = new Player("Human", Faction.GREEN);
            Player cpu = new Player("CPU", Faction.RED);

            builder.withHuman(human);
            builder.withCpuPlayer(cpu);

            human.setCredits(randomMapScenarioProperties.getHumanCredits());
            cpu.setCredits(randomMapScenarioProperties.getCpuCredits());

            MapEditor mapEditor = new MapEditor(terrainFactory);
            int mapWidth = randomMapScenarioProperties.getMapWidth();
            int mapHeight = randomMapScenarioProperties.getMapHeight();
            Map map = getMap(mapEditor, mapWidth, mapHeight);

            MapCoordinate playerConstyard = MapCoordinate.create(5, 5);
            MapCoordinate cpuConstyard = MapCoordinate.create(mapWidth - 5, mapHeight - 5);

            // create spice field nearby
            mapEditor.createCircularField(map, playerConstyard.add(MapCoordinate.create(7,7)), DuneTerrain.TERRAIN_SPICE, 5);
            mapEditor.createCircularField(map, playerConstyard, DuneTerrain.TERRAIN_ROCK, 5);

            mapEditor.createCircularField(map, cpuConstyard, DuneTerrain.TERRAIN_ROCK, 5);
            mapEditor.createCircularField(map, cpuConstyard.min(MapCoordinate.create(7,7)), DuneTerrain.TERRAIN_SPICE, 5);

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

    public Map getMap(MapEditor mapEditor, int mapWidth, int mapHeight) {
        return mapEditor.generateRandom(shroud, mapWidth, mapHeight);
    }
}
