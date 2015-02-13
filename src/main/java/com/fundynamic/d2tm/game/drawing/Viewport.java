package com.fundynamic.d2tm.game.drawing;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.Perimeter;
import com.fundynamic.d2tm.game.map.renderer.*;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Viewport {

    private static final int PIXELS_NEAR_BORDER = 2;

    private final Vector2D viewportDimensions;

    private final Graphics graphics;
    private final Image buffer;

    private final Vector2D drawingVector;

    private final Perimeter viewingVectorPerimeter;

    private final StructureRenderer structureRenderer;
    private final StructureViewportRenderer structureViewportRenderer;

    private final MapCellTerrainRenderer mapCellTerrainRenderer;
    private final MapCellShroudRenderer mapCellShroudRenderer;
    private final MapCellMouseInteractionRenderer mapCellMouseInteractionRenderer;
    private final MapCellViewportRenderer mapCellViewportRenderer;

    private Vector2D velocity;

    private float moveSpeed;

    private Vector2D viewingVector;

    private Map map;

    public Viewport(Vector2D viewportDimensions,
                    Vector2D drawingVector,
                    Vector2D viewingVector,
                    Graphics graphics,
                    Map map,
                    float moveSpeed,
                    int tileWidth,
                    int tileHeight,
                    Mouse mouse) throws SlickException {
        this.viewportDimensions = viewportDimensions;
        this.map = map;
        this.graphics = graphics;

        this.drawingVector = drawingVector;
        this.buffer = constructImage(viewportDimensions);

        this.viewingVectorPerimeter = map.createViewablePerimeter(viewportDimensions, tileWidth, tileHeight);
        this.viewingVector = viewingVector;
        this.velocity = Vector2D.zero();

        this.moveSpeed = moveSpeed;

        this.mapCellViewportRenderer = new MapCellViewportRenderer(map, tileHeight, tileWidth, viewportDimensions);
        this.structureViewportRenderer = new StructureViewportRenderer(map, tileHeight, tileWidth, viewportDimensions);

        this.mapCellTerrainRenderer = new MapCellTerrainRenderer();
        this.mapCellShroudRenderer = new MapCellShroudRenderer(map);
        this.structureRenderer = new StructureRenderer(mouse);
        this.mapCellMouseInteractionRenderer = new MapCellMouseInteractionRenderer(mouse);
    }

    public void render() throws SlickException {
        final Graphics bufferGraphics = this.buffer.getGraphics();
        if (bufferGraphics == null) return; // HACK HACK: this makes sure our tests are happy by not having to stub all the way down these methods...

        mapCellViewportRenderer.render(this.buffer, viewingVector, mapCellTerrainRenderer);
        structureViewportRenderer.render(this.buffer, viewingVector, structureRenderer);
        mapCellViewportRenderer.render(this.buffer, viewingVector, mapCellMouseInteractionRenderer);
//        mapRenderer.render(this.buffer, viewingVector, shroudRenderer);

        drawBufferToGraphics(graphics, drawingVector);
    }

    public void update() {
        Vector2D newViewingVector = viewingVectorPerimeter.makeSureVectorStaysWithin(viewingVector.move(velocity));
        viewingVector = newViewingVector;
    }

    private void moveLeft() {
        this.velocity = new Vector2D(-moveSpeed, this.velocity.getY());
    }

    private void moveRight() {
        this.velocity = new Vector2D(moveSpeed, this.velocity.getY());
    }

    private void moveUp() {
        this.velocity = new Vector2D(this.velocity.getX(), -moveSpeed);
    }

    private void moveDown() {
        this.velocity = new Vector2D(this.velocity.getX(), moveSpeed);
    }

    private void stopMovingHorizontally() {
        this.velocity = new Vector2D(0, this.velocity.getY());
    }

    private void stopMovingVertically() {
        this.velocity = new Vector2D(this.velocity.getX(), 0);
    }

    private void drawBufferToGraphics(Graphics graphics, Vector2D drawingVector) {
        graphics.drawImage(buffer, drawingVector.getX(), drawingVector.getY());
    }

    // These methods are here mainly for (easier) testing. Best would be to remove them if possible - and at the very
    // least not the use them in the non-test code.
    public Vector2D getViewingVector() {
        return viewingVector;
    }

    protected Image constructImage(Vector2D screenResolution) throws SlickException {
        return new Image(screenResolution.getX(), screenResolution.getY());
    }

    public Map getMap() {
        return this.map;
    }

    public void tellAboutNewMousePositions(int newx, int newy) {
        if (newx <= PIXELS_NEAR_BORDER) {
            moveLeft();
        } else if (newx >= viewportDimensions.getX() - PIXELS_NEAR_BORDER) {
            moveRight();
        } else {
            stopMovingHorizontally();
        }

        if (newy <= PIXELS_NEAR_BORDER) {
            moveUp();
        } else if (newy >= viewportDimensions.getY() - PIXELS_NEAR_BORDER) {
            moveDown();
        } else {
            stopMovingVertically();
        }
    }

    /**
     * Takes screen pixel coordinate and translates that into an absolute pixel coordinate on the map
     */
    public int getAbsoluteX(int xPositionOnScreen) {
        return xPositionOnScreen + viewingVector.getX();
    }

    /**
     * Takes screen pixel coordinate and translates that into an absolute pixel coordinate on the map
     */
    public int getAbsoluteY(int yPositionOnScreen) {
        return yPositionOnScreen + viewingVector.getY();
    }
}
