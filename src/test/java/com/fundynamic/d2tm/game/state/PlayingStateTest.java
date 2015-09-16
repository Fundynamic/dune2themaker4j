package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class PlayingStateTest {

    @Test
    public void instantiates() throws SlickException {
        int tileWidth = 32;
        int tileHeight = 32;

        GameContainer gameContainer = Mockito.mock(GameContainer.class);
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(Mockito.mock(Image.class), tileWidth, tileHeight));
        Shroud shroud = new Shroud(Mockito.mock(Image.class), tileWidth, tileHeight);

        new PlayingState(gameContainer, terrainFactory, shroud, tileWidth, tileHeight);
    }

    @Test
    @Ignore("fails due some dependencies set up that are not stub/mockable yet")
    public void testInit() throws SlickException {
        int tileWidth = 32;
        int tileHeight = 32;

        GameContainer gameContainer = Mockito.mock(GameContainer.class);
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(Mockito.mock(Image.class), tileWidth, tileHeight));
        Shroud shroud = new Shroud(Mockito.mock(Image.class), tileWidth, tileHeight);

        PlayingState playingState = new PlayingState(gameContainer, terrainFactory, shroud, tileWidth, tileHeight);

        StateBasedGame stateBasedGame = Mockito.mock(StateBasedGame.class);

        playingState.init(gameContainer, stateBasedGame);
    }

    @Test
    public void testInitInitialGame() throws SlickException {
        int tileWidth = 32;
        int tileHeight = 32;

        GameContainer gameContainer = Mockito.mock(GameContainer.class);
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(Mockito.mock(Image.class), tileWidth, tileHeight));
        Shroud shroud = new Shroud(Mockito.mock(Image.class), tileWidth, tileHeight);

        PlayingState playingState = new PlayingState(gameContainer, terrainFactory, shroud, tileWidth, tileHeight);

        EntityRepository entityRepository = Mockito.mock(EntityRepository.class);
        Map map = new Map(shroud, 64, 64);
        Player cpu = new Player("cpu", Recolorer.FactionColor.BLUE);
        Player human = new Player("human", Recolorer.FactionColor.BLUE);

        playingState.initInitialGame(entityRepository, map, human, cpu);
    }

}