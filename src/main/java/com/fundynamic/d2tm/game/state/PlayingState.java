package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Random;
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

    private List<DrawableViewPort> drawableViewPorts = new ArrayList<>();

    private boolean initialized;


    public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.graphics = gameContainer.getGraphics();

        Input input = gameContainer.getInput();
        input.addKeyListener(new QuitGameKeyListener(gameContainer));

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
    }

    public void render() throws SlickException {
        for (DrawableViewPort drawableViewPort : drawableViewPorts) {
            drawableViewPort.render();
        }
        this.graphics.drawString("Drawing " + drawableViewPorts.size() + " viewports.", 10, 30);
    }

    private class DrawableViewPort {
        private final Vector2D drawingVector;
        private Vector2D<Float> viewingVector;
        private final Viewport viewport;
        private float xVelocity;
        private float yVelocity;

        private DrawableViewPort(Viewport viewport, Vector2D drawingVector, Vector2D viewingVector) {
            this.drawingVector = drawingVector;
            this.viewingVector = viewingVector;
            this.viewport = viewport;
            this.xVelocity = 0F;
            this.yVelocity = 0F;
        }

        void render() throws SlickException {
            viewport.draw(graphics, drawingVector, viewingVector);
        }

        void update() {
            // use events?
            viewingVector = viewingVector.move(xVelocity, yVelocity, 0.5F);

//            if (keyboard.isKeyUpPressed()) {
//                viewingVector = viewingVector.moveUp();
//            }
//            if (keyboard.isKeyDownPressed()) {
//                viewingVector = viewingVector.moveDown();
//            }
//            if (keyboard.isKeyLeftPressed()) {
//                viewingVector = viewingVector.moveLeft();
//            }
//            if (keyboard.isKeyRightPressed()) {
//                viewingVector = viewingVector.moveRight();
//            }
        }
    }

}
