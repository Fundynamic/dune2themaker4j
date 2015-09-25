package com.fundynamic.d2tm.graphics;


import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for retrieving images, wraps instantiating the Image object from Slick.
 *
 * Using the loadAndCache method it will first try to fetch from cache before loading a new image.
 *
 */
public class ImageRepository {

    private Map<String, Image> imagesPerPath = new HashMap<>();

    public Image loadAndCache(String path) {
        if (!imagesPerPath.containsKey(path)) {
            imagesPerPath.put(path, load(path));
        }
        return imagesPerPath.get(path);
    }

    public Image load(String path) {
        try {
            return new Image(path);
        } catch (SlickException e) {
            throw new CannotLoadImageException(e);
        }
    }

}
