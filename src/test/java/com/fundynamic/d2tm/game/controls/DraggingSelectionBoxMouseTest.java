package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapTest;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.SlickException;


public class DraggingSelectionBoxMouseTest {

    @Test
    public void draggingOverNothingSelectsNoUnits() throws SlickException {
        Vector2D startingCoordinates = Vector2D.zero();
        Vector2D draggingCoordinates = Vector2D.create(10, 10);

        Map map = MapTest.makeMap();
        Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);

        Mouse mouse = MouseTest.makeMouse(map, player);

        Viewport viewport = new Viewport(map, mouse, player);

        mouse.setViewport(viewport);

        DraggingSelectionBoxMouse draggingSelectionBoxMouse = new DraggingSelectionBoxMouse(mouse, startingCoordinates);
        draggingSelectionBoxMouse.draggedToCoordinates(draggingCoordinates);

        draggingSelectionBoxMouse.leftButtonReleased();
    }

}