package com.fundynamic.d2tm.game;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

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

    public void draw(Graphics graphics, Vector2D drawingVector, Vector2D viewingVector) throws SlickException {
        final Graphics bufferGraphics = this.buffer.getGraphics();
        bufferGraphics.clear();

        drawViewableMapOnBuffer(viewingVector, bufferGraphics);
        // determine what items are visible & draw them on image

        // add more layers


        bufferGraphics.setColor(Color.white);
        bufferGraphics.drawRect(0, 0, (this.buffer.getWidth() - 1), (this.buffer.getHeight() - 1));

        // draw all on the big canvas
        drawBufferToGraphics(graphics, drawingVector);
    }

    private void drawBufferToGraphics(Graphics graphics, Vector2D drawingVector) {
        graphics.drawImage(buffer, drawingVector.getX(), drawingVector.getY());
    }

    private void drawViewableMapOnBuffer(Vector2D viewingVector, Graphics imageGraphics) throws SlickException {
        final Image mapImage = map.getMapImage();
        Image subImage = mapImage.getSubImage(viewingVector.getX(), viewingVector.getY(), width, height);
        imageGraphics.drawImage(subImage, 0, 0);
    }
}