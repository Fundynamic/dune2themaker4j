package com.fundynamic.d2tm.game.entities.units;


import com.fundynamic.d2tm.game.entities.EntityData;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

public class TestableRenderableWithFacingLogic extends RenderableWithFacingLogic {
    public TestableRenderableWithFacingLogic(Image image, EntityData entityData, float turnSpeed) {
        super(image, entityData, turnSpeed);
    }

    @Override
    public Image getSprite(int x, int y) {
        return Mockito.mock(Image.class);
    }
}
