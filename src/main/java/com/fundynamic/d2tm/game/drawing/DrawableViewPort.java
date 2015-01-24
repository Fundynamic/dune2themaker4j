package com.fundynamic.d2tm.game.drawing;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.Perimeter;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.graphics.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class DrawableViewPort {

    private final Viewport viewport;
    private final Graphics graphics;

    private final Vector2D<Integer> drawingVector;
    private Vector2D<Float> viewingVector;

    private final Perimeter<Float> viewingVectorPerimeter;
    private float velocityX;
    private float velocityY;
    private float moveSpeed;

    public DrawableViewPort(Vector2D screenResolution, Viewport viewport, Vector2D viewingVector, Graphics graphics, float moveSpeed) {
        this(screenResolution, viewport, Vector2D.zero(), viewingVector, graphics, moveSpeed);
    }

    public DrawableViewPort(Vector2D<Integer> screenResolution, Viewport viewport, Vector2D drawingVector, Vector2D viewingVector, Graphics graphics, float moveSpeed) {
        this.viewport = viewport;
        this.graphics = graphics;

        this.drawingVector = drawingVector;

        Map map = viewport.getMap();
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
        viewport.draw(graphics, drawingVector, viewingVector);
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
}
