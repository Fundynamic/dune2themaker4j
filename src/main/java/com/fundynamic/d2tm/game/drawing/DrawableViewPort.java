package com.fundynamic.d2tm.game.drawing;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.graphics.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class DrawableViewPort {

    private final Viewport viewport;
    private final Graphics graphics;

    private final Vector2D<Integer> drawingVector;
    private Vector2D<Float> viewingVector;

    private float velocityX;
    private float velocityY;
    private float moveSpeed;

    public DrawableViewPort(Viewport viewport, Vector2D viewingVector, Graphics graphics, float moveSpeed) {
        this(viewport, Vector2D.zero(), viewingVector, graphics, moveSpeed);
    }

    public DrawableViewPort(Viewport viewport, Vector2D drawingVector, Vector2D viewingVector, Graphics graphics, float moveSpeed) {
        this.viewport = viewport;
        this.graphics = graphics;

        this.drawingVector = drawingVector;
        this.viewingVector = viewingVector;

        this.velocityX = 0F;
        this.velocityY = 0F;

        this.moveSpeed = moveSpeed;
    }

    public DrawableViewPort(Viewport viewport, Vector2D drawingVector, Vector2D viewingVector, Graphics graphics) {
        this(viewport, drawingVector, viewingVector, graphics, 0.5F);
    }

    public void render() throws SlickException {
        viewport.draw(graphics, drawingVector, viewingVector);
        graphics.drawString("Drawing viewport at " + drawingVector.shortString() + " viewing " + viewingVector.shortString(), 10, 30);
    }

    public void update() {
        viewingVector = viewingVector.move(velocityX, velocityY, moveSpeed);
        if (viewingVector.getX() < 0) {
            viewingVector = new Vector2D<>(0F, viewingVector.getY());
        }
        if (viewingVector.getY() < 0) {
            viewingVector = new Vector2D<>(viewingVector.getX(), 0F);
        }

        Map map = viewport.getMap();
        int heightOfMapInPixels = map.getHeight() * Tile.HEIGHT;

        int pixelsPlusScreenHeight = viewingVector.toInt().getY() + Game.SCREEN_HEIGHT;
        if (pixelsPlusScreenHeight > heightOfMapInPixels) {
            int subscract = pixelsPlusScreenHeight - heightOfMapInPixels;
            viewingVector = new Vector2D<>(viewingVector.getX(), viewingVector.getY() - subscract);
        }

        int widthOfMapInPixels = map.getWidth() * Tile.WIDTH;

        int pixelsPlusScreenWidth = viewingVector.toInt().getX() + Game.SCREEN_WIDTH;
        if (pixelsPlusScreenWidth > widthOfMapInPixels) {
            int subscract = pixelsPlusScreenWidth - widthOfMapInPixels;
            viewingVector = new Vector2D<>(viewingVector.getX() - subscract, viewingVector.getY());
        }

    }

    public void moveLeft() {
        this.velocityX = -1F;
    }

    public void moveRight() {
        this.velocityX = 1F;
    }

    public void moveUp() {
        this.velocityY = -1F;
    }

    public void moveDown() {
        this.velocityY = 1;
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
