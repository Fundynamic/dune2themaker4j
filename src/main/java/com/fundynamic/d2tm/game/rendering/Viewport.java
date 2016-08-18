package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.Rectangle;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.Perimeter;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Viewport implements Renderable {

    private static final int PIXELS_NEAR_BORDER = 2;


    private final Image buffer;

    private final Vector2D drawingVector;
    private final Vector2D viewportDimensions;
    private final Perimeter viewingVectorPerimeter;

    private final EntityRepository entityRepository;

    private final CellTerrainRenderer cellTerrainRenderer;
    private final CellShroudRenderer cellShroudRenderer;
    private final CellViewportRenderer cellViewportRenderer;
    private final CellDebugInfoRenderer cellDebugInfoRenderer;

    private boolean drawDebugInfo = false;

    private Vector2D velocity;
    private float moveSpeed;

    private Vector2D viewingVector;
    private final Mouse mouse;

    private Map map;

    public Viewport(Map map, Mouse mouse, Player player, Image buffer) throws SlickException {
        this(Game.getResolution(),
                Vector2D.zero(),
                Vector2D.create(32, 32),
                map,
                (float)Game.TILE_SIZE * 30,
                Game.TILE_SIZE,
                mouse,
                player,
                buffer);
    }

    public Viewport(Vector2D viewportDimensions,
                    Vector2D drawingVector,
                    Vector2D viewingVector,
                    Map map,
                    float moveSpeed,
                    int tileSize,
                    Mouse mouse,
                    Player player,
                    Image buffer) throws SlickException {
        this.viewportDimensions = viewportDimensions;
        this.map = map;

        this.drawingVector = drawingVector;
        this.buffer = buffer;

        this.viewingVectorPerimeter = map.createViewablePerimeter(viewportDimensions, tileSize);
        this.viewingVector = viewingVector;
        this.velocity = Vector2D.zero();

        this.moveSpeed = moveSpeed;

        this.cellViewportRenderer = new CellViewportRenderer(map, tileSize, viewportDimensions);
        this.cellTerrainRenderer = new CellTerrainRenderer();
        this.cellShroudRenderer = new CellShroudRenderer(player, map.getShroud());
        this.cellDebugInfoRenderer = new CellDebugInfoRenderer(mouse);

        this.entityRepository = mouse.getEntityRepository();
        this.mouse = mouse;
        this.mouse.setBattlefield(this); // TODO: <-- THIS IS BAD! (get rid of this cyclic reference)
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
            cellViewportRenderer.render(buffer, viewingVector, cellTerrainRenderer);

            // 1) Select entities to draw
            // 2) Decide if there is more to draw (from the entities)
            // 3) draw!
            RenderQueue renderQueue = getEntitiesToDraw();
            renderQueue.render(buffer.getGraphics());

            cellViewportRenderer.render(buffer, viewingVector, cellShroudRenderer);

            // TODO make mouse implement Renderable interface?
            mouse.render(this.buffer.getGraphics());

            if (drawDebugInfo) {
                cellViewportRenderer.render(this.buffer, viewingVector, cellDebugInfoRenderer);
            }

            drawBufferToGraphics(graphics, drawingVector);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // do nothing (TODO: Remove 'renderable' from viewport!?)
    }

    private RenderQueue getEntitiesToDraw() {
        Rectangle rectangle = Rectangle.createWithDimensions(viewingVector.min(Vector2D.create(32, 32)), viewportDimensions.add(Vector2D.create(64, 64)));

        RenderQueue renderQueue = new RenderQueue(this.viewingVector);
        renderQueue.put(entityRepository.findEntitiesWithinRectangle(rectangle).toList());

        return renderQueue;
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

    public Coordinate translateViewportCoordinateToAbsoluteMapCoordinate(Vector2D positionOnViewport) {
        return new Coordinate(positionOnViewport.add(viewingVector));
    }

    public void toggleDrawDebugMode() {
        drawDebugInfo = !drawDebugInfo;
    }

    public Vector2D getDrawingVector() {
        return drawingVector;
    }

    public Vector2D getViewportDimensions() {
        return viewportDimensions;
    }

    public Coordinate translateScreenToViewportCoordinate(Vector2D screenPosition) {
        Rectangle rect = Rectangle.createWithDimensions(drawingVector, viewportDimensions);
        if (!rect.isVectorWithin(screenPosition)) return null; // outside our viewport!
        return new Coordinate(screenPosition.min(drawingVector));
    }

    public Mouse getMouse() {
        return mouse;
    }
}
