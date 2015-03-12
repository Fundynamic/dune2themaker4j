package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.Moveable;
import com.fundynamic.d2tm.game.behaviors.Selectable;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Random;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Unit extends Entity implements Selectable, Moveable {

    // Behaviors
    private final FadingSelection fadingSelection;
    private Vector2D target;

    // Implementation
    private final int facing;
    private final Map map;


    public Unit(Map map, Vector2D mapCoordinates, Image image, int width, int height, int sight, Player player) {
        this(map, mapCoordinates, new SpriteSheet(image, width, height), new FadingSelection(width, height), sight, player);
    }

    public Unit(Map map, Vector2D mapCoordinates, SpriteSheet spriteSheet, FadingSelection fadingSelection, int sight, Player player) {
        super(mapCoordinates, spriteSheet, sight, player);
        this.map = map;

        int possibleFacings = spriteSheet.getHorizontalCount();
        this.facing = Random.getRandomBetween(0, possibleFacings);
        this.fadingSelection = fadingSelection;
        this.target = mapCoordinates;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);
        this.fadingSelection.render(graphics, x, y);
    }

    @Override
    public void update(float deltaInMs) {
        this.fadingSelection.update(deltaInMs);
        if (this.target != mapCoordinates) {
            map.getCell(mapCoordinates).removeEntity();
            this.mapCoordinates = target;
            map.revealShroudFor(mapCoordinates, sight, player);
            map.getCell(mapCoordinates).setEntity(this);
        }
    }

    public Image getSprite() {
        return spriteSheet.getSprite(facing, 0);
    }

    @Override
    public String toString() {
        return "Unit{" +
                "facing=" + facing +
                '}';
    }

    @Override
    public void select() {
        fadingSelection.select();
    }

    @Override
    public void deselect() {
        fadingSelection.deselect();
    }

    @Override
    public boolean isSelected() {
        return fadingSelection.isSelected();
    }

    @Override
    public void moveTo(Vector2D target) {
        this.target = target;
    }
}
