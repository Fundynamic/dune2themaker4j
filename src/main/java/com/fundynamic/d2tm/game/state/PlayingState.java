package com.fundynamic.d2tm.game.state;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.event.DebugKeysListener;
import com.fundynamic.d2tm.game.event.MouseListener;
import com.fundynamic.d2tm.game.event.QuitGameKeyListener;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.rendering.gui.sidebar.MiniMap;
import com.fundynamic.d2tm.game.rendering.gui.sidebar.Sidebar;
import com.fundynamic.d2tm.game.rendering.gui.topbar.Topbar;
import com.fundynamic.d2tm.game.scenario.Scenario;
import com.fundynamic.d2tm.game.scenario.AbstractScenarioFactory;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import static com.fundynamic.d2tm.Game.*;
import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

public class PlayingState extends BasicGameState {

    public static int ID = 0;

    private final Input input;
    private final Vector2D screenResolution;
    private final AbstractScenarioFactory abstractScenarioFactory;
    private Scenario scenario;

    private GuiComposite guiComposite;

    private ImageRepository imageRepository;

    public static final int HEIGHT_OF_TOP_BAR = 42; // pixels
    public static final int HEIGHT_OF_MINIMAP = 160;
    public static final int WIDTH_OF_SIDEBAR = 160;

    private Mouse mouse;

    public PlayingState(GameContainer gameContainer, ImageRepository imageRepository, AbstractScenarioFactory abstractScenarioFactory) throws SlickException {
        this.input = gameContainer.getInput();
        this.screenResolution = getResolution();
        this.imageRepository = imageRepository;
        this.abstractScenarioFactory = abstractScenarioFactory;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        scenario = abstractScenarioFactory.create();
        Player human = scenario.getHuman();

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
                        battlefield, scenario.getEntityRepository(), scenario.getMap(), human
                )
        );

        input.addMouseListener(new MouseListener(mouse));
        input.addKeyListener(new DebugKeysListener(battlefield, scenario.getHuman(), scenario.getEntityRepository()));
        input.addKeyListener(new QuitGameKeyListener(gameContainer));
    }

    public BattleField makeBattleField(Player human, Mouse mouse) {
        // GUI element: the rendering of the battlefield
        BattleField battlefield;

        try {
            float moveSpeed = 30 * TILE_SIZE;
            Vector2D viewingVector = MapCoordinate.create(1, 1).toCoordinate();

            Vector2D guiAreas = Vector2D.create(WIDTH_OF_SIDEBAR, HEIGHT_OF_TOP_BAR);
            Vector2D viewportDimensions = getResolution().min(guiAreas);

            // start drawing below the top gui bar
            Vector2D viewportDrawingPosition = Vector2D.create(0, HEIGHT_OF_TOP_BAR);

            Image image = imageRepository.createImage(viewportDimensions);

            battlefield = new BattleField(
                    viewportDimensions,
                    viewportDrawingPosition,
                    viewingVector,
                    scenario.getMap(),
                    mouse,
                    moveSpeed,
                    human,
                    image,
                    scenario.getEntityRepository());

        } catch (SlickException e) {
            throw new IllegalStateException("Unable to create new battlefield!", e);
        }
        return battlefield;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
        // Render all GUI elements
        guiComposite.render(graphics);
        scenario.render(graphics);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        float deltaInSeconds = delta / 1000f;

        mouse.update(deltaInSeconds);

        scenario.update(deltaInSeconds);

        guiComposite.update(deltaInSeconds);
    }

}
