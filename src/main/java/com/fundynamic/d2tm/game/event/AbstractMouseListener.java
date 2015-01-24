package com.fundynamic.d2tm.game.event;


import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

public abstract class AbstractMouseListener implements MouseListener {

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
