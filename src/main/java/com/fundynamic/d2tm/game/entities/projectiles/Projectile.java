package com.fundynamic.d2tm.game.entities.projectiles;


import com.fundynamic.d2tm.game.behaviors.Destructible;
import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Projectile extends Entity implements Moveable, Destructible {

    private Vector2D target;
    private boolean destroyed;

    // Implementation
    private final Map map;

    public Projectile(Map map, Vector2D mapCoordinates, SpriteSheet spriteSheet, int sight, Player player, EntityRepository entityRepository) {
        super(mapCoordinates, spriteSheet, sight, player, entityRepository);
        this.map = map;
        target = mapCoordinates;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PROJECTILE;
    }


    @Override
    public void render(Graphics graphics, int x, int y) {
        if (graphics == null) throw new IllegalArgumentException("Graphics must be not-null");
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
    }

    public Image getSprite() {
        int facing = 0;
        if (target != absoluteMapCoordinates) {
            double angle = absoluteMapCoordinates.angleTo(target);
            float chop = 22.5F; // 22.5 degrees equals 16 facings over 360 degrees (360 / 16 = 22.5)
            facing = (int) (angle / chop);
        }
        return spriteSheet.getSprite(facing, 0);
    }

    @Override
    public void update(float deltaInSeconds) {
        if (target != absoluteMapCoordinates) {
            float speed = 96f;

            float timeCorrectedSpeed = speed * deltaInSeconds;
            Vector2D direction = target.min(absoluteMapCoordinates);
            Vector2D normalised = direction.normalise();

            // make sure we don't overshoot
            float distance = absoluteMapCoordinates.distance(target);
            if (distance < timeCorrectedSpeed) timeCorrectedSpeed = distance;

            absoluteMapCoordinates = absoluteMapCoordinates.add(normalised.scale(timeCorrectedSpeed));
        }

        if (target.distance(absoluteMapCoordinates) < 0.1F) {
            // spawn explosion
            entityRepository.placeOnMap(absoluteMapCoordinates, EntityType.PARTICLE, EntityRepository.EXPLOSION_NORMAL, player);

            // do damage on cell / range of cells
            Cell cell = map.getCellByAbsoluteMapCoordinates(absoluteMapCoordinates);
            if (cell.hasAnyEntity()) {
                Entity entity = cell.getEntity();
                if (entity.isDestructible()) {
                    Destructible destructibleEntity = (Destructible) entity;
                    destructibleEntity.takeDamage(100);
                }
            }
            destroyed = true;
        }
    }

    @Override
    public void moveTo(Vector2D target) {
        this.target = target;
    }

    @Override
    public void takeDamage(int hitPoints) {

    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}
