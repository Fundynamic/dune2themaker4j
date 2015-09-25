package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.graphics.ImageRepository;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

public class TestableMouse extends Mouse {
    public TestableMouse(Player controllingPlayer, GameContainer gameContainer, EntityRepository entityRepository, ImageRepository imageRepository) {
        super(controllingPlayer, gameContainer, entityRepository, imageRepository);
    }

    @Override
    public void setMouseImage(Image image, int hotSpotX, int hotSpotY) {
        // do nothing
    }

}
