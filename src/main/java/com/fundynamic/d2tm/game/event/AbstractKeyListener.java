package com.fundynamic.d2tm.game.event;


import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public abstract class AbstractKeyListener implements KeyListener {

    @Override
    public void setInput(Input input) {
        // Slick docs: Set the input that events are being sent from
    }

    @Override
    public boolean isAcceptingInput() {
        // Slick docs: Check if this input listener is accepting input
        return true; // well yes, we do of course.
    }

    @Override
    public void inputEnded() {
        // default behavior: don't do anything with this
    }

    @Override
    public void inputStarted() {
        // Slick docs: Notification that input is about to be processed
        // Our default behavior: don't do anything with this
    }
}
