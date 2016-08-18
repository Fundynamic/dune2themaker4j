package com.fundynamic.d2tm.game;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.TestableMouse;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.RenderableWithFacingLogic;
import com.fundynamic.d2tm.game.entities.units.TestableRenderableWithFacingLogic;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.entities.units.UnitFacings;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.rendering.Viewport;
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

    protected Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);
    protected Map map;
    protected Viewport viewport;

    protected Mouse mouse;

    @Before
    public void setUp() throws SlickException {
        map = makeMap(MAP_WIDTH, MAP_HEIGHT); // create a default map
        imageRepository = makeImageRepository();
        entitiesDataReader = makeEntitiesDataReader();
        entitiesData = entitiesDataReader.fromRulesIni();
        entityRepository = makeTestableEntityRepository(map, entitiesData);

        mouse = makeTestableMouse(player, entityRepository);
        viewport = new Viewport(map, mouse, player, mock(Image.class));

        mouse.setBattlefield(viewport);

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
     * @param player - used in TestableMouse
     * @param entityRepository
     * @return
     * @throws SlickException
     */
    public Mouse makeTestableMouse(Player player, EntityRepository entityRepository) throws SlickException {
        GameContainer gameContainer = mock(GameContainer.class);
        return new TestableMouse(player, gameContainer, entityRepository);
    }

    // MAP
    ////////////////////////////////////////////////////////////////////////////////
    public Map makeMap(int width, int height) throws SlickException {
        return new Map(new Shroud(null, Game.TILE_SIZE) {
            @Override
            public SpriteSheet createSpriteSheetFromImage() {
                return mock(SpriteSheet.class);
            }
        }, width, height) {
            @Override
            public Cell getCell(int x, int y) {
                Cell cell = super.getCell(x, y);
                cell.setTileImage(mock(Image.class)); // TODO: get rid of SUPER UGLY WAY TO HIJACK INTO RENDERING STUFF
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
    public Unit makeUnit(UnitFacings facing, Coordinate coordinate) {
        return makeUnit(facing, coordinate, Vector2D.zero());
    }

    public Unit makeUnit(UnitFacings facing, Coordinate coordinate, Vector2D offset) {
        Unit unit = makeUnit(player, coordinate, "QUAD");
        unit.setFacing(facing.getValue());
        unit.setOffset(offset);
        return unit;
    }

    public Unit makeUnit(Player player) {
        return makeUnit(player, Coordinate.create(0, 0), "QUAD");
    }

    public Unit makeUnit(Player player, Coordinate coordinate, String id) {
        if (entityRepository == null) throw new IllegalStateException("You forgot to set up the entityRepository, probably you need to do super.setUp()");
        if (map == null) throw new IllegalStateException("You forgot to set up the map, probably you need to do super.setUp()");
        return (Unit) entityRepository.placeOnMap(coordinate, EntityType.UNIT, id, player);
    }

    // PROJECTILE
    ////////////////////////////////////////////////////////////////////////////////
    public Projectile makeProjectile(Coordinate coordinate) {
        return entityRepository.placeProjectile(coordinate, "LARGE_ROCKET", player);
    }


    public EntityRepository getTestableEntityRepository() {
        return entityRepository;
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
            protected RenderableWithFacingLogic makeRenderableWithFacingLogic(EntityData entityData, Image recoloredImage, float turnSpeed) {
                return new TestableRenderableWithFacingLogic(recoloredImage, entityData, turnSpeed);
            }
        };
    }

}
