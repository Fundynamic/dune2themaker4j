package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.Perimeter;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Viewport implements Renderable {

    private static final int PIXELS_NEAR_BORDER = 2;

    private final Vector2D viewportDimensions;

    private final Image buffer;

    private final Vector2D drawingVector;

    private final Perimeter viewingVectorPerimeter;

    private final EntityViewportRenderer entityViewportRenderer;

    private final CellTerrainRenderer cellTerrainRenderer;
    private final CellShroudRenderer cellShroudRenderer;
    private final CellViewportRenderer cellViewportRenderer;
    private final CellDebugInfoRenderer cellDebugInfoRenderer;

    private boolean drawDebugInfo = true;

    private Vector2D velocity;
    private float moveSpeed;

    private Vector2D viewingVector;
    private final Mouse mouse;

    private Map map;

    public Viewport(Vector2D viewportDimensions,
                    Vector2D drawingVector,
                    Vector2D viewingVector,
                    Map map,
                    float moveSpeed,
                    int tileWidth,
                    int tileHeight,
                    Mouse mouse,
                    Player player) throws SlickException {
        this.viewportDimensions = viewportDimensions;
        this.map = map;

        this.drawingVector = drawingVector;
        this.buffer = constructImage(viewportDimensions);

        this.viewingVectorPerimeter = map.createViewablePerimeter(viewportDimensions, tileWidth, tileHeight);
        this.viewingVector = viewingVector;
        this.velocity = Vector2D.zero();

        this.moveSpeed = moveSpeed;

        this.cellViewportRenderer = new CellViewportRenderer(map, tileHeight, tileWidth, viewportDimensions);
        this.cellTerrainRenderer = new CellTerrainRenderer();
        this.cellShroudRenderer = new CellShroudRenderer(map, player);
        this.cellDebugInfoRenderer = new CellDebugInfoRenderer(mouse);
        this.mouse = mouse;
        this.mouse.setViewport(this);

        this.entityViewportRenderer = new EntityViewportRenderer(map, tileHeight, tileWidth, viewportDimensions);
    }

    public void render(Graphics graphics) {
        render(graphics, drawingVector.getXAsInt(), drawingVector.getYAsInt());
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        try {
            final Graphics bufferGraphics = this.buffer.getGraphics();
            if (bufferGraphics == null)
                return; // HACK HACK: this makes sure our tests are happy by not having to stub all the way down these methods...

            // TODO: Merge the culling into this viewport class(?)
            cellViewportRenderer.render(this.buffer, viewingVector, cellTerrainRenderer);

            entityViewportRenderer.render(this.buffer, viewingVector);

            cellViewportRenderer.render(this.buffer, viewingVector, cellShroudRenderer);

            mouse.render(this.buffer.getGraphics());

            if (drawDebugInfo) {
                cellViewportRenderer.render(this.buffer, viewingVector, cellDebugInfoRenderer);
            }

            drawBufferToGraphics(graphics, drawingVector);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void update(float delta) {
        Vector2D translation = velocity.scale(delta);
        viewingVector = viewingVectorPerimeter.makeSureVectorStaysWithin(viewingVector.add(translation));
    }

    private void moveLeft() {
        this.velocity = Vector2D.create(-moveSpeed, this.velocity.getY());
    }

    private void moveRight() {
        this.velocity = Vector2D.create(moveSpeed, this.velocity.getY());
    }

    private void moveUp() {
        this.velocity = Vector2D.create(this.velocity.getX(), -moveSpeed);
    }

    private void moveDown() {
        this.velocity = Vector2D.create(this.velocity.getX(), moveSpeed);
    }

    private void stopMovingHorizontally() {
        this.velocity = Vector2D.create(0, this.velocity.getY());
    }

    private void stopMovingVertically() {
        this.velocity = Vector2D.create(this.velocity.getX(), 0);
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
        return new Image(screenResolution.getXAsInt(), screenResolution.getYAsInt());
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

    public Vector2D translateScreenToAbsoluteMapPixels(Vector2D positionOnScreen) {
        return positionOnScreen.add(viewingVector);
    }

    public void toggleDrawDebugMode() {
        drawDebugInfo = !drawDebugInfo;
    }

}
