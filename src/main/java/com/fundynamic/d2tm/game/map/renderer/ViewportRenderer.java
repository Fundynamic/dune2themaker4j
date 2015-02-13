package com.fundynamic.d2tm.game.map.renderer;


import com.fundynamic.d2tm.game.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public interface ViewportRenderer<T> {

    public void render(Image imageToDrawOn, Vector2D viewingVector, Renderer<T> renderer) throws SlickException;

}
