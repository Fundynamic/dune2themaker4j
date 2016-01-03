package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.*;
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
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Random;
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

    private final int tileSize;

    private Player human;
    private Player cpu;

    private List<Viewport> viewports = new ArrayList<>();
    private EntityRepository entityRepository;
    private ImageRepository imageRepository;

    private Predicate updatableEntitiesPredicate;
    private Predicate destroyedEntitiesPredicate;

    public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory, ImageRepository imageRepository, Shroud shroud, int tileSize) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.shroud = shroud;
        this.tileSize = tileSize;
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
            float moveSpeed = 30 * tileSize;
            Vector2D viewportDrawingPosition = Vector2D.zero();
            Vector2D viewingVector = Vector2D.create(32, 32);
//            Vector2D half = screenResolution.min(Vector2D.create(Game.SCREEN_WIDTH / 2, 0));

            Viewport viewport = new Viewport(
                    screenResolution,
                    viewportDrawingPosition,
                    viewingVector,
                    map,
                    moveSpeed,
                    tileSize,
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
//                    tileSize,
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
        return new EntityRepository(map, new Recolorer(), new EntitiesDataReader().fromRulesIni());
    }

    public void initializeMap(EntityRepository entityRepository, Player human, Player cpu) throws SlickException {
        this.human = human;
        this.cpu = cpu;

        //TODO: read from SCENARIO.INI file
        // human entities
        entityRepository.placeStructureOnMap(Coordinate.create(5 * TILE_SIZE, 5 * TILE_SIZE), EntitiesData.REFINERY, human);
        entityRepository.placeStructureOnMap(Coordinate.create(3 * TILE_SIZE, 3 * TILE_SIZE), EntitiesData.CONSTRUCTION_YARD, human);

        entityRepository.placeUnitOnMap(Coordinate.create(10 * TILE_SIZE, 10 * TILE_SIZE), EntitiesData.SOLDIER, human);
        entityRepository.placeUnitOnMap(Coordinate.create(11 * TILE_SIZE, 11 * TILE_SIZE), EntitiesData.INFANTRY, human);
        entityRepository.placeUnitOnMap(Coordinate.create(14 * TILE_SIZE, 10 * TILE_SIZE), EntitiesData.TANK, human);
        entityRepository.placeUnitOnMap(Coordinate.create(15 * TILE_SIZE, 11 * TILE_SIZE), EntitiesData.TANK, human);

        for (int row = 0; row < 30; row += 5) {
            for (int i = 0; i < 62; i++) {
                entityRepository.placeUnitOnMap(Coordinate.create(i * TILE_SIZE, (15 + row) * TILE_SIZE), EntitiesData.TANK, human);
            }
            for (int i = 0; i < 62; i++) {
                entityRepository.placeUnitOnMap(Coordinate.create(i * TILE_SIZE, (18 + row) * TILE_SIZE), EntitiesData.TANK, cpu);
            }
        }

//        entityRepository.placeUnitOnMap(Coordinate.create(16 * TILE_SIZE, 16 * TILE_SIZE), EntitiesData.INFANTRY, cpu);
//        entityRepository.placeUnitOnMap(Coordinate.create(19 * TILE_SIZE, 15 * TILE_SIZE), EntitiesData.QUAD, cpu);
//        entityRepository.placeUnitOnMap(Coordinate.create(20 * TILE_SIZE, 16 * TILE_SIZE), EntitiesData.TANK, cpu);

        // cpu entities
        entityRepository.placeStructureOnMap(Coordinate.create(55 * TILE_SIZE, 55 * TILE_SIZE), EntitiesData.REFINERY, cpu);
        entityRepository.placeStructureOnMap(Coordinate.create(57 * TILE_SIZE, 57 * TILE_SIZE), EntitiesData.CONSTRUCTION_YARD, cpu);

        entityRepository.placeUnitOnMap(Coordinate.create(50 * TILE_SIZE, 50 * TILE_SIZE), EntitiesData.TANK, cpu);
        entityRepository.placeUnitOnMap(Coordinate.create(49 * TILE_SIZE, 49 * TILE_SIZE), EntitiesData.TANK, cpu);
        entityRepository.placeUnitOnMap(Coordinate.create(52 * TILE_SIZE, 52 * TILE_SIZE), EntitiesData.SOLDIER, cpu);
        entityRepository.placeUnitOnMap(Coordinate.create(53 * TILE_SIZE, 53 * TILE_SIZE), EntitiesData.INFANTRY, cpu);

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
