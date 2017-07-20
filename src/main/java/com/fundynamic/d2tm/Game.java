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

import java.util.Arrays;
import java.util.List;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;


public class Game extends StateBasedGame {

    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    public static final boolean SHOW_FPS = false;
    public static final boolean VSYNC = true;

    public static boolean DEBUG_INFO = false;
    // if 'recording' is passed as argument then the game will use 'demo settings' to make it run faster
    public static boolean RECORDING_VIDEO = false;
    // if 'fullscreen' is passed as argument then the game will be rendered in fullscreen
    public static boolean FULLSCREEN = false;

    public static Vector2D getResolution() {
        return Vector2D.create(SCREEN_WIDTH, SCREEN_HEIGHT);
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
                        TILE_SIZE
                )
        );

        container.setShowFPS(SHOW_FPS);
        container.setVSync(VSYNC);

        PlayingState playingState = new PlayingState(
                container,
                terrainFactory,
                imageRepository,
                new Shroud(
                    imageRepository.loadAndCache("shroud_edges.png"),
                        TILE_SIZE
                )
        );

        addState(playingState);
    }

    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);
        RECORDING_VIDEO = argsList.contains("recording");
        DEBUG_INFO = argsList.contains("debug");
        FULLSCREEN = argsList.contains("fullscreen");

        Bootstrap.runAsApplication(new Game("Dune II - The Maker"), SCREEN_WIDTH, SCREEN_HEIGHT, FULLSCREEN);
    }

}
