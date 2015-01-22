package com.fundynamic.d2tm.game.event;


import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public abstract class AbstractKeyListener implements KeyListener {

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }
}
