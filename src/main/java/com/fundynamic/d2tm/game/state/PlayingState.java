package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.drawing.Viewport;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.event.ViewportMovementListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class PlayingState {

    private final TerrainFactory terrainFactory;

    private Map map;
    private Graphics graphics;

    private List<Viewport> viewports = new ArrayList<>();

    private boolean initialized;
    private final Input input;
    private Vector2D<Integer> screenResolution;


    public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.graphics = gameContainer.getGraphics();

        input = gameContainer.getInput();
        input.addKeyListener(new QuitGameKeyListener(gameContainer));

        // on map load...
        this.map = new Map(terrainFactory, 64, 64);

        this.screenResolution = new Vector2D<>(gameContainer.getWidth(), gameContainer.getHeight());
    }

    public void init() throws SlickException {
        // HACK HACK: we call init in render, and we only want to do it once so we check here. Ugly because now we check
        // every iteration if we should do this...
        if (!initialized) {
            this.map.init();
            initialized = true;

            try {
                float moveSpeed = 16.0F;
                Vector2D viewportDrawingPosition = Vector2D.zero();
                Viewport viewport = new Viewport(screenResolution, viewportDrawingPosition, Vector2D.zero(), graphics, this.map, moveSpeed);

                // Add listener for this viewport
                input.addMouseListener(new ViewportMovementListener(viewport, screenResolution));

                viewports.add(viewport);
            } catch (SlickException e) {
                throw new IllegalStateException("Unable to create new viewport!", e);
            }
        }
    }

    public void update() {
        for (Viewport viewport : viewports) {
            viewport.update();
        }
    }

    public void render() throws SlickException {
        for (Viewport viewport : viewports) {
            viewport.render();
        }
    }

}