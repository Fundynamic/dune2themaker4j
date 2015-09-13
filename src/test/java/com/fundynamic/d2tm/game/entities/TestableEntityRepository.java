package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import org.mockito.Mockito;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TestableEntityRepository extends EntityRepository {

    private Image imageToReturn;

    public TestableEntityRepository(Map map, Recolorer recolorer) throws SlickException {
        super(map, recolorer);
    }

    public void setImageToReturn(Image imageToReturn) {
        this.imageToReturn = imageToReturn;
    }

    @Override
    protected Image loadImage(String pathToImage) throws SlickException {
        if (imageToReturn == null) {
            return Mockito.mock(Image.class);
        }
        return imageToReturn;
    }
}
