package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.TestableEntityRepository;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.mock;


public class DraggingSelectionBoxMouseTest {

    @Test
    public void draggingOverNothingSelectsNoUnits() throws SlickException {
        Vector2D startingCoordinates = Vector2D.zero();
        Vector2D draggingCoordinates = Vector2D.create(10, 10);

        Map map = new Map(new Shroud(null, Game.TILE_WIDTH, Game.TILE_HEIGHT), 64, 64);

        GameContainer gameContainer = mock(GameContainer.class);
        EntityRepository entityRepository = new TestableEntityRepository(map, new Recolorer());

        Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);
        Mouse mouse = new TestableMouse(player, gameContainer, entityRepository);
        Viewport viewport = new Viewport(map, mouse, player);

        mouse.setViewport(viewport);

        DraggingSelectionBoxMouse draggingSelectionBoxMouse = new DraggingSelectionBoxMouse(mouse, startingCoordinates);
        draggingSelectionBoxMouse.draggedToCoordinates(draggingCoordinates);

        draggingSelectionBoxMouse.leftButtonReleased();
    }

}