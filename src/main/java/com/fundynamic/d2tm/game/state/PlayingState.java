package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Predicate;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader;
import com.fundynamic.d2tm.game.event.DebugKeysListener;
import com.fundynamic.d2tm.game.event.MouseListener;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.rendering.gui.DummyGuiElement;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.rendering.gui.sidebar.Sidebar;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import static com.fundynamic.d2tm.Game.*;

public class PlayingState extends BasicGameState {

    public static int ID = 0;

    private final TerrainFactory terrainFactory;
    private final Shroud shroud;
    private final Input input;
    private final Vector2D screenResolution;

    private final int tileSize;

    private Player human;
    private Player cpu;

    private GuiComposite guiComposite;

    private EntityRepository entityRepository;
    private ImageRepository imageRepository;

    private Predicate updatableEntitiesPredicate;
    private Predicate destroyedEntitiesPredicate;// pixels

    public static final int HEIGHT_OF_TOP_BAR = 42;// pixels
    public static final int HEIGHT_OF_BOTTOM_BAR = 32;
    public static final int HEIGHT_OF_MINIMAP = 128;
    public static final int WIDTH_OF_SIDEBAR = 160;

    private MapEditor mapEditor;
    private Map map;

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

        mapEditor = new MapEditor(terrainFactory);
        map = new Map(shroud, 64, 64);

        entityRepository = createEntityRepository();

        guiComposite = new GuiComposite();

        Mouse mouse = Mouse.create(
                human,
                gameContainer,
                imageRepository,
                guiComposite
        );

        BattleField battlefield = makeBattleField(human, mouse);

        guiComposite.addGuiElement(battlefield);

        // topbar
        guiComposite.addGuiElement(new DummyGuiElement(0, 0, SCREEN_WIDTH, HEIGHT_OF_TOP_BAR));

        // sidebar
        guiComposite.addGuiElement(
                new Sidebar(
                        SCREEN_WIDTH - WIDTH_OF_SIDEBAR,
                        HEIGHT_OF_TOP_BAR,
                        WIDTH_OF_SIDEBAR,
                        SCREEN_HEIGHT - (HEIGHT_OF_BOTTOM_BAR + HEIGHT_OF_MINIMAP + HEIGHT_OF_TOP_BAR)
                )
        );

        // minimap
        guiComposite.addGuiElement(
                new DummyGuiElement(
                        SCREEN_WIDTH - WIDTH_OF_SIDEBAR,
                        SCREEN_HEIGHT - (HEIGHT_OF_BOTTOM_BAR + HEIGHT_OF_MINIMAP),
                        WIDTH_OF_SIDEBAR,
                        SCREEN_HEIGHT - HEIGHT_OF_BOTTOM_BAR
                )
        );

        // bottombar
        guiComposite.addGuiElement(new DummyGuiElement(0, SCREEN_HEIGHT - HEIGHT_OF_BOTTOM_BAR, SCREEN_WIDTH - WIDTH_OF_SIDEBAR, HEIGHT_OF_BOTTOM_BAR));

        input.addMouseListener(new MouseListener(mouse));
        input.addKeyListener(new DebugKeysListener(battlefield, human));
        input.addKeyListener(new QuitGameKeyListener(gameContainer));

        initializeMap(entityRepository, human, cpu);
    }

    public BattleField makeBattleField(Player human, Mouse mouse) {
        // GUI element: the rendering of the battlefield
        BattleField battlefield;

        try {
            float moveSpeed = 30 * tileSize;
            Vector2D viewingVector = Vector2D.create(32, 32);

            Vector2D guiAreas = Vector2D.create(WIDTH_OF_SIDEBAR, (HEIGHT_OF_TOP_BAR + HEIGHT_OF_BOTTOM_BAR));
            Vector2D viewportResolution = getResolution().min(guiAreas);

            // start drawing below the top gui bar
            Vector2D viewportDrawingPosition = Vector2D.create(0, HEIGHT_OF_TOP_BAR);

            Image image = imageRepository.createImage(screenResolution);

            battlefield = new BattleField(
                    viewportResolution,
                    viewportDrawingPosition,
                    viewingVector,
                    getMap(),
                    mouse,
                    moveSpeed,
                    tileSize,
                    human,
                    image,
                    entityRepository);

        } catch (SlickException e) {
            throw new IllegalStateException("Unable to create new battlefield!", e);
        }
        return battlefield;
    }

    public Map getMap() {
        return map;
    }

    public MapEditor getMapEditor() {
        return mapEditor;
    }

    public EntityRepository createEntityRepository() throws SlickException {
        return new EntityRepository(getMap(), new Recolorer(), new EntitiesDataReader().fromRulesIni());
    }

    public void initializeMap(EntityRepository entityRepository, Player human, Player cpu) throws SlickException {
        map = getMapEditor().generateRandom(map);

        this.human = human;
        this.cpu = cpu;

        // TODO: read from SCENARIO.INI file
        // human entities
        entityRepository.placeStructureOnMap(MapCoordinate.create(5, 5), EntitiesData.CONSTRUCTION_YARD, human);

        // cpu entities
        entityRepository.placeStructureOnMap(MapCoordinate.create(57, 57), EntitiesData.CONSTRUCTION_YARD, cpu);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        // Render all GUI elements
        guiComposite.render(graphics);

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

        guiComposite.update(deltaInSeconds);
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
