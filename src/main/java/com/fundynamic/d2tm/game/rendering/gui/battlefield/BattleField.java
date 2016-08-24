package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.game.controls.Mouse;
import com.fundynamic.d2tm.game.controls.battlefield.AbstractBattleFieldMouseBehavior;
import com.fundynamic.d2tm.game.controls.battlefield.CellBasedMouseBehavior;
import com.fundynamic.d2tm.game.controls.battlefield.NormalMouse;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.Perimeter;
import com.fundynamic.d2tm.game.rendering.gui.GuiElement;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * <p>
 * The battlefield is a view on the map which can be interacted with.
 * </p>
 *
 */
public class BattleField extends GuiElement implements CellBasedMouseBehavior, EntityEventsListener {

    // DATA RELATED
    private Map map;
    private Mouse mouse;

    private final EntityRepository entityRepository;

    // RENDERING RELATED
    private Vector2D viewingVector;

    private final Image buffer;

    private final CellTerrainRenderer cellTerrainRenderer;
    private final CellShroudRenderer cellShroudRenderer;
    private final CellViewportRenderer cellViewportRenderer;

    // MOVEMENT RELATED
    private final Perimeter viewingVectorPerimeter;
    private Vector2D velocity;
    private float moveSpeed;

    // MOUSE BEHAVIOR RELATED
    private AbstractBattleFieldMouseBehavior mouseBehavior;
    private Graphics bufferGraphics;

    public BattleField(Vector2D size,
                       Vector2D drawingPosition,
                       Vector2D viewingVector,
                       Map map,
                       Mouse mouse,
                       float moveSpeed,
                       int tileSize,
                       Player player,
                       Image buffer,
                       EntityRepository entityRepository) throws SlickException {
        super(drawingPosition.getXAsInt(), drawingPosition.getYAsInt(), size.getXAsInt(), size.getYAsInt());

        this.map = map;
        this.mouse = mouse;
        this.entityRepository = entityRepository;

        this.mouseBehavior = new NormalMouse(this);

        this.viewingVectorPerimeter = map.createViewablePerimeter(size, tileSize);
        this.velocity = Vector2D.zero();

        this.moveSpeed = moveSpeed;

        this.viewingVector = viewingVector;
        this.cellViewportRenderer = new CellViewportRenderer(map, tileSize, size);
        this.cellTerrainRenderer = new CellTerrainRenderer();
        this.cellShroudRenderer = new CellShroudRenderer(player, map.getShroud());

        this.buffer = buffer;
        this.bufferGraphics = buffer.getGraphics();
    }

    @Override
    public void render(Graphics graphics) {
        try {
            // TODO: Merge the culling into this viewport class(?)
            cellViewportRenderer.render(bufferGraphics, viewingVector, cellTerrainRenderer);

            // 1) Select entities to draw
            // 2) Decide if there is more to draw (from the entities)
            // 3) draw!
            RenderQueue renderQueue = getEntitiesToDraw();
            renderQueue.render(bufferGraphics);

            cellViewportRenderer.render(bufferGraphics, viewingVector, cellShroudRenderer);

            mouseBehavior.render(bufferGraphics);

            if (hasFocus) {
                bufferGraphics.setColor(Color.red);
                // we use 0,0, because the buffer we draw to is already being drawn
                // at the 'topleft' coordinate
                float lineWidth = bufferGraphics.getLineWidth();
                bufferGraphics.setLineWidth(2.0f);
                bufferGraphics.drawRect(1, 1, getWidthAsInt()-2, getHeightAsInt()-2);
                bufferGraphics.setLineWidth(lineWidth);
            }

            drawBufferToGraphics(graphics, getTopLeft());
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private RenderQueue getEntitiesToDraw() {
        Rectangle rectangle =
                Rectangle.createWithDimensions(
                    viewingVector.min(
                        Vector2D.create(32, 32)
                    ),
                    Vector2D.create(getWidthAsInt() + 64, getHeightAsInt() + 64)
                );

        RenderQueue renderQueue = new RenderQueue(this.viewingVector);
        renderQueue.put(entityRepository.findEntitiesWithinRectangle(rectangle).toList());

        return renderQueue;
    }

    public void update(float delta) {
        Vector2D translation = velocity.scale(delta);
        viewingVector = viewingVectorPerimeter.makeSureVectorStaysWithin(viewingVector.add(translation));
    }

    public void moveLeft() {
        this.velocity = Vector2D.create(-moveSpeed, this.velocity.getY());
    }

    public void moveRight() {
        this.velocity = Vector2D.create(moveSpeed, this.velocity.getY());
    }

    public void moveUp() {
        this.velocity = Vector2D.create(this.velocity.getX(), -moveSpeed);
    }

    public void moveDown() {
        this.velocity = Vector2D.create(this.velocity.getX(), moveSpeed);
    }

    public void stopMovingHorizontally() {
        this.velocity = Vector2D.create(0, this.velocity.getY());
    }

    public void stopMovingVertically() {
        this.velocity = Vector2D.create(this.velocity.getX(), 0);
    }

    private void drawBufferToGraphics(Graphics graphics, Vector2D drawingVector) {
        graphics.drawImage(buffer, drawingVector.getX(), drawingVector.getY());
    }

    // These methods are here mainly for (easier) testing. Best would be to remove them if possible - and at the very
    // least not the use them in the non-test code.
    public Vector2D getViewingVector() {
        return viewingVector;
    }

    public Map getMap() {
        return this.map;
    }

    public Coordinate translateViewportCoordinateToAbsoluteMapCoordinate(Vector2D positionOnViewport) {
        return new Coordinate(positionOnViewport.add(viewingVector));
    }

    public Coordinate translateScreenToViewportCoordinate(Vector2D screenPosition) {
        if (!isVectorWithin(screenPosition)) return null; // outside our viewport!
        return new Coordinate(screenPosition.min(getTopLeft()));
    }

    @Override
    public void leftClicked() {
        mouseBehavior.leftClicked();
    }

    @Override
    public void rightClicked() {
        mouseBehavior.rightClicked();
    }

    @Override
    public void draggedToCoordinates(Vector2D coordinates) {
        int newX = coordinates.getXAsInt();
        int newY = coordinates.getYAsInt();

        Vector2D viewportCoordinates = translateScreenToViewportCoordinate(coordinates);

        if (viewportCoordinates == null) {
            Vector2D drawingVector = getTopLeft();
            Vector2D viewportDimensions = getSize();

            int snappedX = Math.min(
                    Math.max(newX, drawingVector.getXAsInt()),
                    viewportDimensions.getXAsInt() + drawingVector.getXAsInt()
            );

            int snappedY = Math.min(
                    Math.max(newY, drawingVector.getYAsInt()),
                    viewportDimensions.getYAsInt() + drawingVector.getYAsInt()
            );

            Vector2D snappedCoordinates = Vector2D.create(snappedX, snappedY);
            viewportCoordinates = translateScreenToViewportCoordinate(snappedCoordinates);
        }

        mouseBehavior.draggedToCoordinates(viewportCoordinates);
    }

    @Override
    public void movedTo(Vector2D coordinates) {
        this.mouseBehavior.movedTo(coordinates);

        Coordinate viewportCoordinate = translateScreenToViewportCoordinate(coordinates);
        Coordinate absoluteMapCoordinate = translateViewportCoordinateToAbsoluteMapCoordinate(viewportCoordinate);

        Cell cellByAbsoluteMapCoordinates = map.getCellByAbsoluteMapCoordinates(absoluteMapCoordinate);

        mouseMovedToCell(cellByAbsoluteMapCoordinates);
    }

    @Override
    public void leftButtonReleased() {
        this.mouseBehavior.leftButtonReleased();
    }

    @Override
    public void mouseMovedToCell(Cell cell) {
        this.mouseBehavior.mouseMovedToCell(cell);
    }

    public EntityRepository getEntityRepository() {
        return entityRepository;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void setMouseBehavior(AbstractBattleFieldMouseBehavior mouseBehavior) {
        this.mouseBehavior = mouseBehavior;
    }

    public AbstractBattleFieldMouseBehavior getMouseBehavior() {
        return mouseBehavior;
    }

    public Cell getHoverCell() {
        return mouseBehavior.getHoverCell();
    }

    public void setViewingVector(Vector2D viewingVector) {
        this.viewingVector = viewingVector;
    }

    @Override
    public void entitiesSelected(EntitiesSet entities) {
        System.out.println("Battlefield gets told that " + entities + " are selected");
        if (entities.size() == 1) {
            Entity first = entities.getFirst();
            if (first.isEntityBuilder()) {
                guiComposite.entityBuilderSelected(first);
            }
        }
    }

    @Override
    public void entitiesDeselected(EntitiesSet entities) {
        System.out.println("Battlefield gets told that " + entities + " are de-selected");
    }
}
