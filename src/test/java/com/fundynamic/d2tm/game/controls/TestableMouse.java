package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

public class TestableMouse extends Mouse {

    public TestableMouse(Player controllingPlayer, GameContainer gameContainer, EntityRepository entityRepository) {
        super(controllingPlayer, gameContainer, entityRepository);
    }

    @Override
    public void setMouseImage(Image image, int hotSpotX, int hotSpotY) {
        // do nothing
    }

    @Override
    public void setMouseImageHotSpotCentered(Image image) {
        // do nothing
    }
}
