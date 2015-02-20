package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.drawing.Viewport;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.event.ViewportMovementListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.structures.Structure;
import com.fundynamic.d2tm.game.structures.StructuresRepository;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.units.UnitsRepository;
import com.fundynamic.d2tm.graphics.Shroud;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
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

    private StructuresRepository structuresRepository;
    private UnitsRepository unitsRepository;

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
        this.structuresRepository = new StructuresRepository(map);
        this.unitsRepository = new UnitsRepository(map);

        this.mouse = new Mouse();

        this.structuresRepository.placeStructureOnMap(Vector2D.create(5, 5), StructuresRepository.REFINERY);
        this.unitsRepository.placeUnitOnMap(Vector2D.create(10, 10), UnitsRepository.QUAD);

        try {
            float moveSpeed = 30 * tileWidth;
            Vector2D viewportDrawingPosition = Vector2D.zero();
            Vector2D viewingVector = Vector2D.create(40, 40);
            Viewport viewport = new Viewport(screenResolution, viewportDrawingPosition, viewingVector, graphics, this.map, moveSpeed, tileWidth, tileHeight, mouse);

            // Add listener for this viewport
            input.addMouseListener(new ViewportMovementListener(viewport, mouse, structuresRepository));

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
        float deltaInSeconds = delta / 1000f;

        for (Structure structure : map.getStructures()) {
          structure.update(deltaInSeconds);
        }
        for (Viewport viewport : viewports) {
            viewport.update(deltaInSeconds);
        }
    }
}
