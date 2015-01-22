package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.input.Keyboard;
import com.fundynamic.d2tm.game.input.Mouse;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Random;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class PlayingState {

    private final TerrainFactory terrainFactory;

    private Map map;
    private Graphics graphics;
    private GameContainer gameContainer;

    private Keyboard keyboard;
    private Mouse mouse;


    private List<DrawableViewPort> drawableViewPorts = new ArrayList<DrawableViewPort>();

    private boolean initialized;


    public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.graphics = gameContainer.getGraphics();
        this.gameContainer = gameContainer;

        this.keyboard = new Keyboard(gameContainer.getInput());
        this.mouse = new Mouse(gameContainer.getInput());

        // on map load...
        this.map = new Map(terrainFactory, 64, 64);
    }

    public void init() throws SlickException {
        // HACK HACK: we call init in render, and we only want to do it once so we check here. Ugly because now we check
        // every iteration if we should do this...
        if (!initialized) {
            this.map.init();
            initialized = true;

            try {
                Vector2D viewPortDrawingPosition = new Vector2D(0,0);
                final Viewport newViewport;
                newViewport = new Viewport(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, this.map);
                drawableViewPorts.add(new DrawableViewPort(newViewport, viewPortDrawingPosition, new Vector2D(Random.getRandomBetween(0, 2048), Random.getRandomBetween(0, 2048))));
            } catch (SlickException e) {
                throw new IllegalStateException("Unable to create new viewport!");
            }
        }
    }

    public void update() {
        for (DrawableViewPort drawableViewPort : drawableViewPorts) {
            drawableViewPort.update();
        }

        if (keyboard.isEscPressed()) {
            this.gameContainer.exit();
        }
    }

    public void render() throws SlickException {
        for (DrawableViewPort drawableViewPort : drawableViewPorts) {
            drawableViewPort.render();
        }
        this.graphics.drawString("Drawing " + drawableViewPorts.size() + " viewports.", 10, 30);
    }

    private class DrawableViewPort {
        private final Vector2D viewPortDrawingPosition;
        private Vector2D viewPortViewingPosition;
        private final Viewport viewport;

        private DrawableViewPort(Viewport viewport, Vector2D viewPortDrawingPosition, Vector2D viewPortViewingPosition) {
            this.viewPortDrawingPosition = viewPortDrawingPosition;
            this.viewPortViewingPosition = viewPortViewingPosition;
            this.viewport = viewport;
        }

        void render() throws SlickException {
            viewport.draw(graphics, viewPortDrawingPosition, viewPortViewingPosition);
        }

        void update() {
            // use events?
            if (keyboard.isKeyUpPressed()) {
                viewPortViewingPosition = viewPortViewingPosition.moveUp();
            }
            if (keyboard.isKeyDownPressed()) {
                viewPortViewingPosition = viewPortViewingPosition.moveDown();
            }
            if (keyboard.isKeyLeftPressed()) {
                viewPortViewingPosition = viewPortViewingPosition.moveLeft();
            }
            if (keyboard.isKeyRightPressed()) {
                viewPortViewingPosition = viewPortViewingPosition.moveRight();
            }
        }
    }

}
