package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.event.DebugKeysListener;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.event.ViewportMovementListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
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

    private Predicate updatableEntitiesPredicate;
    private Predicate destroyedEntitiesPredicate;

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
        this.map = mapEditor.generateRandom(shroud, mapWidth, mapHeight);
        entityRepository = new EntityRepository(map, new Recolorer());

        this.mouse = new Mouse(human, gameContainer, entityRepository);
        this.mouse.addMouseImage(Mouse.MouseImages.NORMAL, new Image("mouse/mouse_normal.png"));
        this.mouse.addMouseImage(Mouse.MouseImages.HOVER_OVER_SELECTABLE_ENTITY, new Image("mouse/mouse_pick.png"));
        this.mouse.addMouseImage(Mouse.MouseImages.MOVE, new Image("mouse/mouse_move.png"));
        this.mouse.addMouseImage(Mouse.MouseImages.ATTACK, new Image("mouse/mouse_attack.png"));
        this.mouse.init();

        entityRepository.placeStructureOnMap(Vector2D.create(5, 5), EntityRepository.REFINERY, human);
        entityRepository.placeStructureOnMap(Vector2D.create(3, 3), EntityRepository.CONSTRUCTION_YARD, human);
        entityRepository.placeUnitOnMap(Vector2D.create(10, 10), 0, human);
        entityRepository.placeUnitOnMap(Vector2D.create(11, 11), 0, human);
        entityRepository.placeUnitOnMap(Vector2D.create(14, 10), 1, human);
        entityRepository.placeUnitOnMap(Vector2D.create(15, 11), 1, human);

        entityRepository.placeStructureOnMap(Vector2D.create(55, 55), EntityRepository.REFINERY, cpu);
        entityRepository.placeStructureOnMap(Vector2D.create(57, 57), EntityRepository.CONSTRUCTION_YARD, cpu);
        entityRepository.placeUnitOnMap(Vector2D.create(50, 50), 0, cpu);
        entityRepository.placeUnitOnMap(Vector2D.create(49, 49), 0, cpu);
        entityRepository.placeUnitOnMap(Vector2D.create(52, 52), 1, cpu);
        entityRepository.placeUnitOnMap(Vector2D.create(53, 53), 1, cpu);

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

            input.addKeyListener(new DebugKeysListener(mouse, viewport, entityRepository, human));
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

        g.getFont().drawString(540, 20, "Human entity count: " + human.aliveEntities(), Color.red);
        g.getFont().drawString(540, 40, "Enemy entity count: " + cpu.aliveEntities(), Color.green);

        if (cpu.aliveEntities() < 1) {
            container.getGraphics().getFont().drawString(10, 220, "Enemy player has been destroyed. You have won the game.", Color.green);
        }

        if (human.aliveEntities() < 1) {
            container.getGraphics().getFont().drawString(10, 220, "All your units and structures are destroyed. You have lost the game.", Color.red);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        float deltaInSeconds = delta / 1000f;

        Predicate<Entity> updatableEntities = updatableEntitiesPredicate();
        for (Entity entity : entityRepository.filter(updatableEntities)) {
            entity.update(deltaInSeconds);
        }

        entityRepository.removeEntities(destroyedEntitiesPredicate());

        for (Viewport viewport : viewports) {
            viewport.update(deltaInSeconds);
        }
    }

    private Predicate<Entity> updatableEntitiesPredicate() {
        if (this.updatableEntitiesPredicate == null) {
            this.updatableEntitiesPredicate = Predicate.builder().isUpdateable().build();
        }
        return this.updatableEntitiesPredicate;
    }

    private Predicate<Entity> destroyedEntitiesPredicate() {
        if (this.destroyedEntitiesPredicate == null) {
            this.destroyedEntitiesPredicate = Predicate.builder().isDestroyed().build();
        }
        return this.destroyedEntitiesPredicate;
    }
}
