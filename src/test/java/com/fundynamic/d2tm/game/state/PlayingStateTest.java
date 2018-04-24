package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.scenario.factory.RandomMapScenarioFactory;
import com.fundynamic.d2tm.game.scenario.factory.RandomMapScenarioProperties;
import com.fundynamic.d2tm.game.scenario.factory.AbstractScenarioFactory;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class PlayingStateTest extends AbstractD2TMTest {

    private PlayingState playingState;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(mock(Image.class), TILE_SIZE));
        Shroud shroud = new Shroud(mock(Image.class), TILE_SIZE);

        final Map originalMap = map;
        AbstractScenarioFactory abstractScenarioFactory = new RandomMapScenarioFactory(shroud, terrainFactory, entitiesData, new RandomMapScenarioProperties()) {

            @Override
            public Map getMap(MapEditor mapEditor, int mapWidth, int mapHeight) {
                return originalMap;
            }

            @Override
            public EntityRepository getEntityRepository(Map map) throws SlickException {
                return entityRepository;
            }
        };

        playingState = new PlayingState(gameContainer, imageRepository, abstractScenarioFactory) {
            @Override
            public BattleField makeBattleField(Player human, Mouse mouse) {
                return battleField;
            }
        };

        StateBasedGame stateBasedGame = mock(StateBasedGame.class);

        playingState.init(gameContainer, stateBasedGame);
    }

    @Test
    public void updateRemovesDestroyedEntities() throws SlickException {
        StateBasedGame game = mock(StateBasedGame.class);
        Unit unit = makeUnit(player);
        int originalCount = entityRepository.allUnits().size();

        playingState.update(gameContainer, game, 10);

        assertThat(entityRepository.allUnits().size(), is(originalCount));

        unit.takeDamage(unit.getHitPoints(), null); // takes damage, so it gets destroyed

        playingState.update(gameContainer, game, 10);

        assertThat(unit.isDestroyed(), is(true));
        assertThat(entityRepository.allUnits().size(), is(originalCount - 1));
    }
}