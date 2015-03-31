package com.fundynamic.d2tm.game.controls;


import com.fundynamic.d2tm.game.map.Cell;

public interface MouseBehavior {

    void leftClicked();

    void rightClicked();

    void mouseMovedToCell(Cell cell);

}
