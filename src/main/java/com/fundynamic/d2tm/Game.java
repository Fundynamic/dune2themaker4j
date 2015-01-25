package com.fundynamic.d2tm;

import com.fundynamic.d2tm.game.state.PlayingState;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Bootstrap;


public class Game extends StateBasedGame {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    public Game(String title) {
        super(title);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new PlayingState(container, new DuneTerrainFactory(new Theme(new Image("sheet_terrain.png")))));
    }

    public static void main(String[] args) {
        Bootstrap.runAsApplication(new Game("Dune II - The Maker"), SCREEN_WIDTH, SCREEN_HEIGHT, false);
    }

}
