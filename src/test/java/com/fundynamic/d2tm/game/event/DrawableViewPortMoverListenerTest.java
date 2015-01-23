package com.fundynamic.d2tm.game.event;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.Viewport;
import com.fundynamic.d2tm.game.drawing.DrawableViewPort;
import com.fundynamic.d2tm.game.math.Vector2D;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.Game.SCREEN_WIDTH;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DrawableViewPortMoverListenerTest {

    public static final float MOVE_SPEED = 1.0F;
    public static final float SCROLL_SPEED = 2.0F;

    public static final int ANY_COORDINATE_NOT_NEAR_BORDER = 100;

    @Mock
    private Viewport viewport;
    private DrawableViewPort drawableViewPort;
    private DrawableViewPortMoverListener listener;

    @Before
    public void setUp() {
        drawableViewPort = new DrawableViewPort(viewport, Vector2D.zero(), Vector2D.zero(), mock(Graphics.class), MOVE_SPEED);
        listener = new DrawableViewPortMoverListener(drawableViewPort, SCROLL_SPEED);
    }

    @Test
    public void mouseHittingLeftBorderOfScreenMovesViewportToLeft() throws SlickException {
        // moved to 2 pixels close to the left of the screen
        listener.mouseMoved(3, ANY_COORDINATE_NOT_NEAR_BORDER, 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals(-MOVE_SPEED * SCROLL_SPEED, viewportVector.getX(), 0.0001F);
    }

    @Test
    public void mouseHittingRightBorderOfScreenMovesViewportToRight() throws SlickException {
        // moved to 2 pixels close to the right of the screen
        listener.mouseMoved(SCREEN_WIDTH - 3, ANY_COORDINATE_NOT_NEAR_BORDER, SCREEN_WIDTH - 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D<Float> viewportVector = updateAndRenderAndReturnNewViewportVector();

        Assert.assertEquals(MOVE_SPEED * SCROLL_SPEED, viewportVector.getX(), 0.0001F);
    }

    private Vector2D<Float> updateAndRenderAndReturnNewViewportVector() throws SlickException {
        drawableViewPort.update();
        drawableViewPort.render();

        ArgumentCaptor<Vector2D> captor = ArgumentCaptor.forClass(Vector2D.class);
        Mockito.verify(viewport).draw(Mockito.<Graphics>anyObject(), Mockito.<Vector2D<Integer>>anyObject(), captor.capture());

        return captor.getValue();
    }

}