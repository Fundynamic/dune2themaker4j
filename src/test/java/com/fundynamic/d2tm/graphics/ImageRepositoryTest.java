package com.fundynamic.d2tm.graphics;

import org.mockito.Mockito;
import org.newdawn.slick.Image;


public class ImageRepositoryTest {

    public static ImageRepository makeTestableImageRepository() {
        return new ImageRepository() {
            @Override
            public Image load(String path) {
                return Mockito.mock(Image.class);
            }
        };
    }

}