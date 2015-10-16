package com.fundynamic.d2tm;

import com.fundynamic.d2tm.game.state.PlayingState;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Bootstrap;


public class Game extends StateBasedGame {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int TILE_SIZE = 32;

    public static Vector2D getResolution() {
        return new Vector2D(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public Game(String title) {
        super(title);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        ImageRepository imageRepository = new ImageRepository();
        DuneTerrainFactory terrainFactory = new DuneTerrainFactory(
                new Theme(
                        imageRepository.loadAndCache("sheet_terrain.png"),
                        TILE_SIZE,
                        TILE_SIZE
                )
        );

        container.setVSync(true);

        PlayingState playingState = new PlayingState(
                container,
                terrainFactory,
                imageRepository,
                new Shroud(
                    imageRepository.loadAndCache("shroud_edges.png"),
                        TILE_SIZE,
                        TILE_SIZE
                ),
                TILE_SIZE,
                TILE_SIZE
        );

        addState(playingState);
    }

    public static void main(String[] args) {
        Bootstrap.runAsApplication(new Game("Dune II - The Maker"), SCREEN_WIDTH, SCREEN_HEIGHT, false);
    }

}
