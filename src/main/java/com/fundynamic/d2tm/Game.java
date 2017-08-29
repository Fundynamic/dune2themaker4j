package com.fundynamic.d2tm;

import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader;
import com.fundynamic.d2tm.game.scenario.RandomMapScenarioFactory;
import com.fundynamic.d2tm.game.scenario.Scenario;
import com.fundynamic.d2tm.game.scenario.ScenarioFactory;
import com.fundynamic.d2tm.game.state.PlayingState;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.StringUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Bootstrap;

import java.util.Arrays;
import java.util.List;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;


public class Game extends StateBasedGame {

    public static final String GAME_TITLE = "Dune II - The Maker";

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
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

    public String mapFileName = "";

    public Game(String title, String mapFileName) {
        super(title);
        this.mapFileName = mapFileName;
        if (StringUtils.isEmpty(mapFileName)) {
            System.out.println("Starting game with random generated map.");
        } else {
            System.out.println("Starting game with loading map: " + mapFileName);
        }
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

        Shroud shroud = new Shroud(
                imageRepository.loadAndCache("shroud_edges.png"),
                TILE_SIZE
        );

        ScenarioFactory scenarioFactory = new RandomMapScenarioFactory(
                shroud,
                terrainFactory,
                new EntitiesDataReader().fromRulesIni()
        );

        PlayingState playingState = new PlayingState(
                container,
                imageRepository,
                scenarioFactory
        );

        SoundStore.get().setSoundVolume(0.2f);
        SoundStore.get().setMusicVolume(0.5f);

        addState(playingState);
    }

    /**
     * Main entry point
     * @param args
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);
        RECORDING_VIDEO = argsList.contains("recording");
        DEBUG_INFO = argsList.contains("debug");
        FULLSCREEN = argsList.contains("fullscreen");

        String mapFileName = "";
        for (String arg : argsList) {
            if (arg.startsWith("map:")) {
                mapFileName = arg.substring(4);
            }
        }

        Bootstrap.runAsApplication(
                new Game(GAME_TITLE, mapFileName),
                SCREEN_WIDTH,
                SCREEN_HEIGHT,
                FULLSCREEN
            );
    }

}
