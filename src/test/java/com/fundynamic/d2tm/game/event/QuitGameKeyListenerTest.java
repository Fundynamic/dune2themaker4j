package com.fundynamic.d2tm.game.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

@RunWith(MockitoJUnitRunner.class)
public class QuitGameKeyListenerTest {

    @Mock
    private GameContainer gameContainer;

    @Test
    public void exitGameWhenKeyIsESCAPE() {
        QuitGameKeyListener quitGameKeyListener = new QuitGameKeyListener(gameContainer);
        quitGameKeyListener.keyPressed(Input.KEY_ESCAPE, 'E');
        Mockito.verify(gameContainer).exit();
    }

}