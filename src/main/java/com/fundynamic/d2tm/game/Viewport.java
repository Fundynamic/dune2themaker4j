package com.fundynamic.d2tm.game;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * A Viewport class 'views' the map from a certain X, Y position and with a limited height and width.
 *
 * This class gets the map image, and views a portion of it.
 */
public class Viewport {

    private final Map map;

    private final int width, height;

    private Image buffer;

    public Viewport(int width, int height, Map map) throws SlickException {
        this.map = map;
        this.height = height;
        this.width = width;
        this.buffer = new Image(width, height);
    }

    public void draw(Graphics graphics, Vector2D<Integer> drawingVector, Vector2D<Float> viewingVector) throws SlickException {
        final Graphics bufferGraphics = this.buffer.getGraphics();
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
        final Image mapImage = map.createOrGetMapImage();
        Image subImage = mapImage.getSubImage(viewingVector.getX(), viewingVector.getY(), width, height);
        imageGraphics.drawImage(subImage, 0, 0);
    }

    public Map getMap() {
        return map;
    }
}