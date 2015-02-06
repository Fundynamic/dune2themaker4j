package com.fundynamic.d2tm.game.drawing;

import com.fundynamic.d2tm.game.map.*;
import com.fundynamic.d2tm.game.map.renderer.MapRenderer;
import com.fundynamic.d2tm.game.map.renderer.ShroudRenderer;
import com.fundynamic.d2tm.game.map.renderer.TerrainCellRenderer;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Viewport {

    private final Graphics graphics;
    private final Image buffer;

    private final Vector2D drawingVector;
    private final Vector2D screenResolution;

    private final Perimeter viewingVectorPerimeter;

    private float velocityX;
    private float velocityY;

    private float moveSpeed;

    private Vector2D viewingVector;
    private MapRenderer mapRenderer;
    private final TerrainCellRenderer terrainCellRenderer;
    private final ShroudRenderer shroudRenderer;


    public Viewport(Vector2D screenResolution,
                    Vector2D drawingVector,
                    Vector2D viewingVector,
                    Graphics graphics,
                    Map map,
                    float moveSpeed,
                    int tileWidth,
                    int tileHeight) throws SlickException {
        this.graphics = graphics;

        this.drawingVector = drawingVector;
        this.screenResolution = screenResolution;
        this.buffer = constructImage(screenResolution);

        this.viewingVectorPerimeter = map.createViewablePerimeter(screenResolution, tileWidth, tileHeight);
        this.viewingVector = viewingVector;
        this.velocityX = 0F;
        this.velocityY = 0F;

        this.moveSpeed = moveSpeed;
        this.mapRenderer = new MapRenderer(tileHeight, tileWidth, screenResolution);

        this.terrainCellRenderer = new TerrainCellRenderer(map);
        this.shroudRenderer = new ShroudRenderer(map);
    }

    public void render() throws SlickException {
        final Graphics bufferGraphics = this.buffer.getGraphics();
        if (bufferGraphics == null) return; // HACK HACK: this makes sure our tests are happy by not having to stub all the way down these methods...

        mapRenderer.render(this.buffer, viewingVector, terrainCellRenderer);
        mapRenderer.render(this.buffer, viewingVector, shroudRenderer);

        drawBufferToGraphics(graphics, drawingVector);
    }

    public void update() {
        Vector2D newViewingVector = viewingVectorPerimeter.makeSureVectorStaysWithin(viewingVector.move(velocityX, velocityY));
        viewingVector = newViewingVector;
    }

    public void moveLeft() {
        this.velocityX = -moveSpeed;
    }

    public void moveRight() {
        this.velocityX = moveSpeed;
    }

    public void moveUp() {
        this.velocityY = -moveSpeed;
    }

    public void moveDown() {
        this.velocityY = moveSpeed;
    }

    public void stopMovingHorizontally() {
        this.velocityX = 0F;
    }

    public void stopMovingVertically() {
        this.velocityY = 0F;
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
