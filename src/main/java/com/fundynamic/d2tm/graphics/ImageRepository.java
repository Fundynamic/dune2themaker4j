package com.fundynamic.d2tm.graphics;


import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

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

    public SpriteSheet loadAndCacheSpriteSheet(String path, int spriteWidth, int spriteHeight) {
        String cacheKey = path + "sw:" + spriteWidth + "sh:" + spriteHeight;
        if (!imagesPerPath.containsKey(cacheKey)) {
            imagesPerPath.put(cacheKey, loadSpriteSheet(path, spriteWidth, spriteHeight));
        }
        return (SpriteSheet) imagesPerPath.get(cacheKey);
    }

    public Image load(String path) {
        try {
            return new Image(path);
        } catch (SlickException e) {
            throw new CannotLoadImageException(e);
        }
    }

    public Image loadSpriteSheet(String path, int spriteWidth, int spriteHeight) {
        try {
            return new SpriteSheet(new Image(path), spriteWidth, spriteHeight);
        } catch (SlickException e) {
            throw new CannotLoadImageException(e);
        }
    }

    public Image createImage(Vector2D dimensions) throws SlickException {
        return new Image(dimensions.getXAsInt(), dimensions.getYAsInt());
    }

}
