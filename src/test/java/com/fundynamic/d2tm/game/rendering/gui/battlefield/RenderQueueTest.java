package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.NullEntity;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;

import java.util.Arrays;


public class RenderQueueTest extends AbstractD2TMTest {


    @Test
    public void putListOfEntities() {
        RenderQueue renderQueue = new RenderQueue(Vector2D.create(10, 10));
        renderQueue.put(Arrays.asList(new NullEntity()));
    }

}