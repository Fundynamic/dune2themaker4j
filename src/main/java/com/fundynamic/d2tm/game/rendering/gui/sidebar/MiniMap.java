package com.fundynamic.d2tm.game.rendering.gui.sidebar;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

public class MiniMap extends GuiElement {

    private final Map map;
    private final EntityRepository entityRepository;
    private Rectangle renderPosition;
    private Image unscaledTerrainImage;
    private Image unscaledEntityImage;

    private float elapsedTime = 0f;

    public MiniMap(int x, int y, int width, int height, Map map, EntityRepository entityRepository) {
        super(x, y, width, height);

        this.map = map;
        this.entityRepository = entityRepository;
        this.renderPosition = getRenderPosition(map);
        this.unscaledTerrainImage = renderTerrainMiniMap();
        this.unscaledEntityImage = renderEntityMiniMap();
    }

    private Rectangle getRenderPosition(Map map) {
        Vector2D mapDimensions = new Vector2D(map.getWidth() * TILE_SIZE, map.getHeight() * TILE_SIZE);
        return this.scaleContainCenter(mapDimensions);
    }

    @Override
    public void update(float deltaInSeconds) {
        elapsedTime += deltaInSeconds;
        if (elapsedTime >= 0.5f) {
            unscaledTerrainImage = renderTerrainMiniMap();
            unscaledEntityImage = renderEntityMiniMap();
            elapsedTime = 0;
        }
    }

    private Image renderTerrainMiniMap() {
        final int width = map.getWidth();
        final int height = map.getHeight();

        ImageBuffer buffer = new ImageBuffer(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = map.getCell(x + 1, y + 1);
                final Color terrainColor = cell.getTerrainColor();
                buffer.setRGBA(x, y, terrainColor.getRed(), terrainColor.getGreen(), terrainColor.getBlue(), 255);
            }
        }

        Image image = buffer.getImage();
        image.setFilter(Image.FILTER_NEAREST);
        return image;
    }

    private Image renderEntityMiniMap() {
        final int width = map.getWidth();
        final int height = map.getHeight();

        ImageBuffer buffer = new ImageBuffer(width, height);
        for(Entity entity : entityRepository.findAliveEntitiesOfType(EntityType.STRUCTURE, EntityType.UNIT)) {
            for(MapCoordinate mapCoordinate : entity.getAllCellsAsMapCoordinates()) {
                int x = mapCoordinate.getXAsInt(), y = mapCoordinate.getYAsInt();
                switch (entity.getPlayer().getFactionColor()) {
                    case RED:
                        buffer.setRGBA(x, y, 255, 0, 0, 255);
                        break;
                    case GREEN:
                        buffer.setRGBA(x, y, 0, 255, 0, 255);
                        break;
                    case BLUE:
                        buffer.setRGBA(x, y, 0, 0, 255, 255);
                        break;
                }
            }
        }

        Image image = buffer.getImage();
        image.setFilter(Image.FILTER_NEAREST);
        return image;
    }

    @Override
    public void render(Graphics graphics) {
        // clear background
        graphics.setColor(Color.black);
        graphics.fillRect(getTopLeftX(), getTopLeftY(), getWidth(), getHeight());

        // render terrain
        unscaledTerrainImage.draw(
            renderPosition.getTopLeftX(), renderPosition.getTopLeftY(),
            renderPosition.getWidth(), renderPosition.getHeight());
        unscaledEntityImage.draw(
                renderPosition.getTopLeftX(), renderPosition.getTopLeftY(),
                renderPosition.getWidth(), renderPosition.getHeight());

        if (Game.DEBUG_INFO) {
            graphics.drawString("X: " + renderPosition.getTopLeftX() + ", Y:" + renderPosition.getTopLeftY(), getTopLeftX(), getTopLeftY());
            graphics.drawString("W: " + renderPosition.getWidth() + ", H:" + renderPosition.getHeight(), getTopLeftX(), getTopLeftY() + 15);
        }
    }

    @Override
    public void leftClicked() {

    }

    @Override
    public void rightClicked() {

    }

    @Override
    public void leftButtonReleased() {

    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {

    }

    @Override
    public void movedTo(Vector2D coordinates) {

    }
}
