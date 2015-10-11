package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityRepositoryTest;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.graphics.ImageRepositoryTest;
import org.junit.Test;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class MouseTest {

    @Test
    public void createsMouse() throws SlickException {
        Player player = new Player("Stefan", Recolorer.FactionColor.BLUE);
        GameContainer gameContainer = mock(GameContainer.class);
        EntityRepository entityRepository = EntityRepositoryTest.makeTestableEntityRepositoryWithMockedMap();
        ImageRepository imageRepository = ImageRepositoryTest.makeTestableImageRepository();

        Mouse mouse = Mouse.create(player, gameContainer, entityRepository, imageRepository);

        assertThat(mouse, is(notNullValue()));
        assertThat(mouse.getControllingPlayer(), is(player));
        assertThat(mouse.getMouseBehavior(), instanceOf(NormalMouse.class));
        assertThat(mouse.getHoverCell(), is(nullValue()));
    }

}