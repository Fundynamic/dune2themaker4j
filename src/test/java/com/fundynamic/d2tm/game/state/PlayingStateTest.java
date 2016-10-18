package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PlayingStateTest extends AbstractD2TMTest {

    private PlayingState playingState;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(mock(Image.class), TILE_SIZE));
        Shroud shroud = new Shroud(mock(Image.class), TILE_SIZE);

        final Map originalMap = map;

        playingState = new PlayingState(gameContainer, terrainFactory, imageRepository, shroud, TILE_SIZE) {
            @Override
            public EntityRepository createEntityRepository() throws SlickException {
                return entityRepository;
            }

            @Override
            public Map getMap() {
                return map;
            }

            @Override
            public MapEditor getMapEditor() {
                return new MapEditor(terrainFactory) {
                    @Override
                    public Map generateRandom(Map map) {
                        return originalMap;
                    }
                };
            }

            @Override
            public BattleField makeBattleField(Player human, Mouse mouse) {
                return battleField;
            }
        };

        StateBasedGame stateBasedGame = mock(StateBasedGame.class);

        playingState.init(gameContainer, stateBasedGame);
    }


    @Test
    public void testInitInitialGame() throws SlickException {
        Player cpu = new Player("cpu", Recolorer.FactionColor.BLUE);
        Player human = new Player("human", Recolorer.FactionColor.BLUE);

        playingState.initializeMap(entityRepository, human, cpu);
    }

    @Test
    public void rendersViewports() throws SlickException {
        StateBasedGame game = mock(StateBasedGame.class);
        Graphics graphics = mock(Graphics.class);
        Font font = mock(Font.class);

        when(graphics.getFont()).thenReturn(font);

        playingState.render(gameContainer, game, graphics);
    }

    @Test
    public void updateRemovesDestroyedEntities() throws SlickException {
        StateBasedGame game = mock(StateBasedGame.class);
        Unit unit = makeUnit(player);
        int originalCount = entityRepository.getEntitiesCount();

        // keep unit alive
        playingState.update(gameContainer, game, 10);
        assertThat(entityRepository.getEntitiesCount(), is(originalCount));

        unit.takeDamage(unit.getHitPoints(), null); // takes damage, so it gets destroyed
        unit.update(1);       // to dying
        unit.update(1);       // now dead (is Destroyed == true)
        assertThat(unit.isDestroyed(), is(true));

        playingState.update(gameContainer, game, 10);
        assertThat(entityRepository.getEntitiesCount(), is(originalCount - 1));
    }
}