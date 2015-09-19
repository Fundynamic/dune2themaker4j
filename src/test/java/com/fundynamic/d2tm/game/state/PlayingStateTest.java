package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PlayingStateTest {

    private static int TILE_WIDTH = 32;
    private static int TILE_HEIGHT = 32;
    private PlayingState playingState;

    @Mock
    private GameContainer gameContainer;

    @Before
    public void setUp() throws SlickException {
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(mock(Image.class), TILE_WIDTH, TILE_HEIGHT));
        Shroud shroud = new Shroud(mock(Image.class), TILE_WIDTH, TILE_HEIGHT);

        playingState = new PlayingState(gameContainer, terrainFactory, shroud, TILE_WIDTH, TILE_HEIGHT);
    }

    @Test
    @Ignore("fails due some dependencies set up that are not stub/mockable yet")
    public void testInit() throws SlickException {
        int tileWidth = 32;
        int tileHeight = 32;

        GameContainer gameContainer = mock(GameContainer.class);
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(mock(Image.class), tileWidth, tileHeight));
        Shroud shroud = new Shroud(mock(Image.class), tileWidth, tileHeight);

        PlayingState playingState = new PlayingState(gameContainer, terrainFactory, shroud, tileWidth, tileHeight);

        StateBasedGame stateBasedGame = mock(StateBasedGame.class);

        playingState.init(gameContainer, stateBasedGame);
    }

    @Test
    public void testInitInitialGame() throws SlickException {
        EntityRepository entityRepository = mock(EntityRepository.class);
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

        EntityRepository entityRepository = mock(EntityRepository.class);
        Player cpu = new Player("cpu", Recolorer.FactionColor.BLUE);
        Player human = new Player("human", Recolorer.FactionColor.BLUE);

        playingState.initializeMap(entityRepository, human, cpu);

        playingState.render(gameContainer, game, graphics);
    }
}