package com.fundynamic.d2tm.game.drawing;

import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.Perimeter;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.graphics.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class DrawableViewPort {

    private final Map map;

    private final Graphics graphics;
    private final Image buffer;

    private final Vector2D<Integer> drawingVector;
    private final Vector2D<Integer> screenResolution;

    private final Perimeter<Float> viewingVectorPerimeter;

    private float velocityX;
    private float velocityY;

    private float moveSpeed;

    private Vector2D<Float> viewingVector;


    public DrawableViewPort(Vector2D screenResolution, Vector2D viewingVector, Graphics graphics, Map map, float moveSpeed) throws SlickException {
        this(screenResolution, Vector2D.zero(), viewingVector, graphics, map, moveSpeed);
    }

    public DrawableViewPort(Vector2D<Integer> screenResolution, Vector2D drawingVector, Vector2D viewingVector, Graphics graphics, Map map, float moveSpeed) throws SlickException {
        this.graphics = graphics;
        this.map = map;

        this.drawingVector = drawingVector;
        this.screenResolution = screenResolution;
        this.buffer = constructImage(screenResolution);

        float heightOfMapInPixels = map.getHeight() * Tile.HEIGHT;
        float widthOfMapInPixels = map.getWidth() * Tile.WIDTH;
        this.viewingVectorPerimeter = new Perimeter<>(0F,
                widthOfMapInPixels - screenResolution.getX(),
                0F,
                heightOfMapInPixels - screenResolution.getY());
        this.viewingVector = viewingVector;
        this.velocityX = 0F;
        this.velocityY = 0F;

        this.moveSpeed = moveSpeed;
    }

    public void render() throws SlickException {
        draw();
        graphics.drawString("Drawing viewport at " + drawingVector.shortString() + " viewing " + viewingVector.shortString(), 10, 30);
    }

    public void update() {
        viewingVector = viewingVectorPerimeter.makeSureVectorStaysWithin(viewingVector.move(velocityX, velocityY));
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

    public Vector2D<Float> getViewingVector() {
        return viewingVector;
    }

    public void draw() throws SlickException {
        final Graphics bufferGraphics = this.buffer.getGraphics();
        if (bufferGraphics == null) return; // HACK HACK: this makes sure our tests are happy by not having to stub all the way down these methods...

        bufferGraphics.clear();

        drawViewableMapOnBuffer(viewingVector.toInt(), bufferGraphics);
        // determine what items are visible & draw them on image

        // add more layers
        // Units, etc

        // draw all on the big canvas
        drawBufferToGraphics(graphics, drawingVector);
    }

    private void drawBufferToGraphics(Graphics graphics, Vector2D<Integer> drawingVector) {
        graphics.drawImage(buffer, drawingVector.getX(), drawingVector.getY());
    }

    private void drawViewableMapOnBuffer(Vector2D<Integer> viewingVector, Graphics imageGraphics) throws SlickException {
        imageGraphics.drawImage(
            map.getSubImage(
                    viewingVector.getX(),
                    viewingVector.getY(),
                    screenResolution.getX(),
                    screenResolution.getY()),
            0, 0
        );
    }

    protected Image constructImage(Vector2D<Integer> screenResolution) throws SlickException {
        return new Image(screenResolution.getX(), screenResolution.getY());
    }

}
