package com.fundynamic.d2tm.game.drawing;

import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.math.Vector2D;
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
    }

    public void update() {
        viewingVector = viewingVector.move(velocityX, velocityY, moveSpeed);
    }

    public void moveLeft(float velocity) {
        this.velocityX = -velocity;
    }

    public void moveRight(float velocity) {
        this.velocityX = velocity;
    }

    public void moveUp(float velocity) {
        this.velocityY = -velocity;
    }
}
