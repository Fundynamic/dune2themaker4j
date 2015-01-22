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

    private float xVelocity;
    private float yVelocity;

    public DrawableViewPort(Viewport viewport, Vector2D drawingVector, Vector2D viewingVector, Graphics graphics) {
        this.viewport = viewport;
        this.graphics = graphics;

        this.drawingVector = drawingVector;
        this.viewingVector = viewingVector;

        this.xVelocity = 0F;
        this.yVelocity = 0F;
    }

    public void render() throws SlickException {
        viewport.draw(graphics, drawingVector, viewingVector);
    }

    public void update() {
        viewingVector = viewingVector.move(xVelocity, yVelocity, 0.5F);
    }

}
