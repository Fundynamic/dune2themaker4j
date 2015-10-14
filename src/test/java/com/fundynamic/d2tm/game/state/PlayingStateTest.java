package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PlayingStateTest extends AbstractD2TMTest {

    private PlayingState playingState;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(mock(Image.class), TILE_SIZE, TILE_SIZE));
        Shroud shroud = new Shroud(mock(Image.class), TILE_SIZE, TILE_SIZE);

        playingState = new PlayingState(gameContainer, terrainFactory, imageRepository, shroud, TILE_SIZE, TILE_SIZE);
    }

    @Test
    public void testInit() throws SlickException {
        int tileWidth = 32;
        int tileHeight = 32;

        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(mock(Image.class), tileWidth, tileHeight));
        Shroud shroud = new Shroud(mock(Image.class), tileWidth, tileHeight);

        PlayingState playingState = new PlayingState(gameContainer, terrainFactory, imageRepository, shroud, tileWidth, tileHeight) {
            @Override
            public EntityRepository createEntityRepository(Map map) throws SlickException {
                return getTestableEntityRepository();
            }
        };

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