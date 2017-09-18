package com.fundynamic.d2tm.game.rendering.gui.sidebar;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.*;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

public class MiniMap extends GuiElement {

    private final BattleField battleField;
    private final EntityRepository entityRepository;
    private final Map map;
    private final Player player;

    private final Rectangle renderPosition;
    private final float renderScale;

    private float elapsedTime = 0f;
    private boolean redrawMiniMap;
    private Image unscaledMiniMapImage;

    private Vector2D mouseCoordinates;

    public MiniMap(int x, int y, int width, int height, BattleField battleField, EntityRepository entityRepository, Map map, Player player) {
        super(x, y, width, height);

        this.battleField = battleField;
        this.entityRepository = entityRepository;
        this.map = map;
        this.player = player;

        this.redrawMiniMap = true;
        this.renderPosition = getRenderPosition();
        this.renderScale = getRenderScale(Vector2D.create(map.getWidth(), map.getHeight()));
    }

    private Rectangle getRenderPosition() {
        Vector2D mapDimensions = new Vector2D(map.getWidth() * TILE_SIZE, map.getHeight() * TILE_SIZE);
        return this.scaleContainCenter(mapDimensions);
    }

    private float getRenderScale(Vector2D mapDimensions) {
        if (mapDimensions.getX() > mapDimensions.getY()) {
            return getWidth() / mapDimensions.getX();
        } else {
            return getHeight() / mapDimensions.getY();
        }
    }

    @Override
    public void update(float deltaInSeconds) {
        elapsedTime += deltaInSeconds;
        if (elapsedTime >= 0.5f) {
            redrawMiniMap = true;
            elapsedTime = 0;
        }
    }

    @Override
    public void render(Graphics graphics) {
        // clear background
        graphics.setColor(Color.black);
        graphics.fillRect(getTopLeftX(), getTopLeftY(), getWidth(), getHeight());

        if (player.isHasRadar()) {
            if (!player.isLowPower()) {
                // render minimap
                Image unscaledMiniMapImage = getMaybeRedrawnOrStaleMiniMapImage();
                unscaledMiniMapImage.draw(
                        renderPosition.getTopLeftX(), renderPosition.getTopLeftY(),
                        renderPosition.getWidth(), renderPosition.getHeight());
            }

            // render viewport outline
            drawViewportOutline(graphics);
        }
    }

    private Image getMaybeRedrawnOrStaleMiniMapImage() {
        if (redrawMiniMap) {
            // free memory of previously generated mini-map image
            if (unscaledMiniMapImage != null) {
                try {
                    unscaledMiniMapImage.destroy();
                } catch (SlickException e) {
                    throw new RuntimeException(e);
                }
            }

            // regenerate the minimap
            unscaledMiniMapImage = createMiniMapImage();
            redrawMiniMap = false;
        }
        return unscaledMiniMapImage;
    }

    private Image createMiniMapImage() {
        final int width = map.getWidth();
        final int height = map.getHeight();

        ImageBuffer buffer = new ImageBuffer(width, height);

        // render terrain
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // skip shrouded terrain
                Cell cell = map.getCell(x + 1, y + 1);
                if (player.isShrouded(cell.getMapCoordinate())) {
                    continue;
                }

                final Color terrainColor = cell.getTerrainColor();
                buffer.setRGBA(x, y, terrainColor.getRed(), terrainColor.getGreen(), terrainColor.getBlue(), 255);
            }
        }

        // render entities
        for(Entity entity : entityRepository.findAliveEntitiesWithinPlayableMapBoundariesOfType(EntityType.STRUCTURE, EntityType.UNIT)) {
            for(MapCoordinate mapCoordinate : entity.getAllCellsAsMapCoordinates()) {
                // skip shrouded entities
                if (player.isShrouded(mapCoordinate)) {
                    continue;
                }

                // use one pixel offset, because map coordinates are one-based
                int x = mapCoordinate.getXAsInt() - 1;
                int y = mapCoordinate.getYAsInt() - 1;
                Color factionColor = entity.getPlayer().getFactionColor();
                buffer.setRGBA(x, y, factionColor.getRed(), factionColor.getGreen(), factionColor.getBlue(), 255);
            }
        }

        return buffer.getImage(Image.FILTER_NEAREST);
    }

    private void drawViewportOutline(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.setLineWidth(1f);

        Rectangle boundaries = battleField.getViewportCellBoundaries();

        // x and y are offset by 1 because the boundaries are 1-based
        float x = renderPosition.getTopLeftX() + (boundaries.getTopLeftX() - 1) * renderScale;
        float y = renderPosition.getTopLeftY() + (boundaries.getTopLeftY() - 1) * renderScale;
        graphics.drawRect(x, y, (boundaries.getWidth() * renderScale) -1f, (boundaries.getHeight() * renderScale) -1f);
    }

    @Override
    public void leftClicked() {
        MapCoordinate clickedCoordinate = mouseCoordinates
            .min(renderPosition.getTopLeft()) // convert to relative coordinates
            .scale(1f / renderScale) // convert to map coordinates
            .add(new Vector2D(1, 1)) // and offset by one, because of the invisible border
            .asMapCoordinate();
        battleField.centerViewportOn(clickedCoordinate);
    }

    @Override
    public void rightClicked() {
        // No action on right-click
    }

    @Override
    public void leftButtonReleased() {
        // No action when left-click is released
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        mouseCoordinates = coordinates;
        leftClicked();
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        mouseCoordinates = coordinates;
    }
}
