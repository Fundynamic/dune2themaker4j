package com.fundynamic.d2tm;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Game extends BasicGame {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    
    public Game() {
        super("Dune II - The Maker");
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        g.drawString("He who controls the spice... controls the universe!", 0, 0);

    }

    @Override
    public void init(GameContainer container) throws SlickException {
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }
    
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
        app.setForceExit(true);
        app.start();
    }

}
