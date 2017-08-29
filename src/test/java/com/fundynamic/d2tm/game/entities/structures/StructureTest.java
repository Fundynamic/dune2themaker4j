package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibility;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.Image;

import java.util.List;

import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData.WINDTRAP;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class StructureTest extends AbstractD2TMTest {

    @Test
    public void constructsStructureWithProperSize() {
        int widthInCells = 3;
        int heightInCells = 2;
        Structure structure = makeStructure(player, 100, widthInCells, heightInCells, 30, Coordinate.create(0, 0));
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
        Structure structure = makeStructure(player, 100, Coordinate.create(48, 48));
        structure.select();

        Vector2D viewportVec = Vector2D.create(32, 32);
        RenderQueue renderQueue = new RenderQueue(viewportVec);
        structure.enrichRenderQueue(renderQueue);

        List<RenderQueue.ThingToRender> thingsToRender = renderQueue.getThingsToRender(RenderQueue.ENTITY_GUI_LAYER);
        assertThat(thingsToRender.size(), is(2));

        RenderQueue.ThingToRender first = thingsToRender.get(0);
        assertThat(first.renderQueueEnrichable, is(instanceOf(HitPointBasedDestructibility.class)));
        assertThat(first.screenX, is(16)); // structureX - viewportVecX
        assertThat(first.screenY, is(16)); // structureY - viewportVecY

        RenderQueue.ThingToRender second = thingsToRender.get(1);
        assertThat(second.renderQueueEnrichable, is(instanceOf(FadingSelection.class)));
        assertThat(second.screenX, is(16)); // structureX - viewportVecX
        assertThat(second.screenY, is(16)); // structureY - viewportVecY
    }

    @Test
    public void structureSpawnsUnit() {
        Structure structure = makeStructure(player, 100, MapCoordinate.create(10, 10));
        List<AbstractBuildableEntity> buildList = structure.getBuildList();
        Assert.assertFalse(buildList.isEmpty());

        AbstractBuildableEntity abstractBuildableEntity = buildList.get(0);
        structure.buildEntity(abstractBuildableEntity);

        int size = entityRepository.getEntitiesCount();
        // wait long enough for construction to be completed & spawned
        structure.update(abstractBuildableEntity.getEntityData().buildTimeInSeconds + 1);

        // spawned
        Assert.assertEquals(size + 1, entityRepository.getEntitiesCount());
    }

    @Test
    public void powerProduction() {
        Structure structure = makeStructure(player, MapCoordinate.create(11, 11), WINDTRAP);

        // from test-rules.ini, verify assumptions before doing the real test
        Assert.assertEquals(200, structure.getPowerProduction());
        int hitpoints = 300;
        Assert.assertEquals(hitpoints, structure.getHitPoints());

        int damage = 75;
        structure.takeDamage(damage, null);

        Assert.assertEquals(163, structure.getPowerProduction());

        structure.takeDamage((hitpoints - damage), null);

        // should be at 25% of production
        Assert.assertEquals(50, structure.getPowerProduction());
    }
}