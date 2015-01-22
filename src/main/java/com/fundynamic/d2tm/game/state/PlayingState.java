package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.drawing.DrawableViewPort;
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
                Vector2D viewPortDrawingPosition = Vector2D.zero();
                final Viewport newViewport;
                newViewport = new Viewport(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, this.map);
                drawableViewPorts.add(new DrawableViewPort(newViewport, viewPortDrawingPosition, Vector2D.zero(), graphics));
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

}
