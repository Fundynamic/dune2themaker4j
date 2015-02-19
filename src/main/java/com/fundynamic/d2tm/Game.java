package com.fundynamic.d2tm;

import com.fundynamic.d2tm.game.state.PlayingState;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Bootstrap;


public class Game extends StateBasedGame {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    public Game(String title) {
        super(title);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        DuneTerrainFactory terrainFactory = new DuneTerrainFactory(
                new Theme(
                        new Image("sheet_terrain.png"),
                        TILE_WIDTH,
                        TILE_HEIGHT
                ),
                TILE_WIDTH,
                TILE_HEIGHT
        );

        PlayingState playingState = new PlayingState(
                container,
                terrainFactory,
                new Shroud(
                    new Image("shroud_edges.png"),
                    TILE_WIDTH,
                    TILE_HEIGHT
                ),
                TILE_WIDTH,
                TILE_HEIGHT
        );

        addState(playingState);
    }

    public static void main(String[] args) {
        Bootstrap.runAsApplication(new Game("Dune II - The Maker"), SCREEN_WIDTH, SCREEN_HEIGHT, false);
    }

}
