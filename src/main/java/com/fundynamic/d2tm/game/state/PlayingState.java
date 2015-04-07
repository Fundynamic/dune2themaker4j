package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.event.DebugKeysListener;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.event.ViewportMovementListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
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

    private Player human;
    private Player cpu;

    private List<Viewport> viewports = new ArrayList<>();
    private EntityRepository entityRepository;

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
        this.human = new Player("Human", Recolorer.FactionColor.RED);
        this.cpu = new Player("CPU", Recolorer.FactionColor.GREEN);

        int mapWidth = 64;
        int mapHeight = 64;
        MapEditor mapEditor = new MapEditor(terrainFactory);
        this.map = mapEditor.generateRandom(terrainFactory, shroud, mapWidth, mapHeight);
        entityRepository = new EntityRepository(map, new Recolorer());

        this.mouse = new Mouse(human, gameContainer, entityRepository);
        this.mouse.addMouseImage(Mouse.MouseImages.NORMAL, new Image("mouse/mouse_normal.png"));
        this.mouse.addMouseImage(Mouse.MouseImages.HOVER_OVER_SELECTABLE_ENTITY, new Image("mouse/mouse_pick.png"));
        this.mouse.addMouseImage(Mouse.MouseImages.MOVE, new Image("mouse/mouse_move.png"));
        this.mouse.addMouseImage(Mouse.MouseImages.ATTACK, new Image("mouse/mouse_attack.png"));
        this.mouse.init();

        entityRepository.placeStructureOnMap(Vector2D.create(5, 5), EntityRepository.REFINERY, human);
        for (int i = 0; i < 50; i++) {
            Vector2D randomCell = Vector2D.random(mapWidth, mapHeight);
            if (map.getCell(randomCell).getEntity() != null) continue;
            if (Random.getInt(10) < 5) {
                entityRepository.placeUnitOnMap(randomCell, Random.getInt(2), human);
            } else {
                entityRepository.placeUnitOnMap(randomCell, Random.getInt(2), cpu);
            }
        }


        try {
            float moveSpeed = 30 * tileWidth;
            Vector2D viewportDrawingPosition = Vector2D.zero();
            Vector2D viewingVector = Vector2D.create(32, 32);

            Viewport viewport = new Viewport(
                    screenResolution,
                    viewportDrawingPosition,
                    viewingVector,
                    this.map,
                    moveSpeed,
                    tileWidth,
                    tileHeight,
                    mouse,
                    human);

            // Add listener for this viewport
            input.addMouseListener(new ViewportMovementListener(viewport, mouse, entityRepository, human));

            viewports.add(viewport);

            input.addKeyListener(new DebugKeysListener(mouse, viewport, entityRepository));
        } catch (SlickException e) {
            throw new IllegalStateException("Unable to create new viewport!", e);
        }
        input.addKeyListener(new QuitGameKeyListener(gameContainer));
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        for (Viewport viewport : viewports) {
            viewport.render(g);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        float deltaInSeconds = delta / 1000f;

        Predicate updatableEntities = Predicate.builder().isUpdateable().build();
        for (Entity entity : entityRepository.filter(updatableEntities)) {
            entity.update(deltaInSeconds);
        }

        for (Viewport viewport : viewports) {
            viewport.update(deltaInSeconds);
        }
    }
}
