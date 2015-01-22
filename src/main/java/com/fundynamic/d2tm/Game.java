package com.fundynamic.d2tm;

import com.fundynamic.d2tm.game.PlayingState;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.*;
import org.newdawn.slick.util.Bootstrap;


public class Game extends BasicGame {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private PlayingState playingState;

    public Game(String title) {
        super(title);
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

    public static void main(String[] args) {
        Bootstrap.runAsApplication(new Game("Dune II - The Maker"), SCREEN_WIDTH, SCREEN_HEIGHT, false);
    }

}
