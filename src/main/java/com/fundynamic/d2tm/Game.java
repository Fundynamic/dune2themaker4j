package com.fundynamic.d2tm;

import com.fundynamic.d2tm.game.PlayingState;
import com.fundynamic.d2tm.game.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.*;


public class Game extends BasicGame {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private PlayingState playingState;

    public Game() {
        super("Dune II - The Maker");
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        playingState.init();
        playingState.render();
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        try {
            Theme theme = new Theme(new Image("sheet_terrain.png"));
            TerrainFactory terrainFactory = new DuneTerrainFactory(theme);
            playingState = new PlayingState(gameContainer, terrainFactory);
            // calling init here does not work with images
        } catch (Exception e) {
            throw new SlickException("Exception occurred while initializing game.", e);
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        playingState.update();
    }
    
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
        app.setForceExit(true);
        app.start();
    }

}
