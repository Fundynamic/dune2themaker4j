package com.fundynamic.d2tm.game.drawing;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.Perimeter;
import com.fundynamic.d2tm.game.map.renderer.MapRenderer;
import com.fundynamic.d2tm.game.map.renderer.ShroudRenderer;
import com.fundynamic.d2tm.game.map.renderer.TerrainCellRenderer;
import com.fundynamic.d2tm.game.math.Random;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.*;

public class Viewport {

    private final Graphics graphics;
    private final Image buffer;

    private final Vector2D drawingVector;

    private final Perimeter viewingVectorPerimeter;
    private SpriteSheet constyard;

    private Vector2D velocity;

    private float moveSpeed;

    private Vector2D viewingVector;
    private MapRenderer mapRenderer;
    private final TerrainCellRenderer terrainCellRenderer;
    private final ShroudRenderer shroudRenderer;


    public Viewport(Vector2D viewportDimensions,
                    Vector2D drawingVector,
                    Vector2D viewingVector,
                    Graphics graphics,
                    Map map,
                    float moveSpeed,
                    int tileWidth,
                    int tileHeight) throws SlickException {
        this.graphics = graphics;

        this.drawingVector = drawingVector;
        this.buffer = constructImage(viewportDimensions);

        this.viewingVectorPerimeter = map.createViewablePerimeter(viewportDimensions, tileWidth, tileHeight);
        this.viewingVector = viewingVector;
        this.velocity = Vector2D.zero();

        this.moveSpeed = moveSpeed;

        this.mapRenderer = new MapRenderer(tileHeight, tileWidth, viewportDimensions);
        this.terrainCellRenderer = new TerrainCellRenderer(map);
        this.shroudRenderer = new ShroudRenderer(map);

        constyard = null;
        try {
            constyard = new SpriteSheet(new Image("structures/2x2_constyard.png"), 64, 64);
        } catch (Throwable t) {
            // swallow for now...
        }
    }

    public void render() throws SlickException {
        final Graphics bufferGraphics = this.buffer.getGraphics();
        if (bufferGraphics == null) return; // HACK HACK: this makes sure our tests are happy by not having to stub all the way down these methods...

        mapRenderer.render(this.buffer, viewingVector, terrainCellRenderer);
//        mapRenderer.render(this.buffer, viewingVector, shroudRenderer);

        bufferGraphics.drawImage(constyard.getSprite(0, Random.getRandomBetween(0, 2)), 100, 100);
        drawBufferToGraphics(graphics, drawingVector);
    }

    public void update() {
        Vector2D newViewingVector = viewingVectorPerimeter.makeSureVectorStaysWithin(viewingVector.move(velocity));
        viewingVector = newViewingVector;
    }

    public void moveLeft() {
        this.velocity = new Vector2D(-moveSpeed, this.velocity.getY());
    }

    public void moveRight() {
        this.velocity = new Vector2D(moveSpeed, this.velocity.getY());
    }

    public void moveUp() {
        this.velocity = new Vector2D(this.velocity.getX(), -moveSpeed);
    }

    public void moveDown() {
        this.velocity = new Vector2D(this.velocity.getX(), moveSpeed);
    }

    public void stopMovingHorizontally() {
        this.velocity = new Vector2D(0, this.velocity.getY());
    }

    public void stopMovingVertically() {
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

}
