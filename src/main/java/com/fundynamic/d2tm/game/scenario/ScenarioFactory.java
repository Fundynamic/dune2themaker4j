package com.fundynamic.d2tm.game.scenario;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import org.newdawn.slick.SlickException;

/**
 * Generates a Scenario to play
 */
public abstract class ScenarioFactory {

    protected final Shroud shroud;
    protected final TerrainFactory terrainFactory;
    protected final EntitiesData entitiesData;

    protected ScenarioFactory(Shroud shroud, TerrainFactory terrainFactory, EntitiesData entitiesData) {
        this.shroud = shroud;
        this.terrainFactory = terrainFactory;
        this.entitiesData = entitiesData;
    }

    public abstract Scenario create();

    public EntityRepository getEntityRepository(Map map) throws SlickException {
        return new EntityRepository(
                map,
                new Recolorer(),
                entitiesData
        );
    }
}
