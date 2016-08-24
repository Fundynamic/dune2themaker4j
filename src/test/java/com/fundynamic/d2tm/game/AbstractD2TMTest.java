package com.fundynamic.d2tm.game;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.TestableMouse;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.RenderQueueEnrichableWithFacingLogic;
import com.fundynamic.d2tm.game.entities.units.TestableRenderQueueEnrichableWithFacingLogic;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.entities.units.UnitFacings;
import com.fundynamic.d2tm.game.event.MouseListener;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractD2TMTest {

    public static final float ONE_FRAME_PER_SECOND_DELTA = 1f;

    public static Vector2D screenResolution = Game.getResolution();

    public static Vector2D battlefieldSize = Vector2D.create(320, 200);
    public static Vector2D battlefieldViewingVector = Vector2D.create(32, 32);
    public static Vector2D battleFieldDrawingPosition = Vector2D.create(0, 42);
    public static float battleFieldMoveSpeed = 2.0F;
    public static int battleFieldTileSize = Game.TILE_SIZE;

    public static int TILE_SIZE = 32;

    public static int MAP_WIDTH = 64;
    public static int MAP_HEIGHT = 64;

    @Mock
    protected Graphics graphics;

    @Mock
    protected GameContainer gameContainer;

    protected ImageRepository imageRepository;
    protected EntitiesDataReader entitiesDataReader;
    protected EntityRepository entityRepository;
    protected EntitiesData entitiesData;

    protected GuiComposite guiComposite;

    protected Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);
    protected Player cpu = new Player("CPU", Recolorer.FactionColor.BLUE);
    protected Map map;
    protected BattleField battleField;


    protected Mouse mouse;
    protected MouseListener listener;
    protected Shroud shroud;

    @Before
    public void setUp() throws SlickException {
        shroud = new Shroud(null, Game.TILE_SIZE) {
            @Override
            public SpriteSheet createSpriteSheetFromImage() {
                return mock(SpriteSheet.class);
            }
        };

        map = makeMap(MAP_WIDTH, MAP_HEIGHT); // create a default map
        imageRepository = makeImageRepository();
        entitiesDataReader = makeEntitiesDataReader();
        entitiesData = entitiesDataReader.fromRulesIni();
        entityRepository = makeTestableEntityRepository(map, entitiesData);

        // Nice little circular dependency here...
        guiComposite = new GuiComposite();
        mouse = makeTestableMouse(player, guiComposite);

        listener = new MouseListener(mouse);

        Image bufferWithGraphics = mock(Image.class);
        Graphics bufferGraphics = mock(Graphics.class);
        when(bufferWithGraphics.getGraphics()).thenReturn(bufferGraphics);

        battleField = new BattleField(
                battlefieldSize,
                battleFieldDrawingPosition,
                battlefieldViewingVector,
                map,
                mouse,
                battleFieldMoveSpeed,
                battleFieldTileSize,
                player,
                bufferWithGraphics,
                entityRepository
        );

        guiComposite.addGuiElement(battleField);


        Input input = mock(Input.class);
        when(gameContainer.getInput()).thenReturn(input);
    }

    public static EntitiesDataReader makeEntitiesDataReader() {
        return new EntitiesDataReader() {
            @Override
            public EntitiesData createNewEntitiesData() {
                return new EntitiesData() {
                    @Override
                    protected Image loadImage(String pathToImage) throws SlickException {
                        return mock(Image.class);
                    }
                };
            }
        };
    }

    // RESOURCE LOADING
    ////////////////////////////////////////////////////////////////////////////////
    private ImageRepository makeImageRepository() {
        return new ImageRepository() {
            @Override
            public Image load(String path) {
                return mock(Image.class);
            }

            @Override
            public Image createImage(Vector2D dimensions) throws SlickException {
                return mock(Image.class);
            }
        };
    }

    // MOUSE
    ////////////////////////////////////////////////////////////////////////////////
    /**
     * Creates mouse with TestableEntityRepository and TestableImageRepository
     *
     * @param player
     * @param guiComposite
     * @return
     * @throws SlickException
     */
    public Mouse makeTestableMouse(Player player, GuiComposite guiComposite) throws SlickException {
        GameContainer gameContainer = mock(GameContainer.class);
        return new TestableMouse(player, gameContainer, guiComposite);
    }

    // MAP
    ////////////////////////////////////////////////////////////////////////////////
    public Map makeMap(int width, int height) throws SlickException {
        final Image mockedImage = mock(Image.class);
        final Graphics mockedImageGraphics = mock(Graphics.class);
        when(mockedImage.getGraphics()).thenReturn(mockedImageGraphics);
        return new Map(shroud, width, height) {
            @Override
            public Cell getCell(int x, int y) {
                Cell cell = super.getCell(x, y);
                // TODO: get rid of SUPER UGLY WAY TO HIJACK INTO RENDERING STUFF
                cell.setTileImage(mockedImage);
                return cell;
            }
        };
    }

    public Cell makeCell(int x, int y) {
        Terrain terrain = mock(Terrain.class);
        return new Cell(map, terrain, x, y);
    }

    // STRUCTURES
    ////////////////////////////////////////////////////////////////////////////////
    public Structure makeStructure(Player player, int hitPoints) {
        return makeStructure(player, hitPoints, 2, 3, 5, Coordinate.create(32, 32));
    }

    public Structure makeStructure(Player player, int hitPoints, Coordinate coordinate) {
        return makeStructure(player, hitPoints, 2, 3, 5, coordinate);
    }

    public Structure makeStructure(Player player, int hitPoints, int widthInCells, int heightInCells, int sight, final Coordinate coordinate) {
        EntityData entityData = new EntityData(widthInCells * TILE_SIZE, heightInCells * TILE_SIZE, sight);
        entityData.hitPoints = hitPoints;
        Structure structure = new Structure(coordinate, mock(SpriteSheet.class), player, entityData, entityRepository) {

            @Override
            public boolean isDestroyed() {
                // we do this so that we do not have to deal with spawning explosions (which is done in the
                // update method)
                return super.hitPointBasedDestructibility.hasDied();
            }

            @Override
            public Image getSprite() {
                return mock(Image.class);
            }
        };
        entityRepository.placeOnMap(structure);
        return structure;
    }

    // UNIT
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     *     Creates a {@link EntitiesData#QUAD} with a given {@link UnitFacings} on a specified coordinate.
     * </p>
     * @param facing
     * @param coordinate
     * @return {@link Unit} constructed
     */
    public Unit makeUnit(UnitFacings facing, Coordinate coordinate) {
        return makeUnit(facing, coordinate, Vector2D.zero());
    }

    /**
     * <p>
     *     Creates a {@link EntitiesData#QUAD} with a given {@link UnitFacings} on a specified coordinate with given
     *     offset (from the coordinate).
     * </p>
     * @param facing
     * @param coordinate
     * @param offset
     * @return {@link Unit} constructed
     */
    public Unit makeUnit(UnitFacings facing, Coordinate coordinate, Vector2D offset) {
        Unit unit = makeUnit(player, coordinate, EntitiesData.QUAD);
        unit.setFacing(facing.getValue());
        unit.setOffset(offset);
        return unit;
    }

    /**
     * <p>
     *     Creates a {@link EntitiesData#QUAD} unit at 0,0
     * </p>
     * <p>
     *     Use this when you do not care about any location or type of unit.
     * </p>
     * @param player
     * @return {@link Unit} constructed
     */
    public Unit makeUnit(Player player) {
        return makeUnit(player, Coordinate.create(0, 0), EntitiesData.QUAD);
    }

    /**
     * <p>
     *     Creates a unit for player at coordinate and given ID. Where ID is the string representation
     *     of an {@link EntityData}.
     * </p>
     * @param player
     * @param coordinate
     * @param id
     * @return {@link Unit} constructed
     */
    public Unit makeUnit(Player player, Coordinate coordinate, String id) {
        if (entityRepository == null) throw new IllegalStateException("You forgot to set up the entityRepository, probably you need to do super.setUp()");
        if (map == null) throw new IllegalStateException("You forgot to set up the map, probably you need to do super.setUp()");
        return (Unit) entityRepository.placeOnMap(coordinate, EntityType.UNIT, id, player);
    }

    // PROJECTILE
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructs a {@link EntitiesData#LARGE_ROCKET} on given {@link Coordinate} for Human {@link Player}
     * @param coordinate
     * @return the {@link Projectile} constructed
     */
    public Projectile makeProjectile(Coordinate coordinate) {
        return makeProjectile(player, coordinate);
    }

    /**
     * Constructs a {@link EntitiesData#LARGE_ROCKET} on given {@link Coordinate} and {@link Player}
     * @param player
     * @param coordinate
     * @return the {@link Projectile} constructed
     */
    public Projectile makeProjectile(Player player,  Coordinate coordinate) {
        return entityRepository.placeProjectile(coordinate, EntitiesData.LARGE_ROCKET, player);
    }

    public EntityRepository makeTestableEntityRepository(final Map map, EntitiesData entitiesData) throws SlickException {
        Image image = mock(Image.class);
        Recolorer recolorer = mock(Recolorer.class);
        when(recolorer.recolorToFactionColor(any(Image.class), any(Recolorer.FactionColor.class))).thenReturn(image);
        return new EntityRepository(map, recolorer, entitiesData) {
            // used by Projectile and Particle
            @Override
            public SpriteSheet makeSpriteSheet(EntityData entityData, Image recoloredImage) {
                return mock(SpriteSheet.class);
            }

            @Override
            protected RenderQueueEnrichableWithFacingLogic makeRenderableWithFacingLogic(EntityData entityData, Image recoloredImage, float turnSpeed) {
                return new TestableRenderQueueEnrichableWithFacingLogic(recoloredImage, entityData, turnSpeed);
            }
        };
    }

}
