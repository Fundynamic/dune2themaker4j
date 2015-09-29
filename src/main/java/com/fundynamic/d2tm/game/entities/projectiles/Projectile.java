package com.fundynamic.d2tm.game.entities.projectiles;


import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

public class Projectile extends Entity implements Moveable {

    public Projectile(Vector2D mapCoordinates, SpriteSheet spriteSheet, int sight, Player player) {
        super(mapCoordinates, spriteSheet, sight, player);
    }

    @Override
    public void render(Graphics graphics, int x, int y) {

    }

    @Override
    public void update(float deltaInMs) {

    }

    @Override
    public void moveTo(Vector2D target) {

    }

}
