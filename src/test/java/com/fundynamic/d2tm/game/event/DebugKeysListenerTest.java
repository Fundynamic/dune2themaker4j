package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.PlacingStructureMouse;
import com.fundynamic.d2tm.game.controls.PlacingUnitMouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.rendering.Viewport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DebugKeysListenerTest {

    public static final char ANY_CHAR = '#';
    public static final int ANY_KEY = Input.KEY_COLON;

    @Mock private Mouse mouse;
    @Mock private Viewport viewport;
    @Mock private EntityRepository entityRepository; // we want a mock here

    private final Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);
    private DebugKeysListener debugKeysListener;

    @Before
    public void setUp() {
        debugKeysListener = new DebugKeysListener(mouse, viewport, entityRepository, player);
    }

    @Test
    public void keySEnablesPlacingStructure() {
        debugKeysListener.keyPressed(Input.KEY_S, ANY_CHAR);

        verify(mouse).setMouseBehavior(isA(PlacingStructureMouse.class));
        verifyZeroInteractions(viewport);
    }

    @Test
    public void keyUEnablesPlacingUnit() {
        debugKeysListener.keyPressed(Input.KEY_U, ANY_CHAR);

        verify(mouse).setMouseBehavior(isA(PlacingUnitMouse.class));
        verifyZeroInteractions(viewport);
    }

    @Test
    public void keyDEnablesDrawDebugMode() {
        debugKeysListener.keyPressed(Input.KEY_D, ANY_CHAR);

        verifyZeroInteractions(mouse);
        verify(viewport).toggleDrawDebugMode();
    }

    @Test
    public void keyREnablesShroudRevealingForPlayer() throws SlickException {
        Map map = mock(Map.class);
        when(viewport.getMap()).thenReturn(map);

        debugKeysListener.keyPressed(Input.KEY_R, ANY_CHAR);

        verifyZeroInteractions(mouse);
        verify(map).revealAllShroudFor(player);
    }

    @Test
    public void anyOtherKeyDoesNothing() {
        debugKeysListener.keyPressed(ANY_KEY, ANY_CHAR);
        verifyZeroInteractions(mouse);
        verifyZeroInteractions(viewport);
        verifyZeroInteractions(entityRepository);
    }

    @Test
    public void keyReleaseDoesNothing() {
        debugKeysListener.keyReleased(ANY_KEY, ANY_CHAR);
        verifyZeroInteractions(mouse);
        verifyZeroInteractions(viewport);
        verifyZeroInteractions(entityRepository);
    }
}