package com.fundynamic.d2tm.game.entities.units;


import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.game.types.EntityData;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Null object for renderable with facing logic. So we don't need to put if statements everywhere with null checks *
 */
public class NullRenderQueueEnrichableWithFacingLogic extends RenderQueueEnrichableWithFacingLogic {

    public NullRenderQueueEnrichableWithFacingLogic(EntityData entityData) throws SlickException {
        super(new Image(32, 32), entityData, 1F); // create dummy image
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        // don't do anything
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // don't do anything
    }

    // since this is a null object (no cannon available) it is NOT required to 'face' the enemy with a cannon, because
    // hence there is none
    @Override
    public boolean isRequiredToFaceEnemyBeforeShooting() {
        return false;
    }
}
