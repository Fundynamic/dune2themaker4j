package com.fundynamic.d2tm.game.entities.units;


import com.fundynamic.d2tm.game.types.EntityData;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

public class TestableRenderQueueEnrichableWithFacingLogic extends RenderQueueEnrichableWithFacingLogic {

    public TestableRenderQueueEnrichableWithFacingLogic(Image image, EntityData entityData, float turnSpeed) {
        super(image, entityData, turnSpeed);
    }

    @Override
    public Image getSprite(int x, int y) {
        return Mockito.mock(Image.class);
    }

    @Override
    public int getVerticalCount() {
        return 0;
    }

    @Override
    public int getHorizontalCount() {
        return 0;
    }
}
