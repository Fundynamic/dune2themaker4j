package com.fundynamic.d2tm.game.controls;

import com.fundynamic.d2tm.game.behaviors.Renderable;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Cell;
import org.newdawn.slick.Graphics;


public class Mouse {

    private int x, y;
    private MouseBehavior mouseBehavior;

    private Entity lastSelectedEntity;
    private Cell hoverCell;

    public Mouse(Player controllingPlayer) {
        this.mouseBehavior = new NormalMouse(controllingPlayer, this);
        this.hoverCell = null;
    }

    public void leftClicked() {
        mouseBehavior.leftClicked();
    }

    public void rightClicked() {
        mouseBehavior.rightClicked();
    }

    public void mouseMovedToXAndY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void mouseMovedToCell(Cell cell) {
        mouseBehavior.mouseMovedToCell(cell);
    }

    public void setMouseBehavior(MouseBehavior mouseBehavior) {
        if (mouseBehavior == null) throw new IllegalArgumentException("MouseBehavior argument may not be null!");
        System.out.println("Mouse behavior changed into " + mouseBehavior);
        this.mouseBehavior = mouseBehavior;
    }

    public void render(Graphics graphics) {
        mouseBehavior.render(graphics, x, y);
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
}
