package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader;
import com.fundynamic.d2tm.game.event.DebugKeysListener;
import com.fundynamic.d2tm.game.event.MouseListener;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.rendering.gui.sidebar.MiniMap;
import com.fundynamic.d2tm.game.rendering.gui.sidebar.Sidebar;
import com.fundynamic.d2tm.game.rendering.gui.topbar.Topbar;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrain;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import static com.fundynamic.d2tm.Game.*;
import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

public class PlayingState extends BasicGameState {

    public static int ID = 0;

    private final TerrainFactory terrainFactory;
    private final Shroud shroud;
    private final Input input;
    private final Vector2D screenResolution;

    private Player human;
    private Player cpu;

    private GuiComposite guiComposite;

    private EntityRepository entityRepository;
    private ImageRepository imageRepository;

    private Predicate updatableEntitiesPredicate;
    private Predicate destroyedEntitiesPredicate;// pixels

    public static final int HEIGHT_OF_TOP_BAR = 42;// pixels
    public static final int HEIGHT_OF_MINIMAP = 160;
    public static final int WIDTH_OF_SIDEBAR = 160;

    private MapEditor mapEditor;
    private Map map;
    private Mouse mouse;

    public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory, ImageRepository imageRepository, Shroud shroud) throws SlickException {
        this.terrainFactory = terrainFactory;
        this.shroud = shroud;
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
        Player human = new Player("Human", Faction.GREEN);
        Player cpu = new Player("CPU", Faction.RED);

        if (Game.RECORDING_VIDEO) {
            human.setCredits(2200);
        } else {
            human.setCredits(3000);
        }
        cpu.setCredits(2000);

        mapEditor = new MapEditor(terrainFactory);
        map = new Map(shroud, 128, 128);

        entityRepository = createEntityRepository();

        guiComposite = new GuiComposite();

        mouse = Mouse.create(
                human,
                gameContainer,
                imageRepository,
                guiComposite
        );

        BattleField battlefield = makeBattleField(human, mouse);

        guiComposite.addGuiElement(battlefield);

        // topbar / moneybar
        guiComposite.addGuiElement(new Topbar(0, 0, SCREEN_WIDTH, HEIGHT_OF_TOP_BAR, human, imageRepository.loadAndCache("lightning.png")));

        // sidebar
        guiComposite.addGuiElement(
                new Sidebar(
                        SCREEN_WIDTH - WIDTH_OF_SIDEBAR,
                        HEIGHT_OF_TOP_BAR,
                        WIDTH_OF_SIDEBAR,
                        SCREEN_HEIGHT - (HEIGHT_OF_MINIMAP + HEIGHT_OF_TOP_BAR)
                )
        );

        // minimap
        guiComposite.addGuiElement(
                new MiniMap(
                        SCREEN_WIDTH - WIDTH_OF_SIDEBAR,
                        SCREEN_HEIGHT - HEIGHT_OF_MINIMAP,
                        WIDTH_OF_SIDEBAR,
                        HEIGHT_OF_MINIMAP,
                        map,
                        entityRepository
                )
        );

        input.addMouseListener(new MouseListener(mouse));
        input.addKeyListener(new DebugKeysListener(battlefield, human, entityRepository));
        input.addKeyListener(new QuitGameKeyListener(gameContainer));

        initializeMap(entityRepository, human, cpu);
    }

    public BattleField makeBattleField(Player human, Mouse mouse) {
        // GUI element: the rendering of the battlefield
        BattleField battlefield;

        try {
            float moveSpeed = 30 * TILE_SIZE;
            Vector2D viewingVector = Vector2D.create(32, 32);

            Vector2D guiAreas = Vector2D.create(WIDTH_OF_SIDEBAR, HEIGHT_OF_TOP_BAR);
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
        return new EntityRepository(
                getMap(),
                new Recolorer(),
                new EntitiesDataReader().fromRulesIni()
        );
    }

    public void initializeMap(EntityRepository entityRepository, Player human, Player cpu) throws SlickException {
        map = getMapEditor().generateRandom(map);

        this.human = human;
        this.cpu = cpu;

        MapCoordinate playerConstyard = MapCoordinate.create(5, 5);
        MapCoordinate cpuConstyard = MapCoordinate.create(57, 57);

        // create spice field nearby
        mapEditor.createCircularField(map, MapCoordinate.create(15, 15), DuneTerrain.TERRAIN_SPICE, 5);
        mapEditor.createCircularField(map, playerConstyard, DuneTerrain.TERRAIN_ROCK, 5);
        mapEditor.createCircularField(map, cpuConstyard, DuneTerrain.TERRAIN_ROCK, 5);
        mapEditor.smooth(map);

        // TODO: read from SCENARIO.INI file
        // human entities
//        entityRepository.placeUnitOnMap(MapCoordinate.create(8, 2), "QUAD", human);
        entityRepository.placeStructureOnMap(playerConstyard, EntitiesData.CONSTRUCTION_YARD, human);

        // cpu entities
//        entityRepository.placeUnitOnMap(MapCoordinate.create(40, 40), "QUAD", cpu);
//        entityRepository.placeUnitOnMap(MapCoordinate.create(50, 50), "QUAD", cpu);
//        entityRepository.placeUnitOnMap(MapCoordinate.create(30, 32), "QUAD", cpu);
//        entityRepository.placeUnitOnMap(MapCoordinate.create(34, 43), "QUAD", cpu);
        entityRepository.placeStructureOnMap(cpuConstyard, EntitiesData.CONSTRUCTION_YARD, cpu);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        // Render all GUI elements
        guiComposite.render(graphics);

        Font font = graphics.getFont();

        // TODO: Proper end-game conditions and dealing with them
        if (cpu.aliveEntities() < 1) {
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

        mouse.update(deltaInSeconds);

        human.update(deltaInSeconds);
        cpu.update(deltaInSeconds);

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
