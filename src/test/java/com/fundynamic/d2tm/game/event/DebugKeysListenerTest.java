package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DebugKeysListenerTest {

    public static final char ANY_CHAR = '#';
    public static final int ANY_KEY = Input.KEY_COLON;

    @Mock private BattleField battleField;

    private final Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);
    private DebugKeysListener debugKeysListener;

    @Before
    public void setUp() {
        debugKeysListener = new DebugKeysListener(battleField, player);
    }

    @Test
    public void keyREnablesShroudRevealingForPlayer() throws SlickException {
        Map map = mock(Map.class);
        when(battleField.getMap()).thenReturn(map);

        debugKeysListener.keyPressed(Input.KEY_R, ANY_CHAR);

        verify(map).revealAllShroudFor(player);
    }

    @Test
    public void anyOtherKeyDoesNothing() {
        debugKeysListener.keyPressed(ANY_KEY, ANY_CHAR);
        verifyZeroInteractions(battleField);
    }

    @Test
    public void keyReleaseDoesNothing() {
        debugKeysListener.keyReleased(ANY_KEY, ANY_CHAR);
        verifyZeroInteractions(battleField);
    }
}