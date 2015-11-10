package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.rendering.Viewport;
import com.fundynamic.d2tm.graphics.ImageRepository;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;
import java.util.Map;


public class Mouse {

    public enum MouseImages {
        NORMAL, HOVER_OVER_SELECTABLE_ENTITY, MOVE, ATTACK, CUSTOM
    }

    private final Player controllingPlayer;
    private final GameContainer gameContainer;
    private final EntityRepository entityRepository;

    private Viewport viewport;

    private MouseBehavior mouseBehavior;
    private Entity lastSelectedEntity;
    private Cell hoverCell;
    private MouseImages currentImage;

    private Map<MouseImages, Image> mouseImages = new HashMap<>();

    Mouse(Player controllingPlayer, GameContainer gameContainer, EntityRepository entityRepository) {
        this.controllingPlayer = controllingPlayer;
        this.entityRepository = entityRepository;
        this.gameContainer = gameContainer;
    }

    public static Mouse create(Player player, GameContainer gameContainer, EntityRepository entityRepository, ImageRepository imageRepository) throws SlickException {
        Mouse mouse = new Mouse(player, gameContainer, entityRepository);
        mouse.addMouseImage(Mouse.MouseImages.NORMAL, imageRepository.loadAndCache("mouse/mouse_normal.png"));
        mouse.addMouseImage(Mouse.MouseImages.HOVER_OVER_SELECTABLE_ENTITY, imageRepository.loadAndCache("mouse/mouse_pick.png"));
        mouse.addMouseImage(Mouse.MouseImages.MOVE, imageRepository.loadAndCache("mouse/mouse_move.png"));
        mouse.addMouseImage(Mouse.MouseImages.ATTACK, imageRepository.loadAndCache("mouse/mouse_attack.png"));
        mouse.init();
        return mouse;
    }

    public void init() {
        this.mouseBehavior = new NormalMouse(this);
    }

    // TODO make mouse implement Renderable interface?
    public void render(Graphics graphics) {
        mouseBehavior.render(graphics);
    }

    /**
     * When a left click (== press and release) has been detected, this method is called.
     */
    public void leftClicked() {
        mouseBehavior.leftClicked();
    }

    /**
     * When a right click (== press and release) has been detected, this method is called.
     */
    public void rightClicked() {
        mouseBehavior.rightClicked();
    }

    /**
     * This method is called to update the mouse (and its associated behavior) state. Notifying that
     * the mouse has been moved to the given cell.
     * @param cell
     */
    public void mouseMovedToCell(Cell cell) {
        mouseBehavior.mouseMovedToCell(cell);
    }

    public Entity hoveringOverSelectableEntity() {
        if (hoverCell == null) return NullEntity.INSTANCE;
        EntitiesSet entities = entityRepository.filter(Predicate.builder().
                vectorWithin(hoverCell.getCoordinates()).
                isSelectable());
        Entity entity = entities.getFirst();
        if (entity == null) return NullEntity.INSTANCE;
        if (!entity.isSelectable()) return NullEntity.INSTANCE;
        return entity;
    }

    public void setMouseBehavior(MouseBehavior mouseBehavior) {
        if (mouseBehavior == null) throw new IllegalArgumentException("MouseBehavior argument may not be null!");
        System.out.println("Mouse behavior changed into " + mouseBehavior);
        this.mouseBehavior = mouseBehavior;
    }

    public Entity getLastSelectedEntity() {
        return lastSelectedEntity;
    }

    public void setLastSelectedEntity(Entity lastSelectedEntity) {
        this.lastSelectedEntity = lastSelectedEntity;
    }

    public Cell getHoverCell() {
        return hoverCell;
    }

    public void setHoverCell(Cell hoverCell) {
        this.hoverCell = hoverCell;
    }

    public Player getControllingPlayer() {
        return controllingPlayer;
    }

    public void leftButtonReleased() {
        mouseBehavior.leftButtonReleased();
    }

    public void draggedToCoordinates(int newX, int newY) {
        mouseBehavior.draggedToCoordinates(Vector2D.create(newX, newY));
    }

    public void setMouseImage(Image image, int hotSpotX, int hotSpotY) {
        if (image == null) throw new IllegalArgumentException("Image to set for mouse cursor may not be null!");
        if (!mouseImages.containsValue(image)) {
            this.currentImage = MouseImages.CUSTOM;
        }
        try {
            gameContainer.setMouseCursor(image, hotSpotX, hotSpotY);
        } catch (SlickException e) {
            throw new CannotSetMouseCursorException(e);
        }
    }

    public void setMouseImageHotSpotCentered(Image image) {
        if (image == null) throw new IllegalArgumentException("Image to set for mouse cursor may not be null!");
        if (!mouseImages.containsValue(image)) {
            this.currentImage = MouseImages.CUSTOM;
        }
        try {
            gameContainer.setMouseCursor(image, image.getWidth() / 2, image.getHeight() / 2);
        } catch (SlickException e) {
            throw new CannotSetMouseCursorException(e);
        }
    }

    public void setMouseImage(MouseImages key, int hotSpotX, int hotSpotY) {
        if (key.equals(this.currentImage)) return;
        this.currentImage = key;
        setMouseImage(mouseImages.get(key), hotSpotX, hotSpotY);
    }

    public void addMouseImage(MouseImages key, Image image) {
        if (image == null) throw new IllegalArgumentException("Image for mouse images cannot be null!");
        this.mouseImages.put(key, image);
    }

    public MouseBehavior getMouseBehavior() {
        return this.mouseBehavior;
    }

    public EntityRepository getEntityRepository() {
        return entityRepository;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public void movedTo(Vector2D position) {
        viewport.tellAboutNewMousePositions(position.getXAsInt(), position.getYAsInt());

        com.fundynamic.d2tm.game.map.Map map = viewport.getMap();
        Coordinate absoluteMapCoordinates = viewport.translateScreenToAbsoluteMapPixels(position);
        mouseMovedToCell(map.getCellByAbsoluteMapCoordinates(absoluteMapCoordinates));
    }

}
