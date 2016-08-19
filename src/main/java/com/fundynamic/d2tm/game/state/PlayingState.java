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
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

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

    // GUI element: the rendering of the battlefield
    private Viewport battlefield;

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
            Vector2D viewingVector = Vector2D.create(32, 32);


            int heightOfTopBar = 42; // pixels
            int bottomBar = 32; // pixels
            int widthOfSidebar = 160;

            Vector2D guiAreas = Vector2D.create(widthOfSidebar, (heightOfTopBar + bottomBar));
            Vector2D viewportResolution = getResolution().min(guiAreas);

            // start drawing below the top gui bar
            Vector2D viewportDrawingPosition = Vector2D.zero().add(Vector2D.create(0, heightOfTopBar));

            battlefield = new Viewport(
                    viewportResolution,
                    viewportDrawingPosition,
                    viewingVector,
                    map,
                    moveSpeed,
                    tileSize,
                    mouse,
                    human,
                    imageRepository.createImage(screenResolution));

            // Add listener for this battlefield
            // THIS WON'T WORK, BECAUSE FOR NOW WE GET VIEWPORT FROM MOUSE IN MOUSE/VIEWPORT LOGIC!
            input.addMouseListener(new MouseInViewportListener(mouse));

            input.addKeyListener(new DebugKeysListener(mouse, battlefield, entityRepository, human));
        } catch (SlickException e) {
            throw new IllegalStateException("Unable to create new battlefield!", e);
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
        entityRepository.placeStructureOnMap(Coordinate.create(5 * TILE_SIZE, 5 * TILE_SIZE), EntitiesData.CONSTRUCTION_YARD, human);

        entityRepository.placeStructureOnMap(Coordinate.create(57 * TILE_SIZE, 57 * TILE_SIZE), EntitiesData.CONSTRUCTION_YARD, cpu);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        // Render all GUI elements
        battlefield.render(graphics);

        Vector2D resolution = getResolution();

        // TODO:
        // 1. top bar (buttons, credits, resources, etc)
        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, resolution.getXAsInt(), 42);

        // 2. sidebar (for interacting with selected unit(s), structure(s), etc)
        graphics.setColor(Color.red);
        graphics.fillRect(resolution.getXAsInt() - 160, 0, resolution.getXAsInt(), resolution.getYAsInt());

        // 3. minimap
        graphics.setColor(Color.green);
        graphics.fillRect(resolution.getXAsInt() - 129, resolution.getYAsInt() - 129, resolution.getXAsInt() - 1, resolution.getYAsInt() - 1);


        // 4. bottom bar (messages bar)
        graphics.setColor(Color.blue);
        graphics.fillRect(0, resolution.getYAsInt() - 32, resolution.getXAsInt(), resolution.getYAsInt());

        Font font = graphics.getFont();

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

        battlefield.update(deltaInSeconds);
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
