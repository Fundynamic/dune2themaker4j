package com.fundynamic.d2tm.game.event;


import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Viewport;
import org.newdawn.slick.Input;

public class ViewportMovementListener extends AbstractMouseListener {

    private final Viewport viewport;
    private final Mouse mouse;
    private final EntityRepository entityRepository;
    private final Player player;
    private EntityRepository.EntityType entityType;

    public ViewportMovementListener(Viewport viewport, Mouse mouse, EntityRepository entityRepository, Player player) {
        this.viewport = viewport;
        this.mouse = mouse;
        this.entityRepository = entityRepository;
        this.entityType = EntityRepository.EntityType.STRUCTURE;
        this.player = player;
    }

    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        if (clickCount == 1) {
            if (button == Input.MOUSE_LEFT_BUTTON) {
                mouse.leftClicked();
            } else if (button == Input.MOUSE_RIGHT_BUTTON) {
                mouse.rightClicked();
            }
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {

    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (button == Input.MOUSE_LEFT_BUTTON) {
            mouse.leftButtonReleased();
        }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        viewport.tellAboutNewMousePositions(newx, newy);

        int absoluteX = viewport.getAbsoluteX(newx);
        int absoluteY = viewport.getAbsoluteY(newy);

        Map map = viewport.getMap();

        mouse.mouseMovedToCell(map.getCellByAbsolutePixelCoordinates(absoluteX, absoluteY));
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        mouse.draggedToCoordinates(newx, newy);
    }
}
