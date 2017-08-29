package com.fundynamic.d2tm.game.rendering.gui.sidebar;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.state.PlayingState;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.junit.Assert.*;

public class MiniMapTest extends AbstractD2TMTest {

    @Override
    public void setUp() throws SlickException {
        super.setUp();

        MiniMap miniMap = new MiniMap(
            screenResolution.getXAsInt() - PlayingState.WIDTH_OF_SIDEBAR,
            screenResolution.getYAsInt() - PlayingState.HEIGHT_OF_MINIMAP,
            PlayingState.WIDTH_OF_SIDEBAR,
            PlayingState.HEIGHT_OF_MINIMAP,
            battleField, entityRepository, map, player
        );
        guiComposite.addGuiElement(miniMap);
    }

    @Test
    public void clickingOnMiniMapMovesTheViewport() throws Exception {
        int centerOfMiniMapX = screenResolution.getXAsInt() - PlayingState.WIDTH_OF_SIDEBAR / 2;
        int centerOfMiniMapY = screenResolution.getYAsInt() - PlayingState.HEIGHT_OF_MINIMAP / 2;

        Vector2D initialViewingVector = battleField.getViewingVector();

        mouse.movedTo(Coordinate.create(centerOfMiniMapX, centerOfMiniMapY));
        mouse.leftClicked();

        Vector2D resultViewingVector = battleField.getViewingVector();

        assertNotEquals(initialViewingVector, resultViewingVector);
    }
}