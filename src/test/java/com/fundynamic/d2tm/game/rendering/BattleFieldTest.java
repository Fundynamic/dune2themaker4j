package com.fundynamic.d2tm.game.rendering;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.gui.GuiComposite;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.AssertHelper.assertFloatEquals;
import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;


public class BattleFieldTest extends AbstractD2TMTest {

    public static final int ANY_COORDINATE_NOT_NEAR_BORDER = 100;

    @Test
    public void renderSmokeTest() throws SlickException {
        makeUnit(player, Coordinate.create(2, 2), "QUAD");
        map.revealAllShroudFor(player);

        battleField.render(graphics);
    }

    @Test
    public void translatesScreenCoordinatesIntoAbsoluteMapCoordinates() {
        Vector2D mouseCoordinates = Vector2D.create(87, 73);
        battleField.movedTo(mouseCoordinates);

        Cell hoverCell = battleField.getHoverCell();
        // Y = (73 - 42) / 32 = 0.96 == 1
        // X = (87 - 0) / 32 = 2,71 = 3
        Assert.assertEquals(3, hoverCell.getX());
        Assert.assertEquals(1, hoverCell.getY());
    }

    @Test
    public void mouseHittingLeftBorderOfScreenMovesViewportToLeft() throws SlickException {
        // make sure the viewport can move around
        battleField.setViewingVector(Vector2D.create(64, 64));

        // moved to 2 pixels close to the left of the screen
        listener.mouseMoved(3, ANY_COORDINATE_NOT_NEAR_BORDER, GuiComposite.PIXELS_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D initialViewport = getLastCalledViewport();

        Vector2D viewportVectorAfterUpdate = updateAndRenderAndReturnNewViewportVector();

        assertFloatEquals(initialViewport.getXAsInt() - battleFieldMoveSpeed, viewportVectorAfterUpdate.getX());
        assertFloatEquals(initialViewport.getYAsInt(), viewportVectorAfterUpdate.getY());
    }


    @Test
    public void mouseHittingRightBorderOfScreenMovesViewportToRight() throws SlickException {
        // make sure the viewport can move around
        battleField.setViewingVector(Vector2D.create(64, 64));

        // moved to 2 pixels close to the right of the screen
        listener.mouseMoved(screenResolution.getXAsInt() - 3, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getXAsInt() - 2, ANY_COORDINATE_NOT_NEAR_BORDER);

        Vector2D initialViewport = getLastCalledViewport();

        Vector2D viewportVector = updateAndRenderAndReturnNewViewportVector();

        assertFloatEquals(initialViewport.getXAsInt() + battleFieldMoveSpeed, viewportVector.getX());
        assertFloatEquals(initialViewport.getYAsInt(), viewportVector.getY());
    }


    @Test
    public void mouseHittingTopOfScreenMovesViewportUp() throws SlickException {
        // make sure the viewport can move around
        battleField.setViewingVector(Vector2D.create(64, 64));

        // moved to 2 pixels close to the top of the screen
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, 3, ANY_COORDINATE_NOT_NEAR_BORDER, 2);

        Vector2D initialViewport = getLastCalledViewport();

        Vector2D viewportVector = updateAndRenderAndReturnNewViewportVector();

        assertFloatEquals(initialViewport.getXAsInt(), viewportVector.getX());
        assertFloatEquals(initialViewport.getYAsInt() - battleFieldMoveSpeed, viewportVector.getY());
    }

    @Test
    public void mouseHittingBottomOfScreenMovesViewportDown() throws SlickException {
        // moved to 2 pixels close to the bottom of the screen
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getYAsInt() - 3, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getYAsInt() - 2);

        Vector2D initialViewport = getLastCalledViewport();

        Vector2D viewportVector = updateAndRenderAndReturnNewViewportVector();

        assertFloatEquals(initialViewport.getXAsInt(), viewportVector.getX());
        assertFloatEquals(initialViewport.getYAsInt() + battleFieldMoveSpeed, viewportVector.getY());
    }

    @Test
    public void mouseFromTopLeftToNoBorderStopsMoving() throws SlickException {
        // this updates viewport coordinates one move up and to the left
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, 0, 0);
        updateAndRender(); // and move the viewport
        Vector2D tickOneViewportVector = battleField.getViewingVector();

        // move back to the middle
        listener.mouseMoved(0, 0, ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
        updateAndRender(); // and stop moving

        // Check that the viewport stopped moving
        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved while expected to have stopped", tickOneViewportVector.getY(), viewportVector.getY());
        assertFloatEquals("X position moved while expected to have stopped", tickOneViewportVector.getX(), viewportVector.getX());
    }

    @Test
    public void mouseFromBottomRightToNoBorderStopsMoving() throws SlickException {
        // this updates viewport coordinates one move up and to the left
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getXAsInt(), screenResolution.getYAsInt());
        updateAndRender(); // and move the viewport
        Vector2D tickOneViewportVector = battleField.getViewingVector();

        // move back to the middle
        listener.mouseMoved(screenResolution.getXAsInt(), screenResolution.getYAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER, ANY_COORDINATE_NOT_NEAR_BORDER);
        updateAndRender(); // and stop moving

        // Check that the viewport stopped moving
        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved while expected to have stopped", tickOneViewportVector.getY(), viewportVector.getY());
        assertFloatEquals("X position moved while expected to have stopped", tickOneViewportVector.getX(), viewportVector.getX());
    }


    @Test
    public void stopsMovingLeftWhenAtTheLeftEdge() throws SlickException {
        listener.mouseMoved(0, ANY_COORDINATE_NOT_NEAR_BORDER, 0, ANY_COORDINATE_NOT_NEAR_BORDER); // move left
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("X position moved over the edge", 32F, viewportVector.getX());
    }

    @Test
    public void stopsMovingUpWhenAtTheUpperEdge() throws SlickException {
        listener.mouseMoved(ANY_COORDINATE_NOT_NEAR_BORDER, 0, ANY_COORDINATE_NOT_NEAR_BORDER, 0); // move up
        updateAndRender();
        updateAndRender();
        updateAndRender();

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved over the edge", 32F, viewportVector.getY());
    }

    @Test
    public void stopsMovingDownWhenAtTheBottomEdge() throws SlickException {
        Vector2D battleFieldSize = battleField.getSize();
        float maxYViewportPosition = ((MAP_HEIGHT * TILE_SIZE) - TILE_SIZE) - battleFieldSize.getY();

        battleField.setViewingVector(Vector2D.create(64, maxYViewportPosition - 1));

        listener.mouseMoved(
                ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getYAsInt(), // old
                ANY_COORDINATE_NOT_NEAR_BORDER, screenResolution.getYAsInt()  // new coordinate
        ); // move down

        updateAndRender();
        updateAndRender(); // regardless
        updateAndRender(); // it should not go over the edge

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("Y position moved over the bottom", maxYViewportPosition, viewportVector.getY());
    }

    @Test
    public void stopsMovingRightWhenAtTheRightEdge() throws SlickException {
        Vector2D battleFieldSize = battleField.getSize();

        float maxXViewportPosition = ((MAP_WIDTH * TILE_SIZE) - TILE_SIZE) - battleFieldSize.getX();

        battleField.setViewingVector(Vector2D.create(maxXViewportPosition - 1, 64));

        listener.mouseMoved(
                screenResolution.getXAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER, // old
                screenResolution.getXAsInt(), ANY_COORDINATE_NOT_NEAR_BORDER  // new coordinate
        ); // move right

        updateAndRender();
        updateAndRender(); // regardless
        updateAndRender(); // it should not go over the edge

        Vector2D viewportVector = getLastCalledViewport();
        assertFloatEquals("X position moved over the right", maxXViewportPosition, viewportVector.getX());
    }

    private Vector2D updateAndRenderAndReturnNewViewportVector() throws SlickException {
        updateAndRender();
        return getLastCalledViewport();
    }

    private void updateAndRender() throws SlickException {
        guiComposite.update(ONE_FRAME_PER_SECOND_DELTA);
        guiComposite.render(graphics);
    }

    private Vector2D getLastCalledViewport() throws SlickException {
        return battleField.getViewingVector();
    }

}