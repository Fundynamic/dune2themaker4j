package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.drawing.Viewport;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.event.ViewportMovementListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.ConstructionYard;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

public class PlayingState extends BasicGameState {

    public static int ID = 0;

    private final TerrainFactory terrainFactory;
    private final Shroud shroud;
    private final Input input;
    private final Vector2D screenResolution;

    private final int tileWidth;
    private final int tileHeight;

    private Map map;
    private Graphics graphics;
    private Mouse mouse;

    private List<Viewport> viewports = new ArrayList<>();

    public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory, Shroud shroud, int tileWidth, int tileHeight) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.shroud = shroud;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.graphics = gameContainer.getGraphics();
        this.input = gameContainer.getInput();
        this.screenResolution = new Vector2D(gameContainer.getWidth(), gameContainer.getHeight());
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        input.addKeyListener(new QuitGameKeyListener(gameContainer));

        this.map = Map.generateRandom(terrainFactory, shroud, 64, 64);

        this.mouse = new Mouse(this.map.getCell(0, 0));

        this.map.getCell(10,10).
                setConstructionYard(new ConstructionYard(new Image("structures/2x2_constyard.png")));

        try {
            float moveSpeed = 16.0F;
            Vector2D viewportDrawingPosition = Vector2D.zero();
            Vector2D viewingVector = Vector2D.create(40, 40);
            Viewport viewport = new Viewport(screenResolution, viewportDrawingPosition, viewingVector, graphics, this.map, moveSpeed, tileWidth, tileHeight, mouse);

            // Add listener for this viewport
            input.addMouseListener(new ViewportMovementListener(viewport, screenResolution, mouse));

            viewports.add(viewport);
        } catch (SlickException e) {
            throw new IllegalStateException("Unable to create new viewport!", e);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        for (Viewport viewport : viewports) {
            viewport.render();
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        for (Viewport viewport : viewports) {
            viewport.update();
        }
    }
}
