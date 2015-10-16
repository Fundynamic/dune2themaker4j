package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.event.DebugKeysListener;
import com.fundynamic.d2tm.game.event.MouseInViewportListener;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

import static com.fundynamic.d2tm.Game.TILE_SIZE;
import static com.fundynamic.d2tm.Game.getResolution;

public class PlayingState extends BasicGameState {

    public static int ID = 0;

    private final TerrainFactory terrainFactory;
    private final Shroud shroud;
    private final Input input;
    private final Vector2D screenResolution;

    private final int tileWidth;
    private final int tileHeight;

    private Player human;
    private Player cpu;

    private List<Viewport> viewports = new ArrayList<>();
    private EntityRepository entityRepository;
    private ImageRepository imageRepository;

    private Predicate updatableEntitiesPredicate;
    private Predicate destroyedEntitiesPredicate;

    public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory, ImageRepository imageRepository, Shroud shroud, int tileWidth, int tileHeight) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.shroud = shroud;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.input = gameContainer.getInput();
        this.screenResolution = getResolution();
        this.imageRepository = imageRepository;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        Player human = new Player("Human", Recolorer.FactionColor.RED);
        Player cpu = new Player("CPU", Recolorer.FactionColor.GREEN);

        MapEditor mapEditor = new MapEditor(terrainFactory);
        Map map = mapEditor.generateRandom(shroud, 64, 64);

        entityRepository = createEntityRepository(map);

        Mouse mouse = Mouse.create(human, gameContainer, entityRepository, imageRepository);

        try {
            float moveSpeed = 30 * tileWidth;
            Vector2D viewportDrawingPosition = Vector2D.zero();
            Vector2D viewingVector = Vector2D.create(32, 32);
//            Vector2D half = screenResolution.min(Vector2D.create(Game.SCREEN_WIDTH / 2, 0));

            Viewport viewport = new Viewport(
                    screenResolution,
                    viewportDrawingPosition,
                    viewingVector,
                    map,
                    moveSpeed,
                    tileWidth,
                    tileHeight,
                    mouse,
                    human,
                    imageRepository.createImage(screenResolution));

            // here we can create a new viewport
//            Vector2D viewportDrawingPosition2 = viewportDrawingPosition.add(Vector2D.create(Game.SCREEN_WIDTH / 2, 0));
//
//            Viewport viewport2 = new Viewport(
//                    half,
//                    viewportDrawingPosition2,
//                    viewingVector,
//                    map,
//                    moveSpeed,
//                    tileWidth,
//                    tileHeight,
//                    mouse,
//                    human);

            // Add listener for this viewport
            // THIS WON'T WORK, BECAUSE FOR NOW WE GET VIEWPORT FROM MOUSE IN MOUSE/VIEWPORT LOGIC!
            input.addMouseListener(new MouseInViewportListener(mouse));

            viewports.add(viewport);

            input.addKeyListener(new DebugKeysListener(mouse, viewport, entityRepository, human));
        } catch (SlickException e) {
            throw new IllegalStateException("Unable to create new viewport!", e);
        }
        input.addKeyListener(new QuitGameKeyListener(gameContainer));

        initializeMap(entityRepository, human, cpu);
    }

    public EntityRepository createEntityRepository(Map map) throws SlickException {
        return new EntityRepository(map, new Recolorer());
    }

    public void initializeMap(EntityRepository entityRepository, Player human, Player cpu) throws SlickException {
        this.human = human;
        this.cpu = cpu;

        // human entities
        entityRepository.placeStructureOnMap(Vector2D.create(5 * TILE_SIZE, 5 * TILE_SIZE), EntityRepository.REFINERY, human);
        entityRepository.placeStructureOnMap(Vector2D.create(3 * TILE_SIZE, 3 * TILE_SIZE), EntityRepository.CONSTRUCTION_YARD, human);

        entityRepository.placeUnitOnMap(Vector2D.create(10 * TILE_SIZE, 10 * TILE_SIZE), 0, human);
        entityRepository.placeUnitOnMap(Vector2D.create(11 * TILE_SIZE, 11 * TILE_SIZE), 0, human);
        entityRepository.placeUnitOnMap(Vector2D.create(14 * TILE_SIZE, 10 * TILE_SIZE), 1, human);
        entityRepository.placeUnitOnMap(Vector2D.create(15 * TILE_SIZE, 11 * TILE_SIZE), 1, human);

        // cpu entities
        entityRepository.placeStructureOnMap(Vector2D.create(55 * TILE_SIZE, 55 * TILE_SIZE), EntityRepository.REFINERY, cpu);
        entityRepository.placeStructureOnMap(Vector2D.create(57 * TILE_SIZE, 57 * TILE_SIZE), EntityRepository.CONSTRUCTION_YARD, cpu);

        entityRepository.placeUnitOnMap(Vector2D.create(50 * TILE_SIZE, 50 * TILE_SIZE), 0, cpu);
        entityRepository.placeUnitOnMap(Vector2D.create(49 * TILE_SIZE, 49 * TILE_SIZE), 0, cpu);
        entityRepository.placeUnitOnMap(Vector2D.create(52 * TILE_SIZE, 52 * TILE_SIZE), 1, cpu);
        entityRepository.placeUnitOnMap(Vector2D.create(53 * TILE_SIZE, 53 * TILE_SIZE), 1, cpu);

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        for (Viewport viewport : viewports) {
            viewport.render(graphics);
        }

        Font font = graphics.getFont();

        font.drawString(540, 20, "Human entity count: " + human.aliveEntities(), Color.red);
        font.drawString(540, 40, "Enemy entity count: " + cpu.aliveEntities(), Color.green);

        if (cpu.aliveEntities() < 1) {
            // why like this!?
            font.drawString(10, 220, "Enemy player has been destroyed. You have won the game.", Color.green);
        }

        if (human.aliveEntities() < 1) {
            font.drawString(10, 220, "All your units and structures are destroyed. You have lost the game.", Color.red);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        float deltaInSeconds = delta / 1000f;

        Predicate<Entity> updatableEntities = updatableEntitiesPredicate();
        for (Entity entity : entityRepository.filter(updatableEntities)) {
            entity.update(deltaInSeconds);
        }

        // Entities can 'remove' themselves, by flagging them as 'removable'
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
