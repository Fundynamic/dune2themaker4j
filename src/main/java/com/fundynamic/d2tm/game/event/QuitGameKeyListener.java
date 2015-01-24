package com.fundynamic.d2tm.game.event;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class QuitGameKeyListener extends AbstractKeyListener {

    private final GameContainer gameContainer;

    public QuitGameKeyListener(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            gameContainer.exit();
        }
    }

    @Override
    public void keyReleased(int i, char c) {

    }
}
