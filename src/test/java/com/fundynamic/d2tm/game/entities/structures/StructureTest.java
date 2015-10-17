package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibility;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.rendering.RenderQueue;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.newdawn.slick.Image;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StructureTest extends AbstractD2TMTest {

    @Test
    public void constructsStructureWithProperSize() {
        int widthInCells = 3;
        int heightInCells = 2;
        Structure structure = makeStructure(player, 100, widthInCells, heightInCells, 30, Vector2D.zero());
        assertThat(structure.getWidthInCells(), is(3));
        assertThat(structure.getHeightInCells(), is(2));
    }

    @Test
    public void isEntityTypeStructure() {
        Structure structure = makeStructure(player, 100);
        assertThat(structure.getEntityType(), is(EntityType.STRUCTURE));
    }

    @Test
    public void render() {
        Structure structure = makeStructure(player, 100);
        structure.render(graphics, 10, 10);
        verify(graphics).drawImage(any(Image.class), eq(10f), eq(10f));
    }

    @Test
    public void selectedStructurePutsFadingSelectionAndHealthBarOnRenderQueue() {
        Structure structure = makeStructure(player, 100, Vector2D.create(48, 48));
        structure.select();

        Vector2D viewportVec = Vector2D.create(32, 32);
        RenderQueue renderQueue = new RenderQueue(viewportVec);
        structure.enrichRenderQueue(renderQueue);

        List<RenderQueue.ThingToRender> thingsToRender = renderQueue.getThingsToRender(RenderQueue.ENTITY_GUI_LAYER);
        assertThat(thingsToRender.size(), is(2));

        RenderQueue.ThingToRender first = thingsToRender.get(0);
        assertThat(first.renderable, is(instanceOf(HitPointBasedDestructibility.class)));
        assertThat(first.x, is(16)); // structureX - viewportVecX
        assertThat(first.y, is(16)); // structureY - viewportVecY

        RenderQueue.ThingToRender second = thingsToRender.get(1);
        assertThat(second.renderable, is(instanceOf(FadingSelection.class)));
        assertThat(second.x, is(16)); // structureX - viewportVecX
        assertThat(second.y, is(16)); // structureY - viewportVecY
    }
}