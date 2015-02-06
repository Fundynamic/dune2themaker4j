package com.fundynamic.d2tm.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TestableImage extends Image {

    private final int width;
    private final int height;
    private final Graphics graphics;

    public TestableImage(int width, int height, Graphics graphics) throws SlickException {
        this.width = width;
        this.height = height;
        this.graphics = graphics;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Graphics getGraphics() throws SlickException {
        return graphics;
    }
}