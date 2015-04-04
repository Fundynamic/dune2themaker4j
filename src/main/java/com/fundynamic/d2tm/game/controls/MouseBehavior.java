package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Graphics;

public interface MouseBehavior {

    void leftClicked();

    void rightClicked();

    void mouseMovedToCell(Cell cell);

    void draggedToCoordinates(Vector2D coordinates);

    void leftButtonReleased();

    void render(Graphics g);
}
