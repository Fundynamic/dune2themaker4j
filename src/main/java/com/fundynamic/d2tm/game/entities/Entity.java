package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import com.fundynamic.d2tm.game.entities.superpowers.SuperPower;
import com.fundynamic.d2tm.game.entities.units.RenderQueueEnrichableWithFacingLogic;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>
 *     An entity is a 'thing' that 'lives' on the {@link com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField}.
 * </p>
 * <h2>State</h2>
 * <p>
 *     An entity has state, usually a lifespan or somesort, therefor it is {@link Updateable}. The {@link #update(float)} method
 *     is called by the {@link com.fundynamic.d2tm.game.state.PlayingState#update(GameContainer, StateBasedGame, int)} method.
 * </p>
 * <h2>Rendering:</h2>
 * <p>
 * An entity is told where on screen it should be rendered by a {@link RenderQueue}.
 *
 * This is because the translation
 * from Map to screen position is done by a {@link com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField} or
 * Viewport (To be built).
 * </p>
 */
public abstract class Entity implements EnrichableAbsoluteRenderable, Updateable {

    // Final properties of entity
    protected final EntityData entityData;
    protected final SpriteSheet spritesheet;
    protected final Player player;
    protected final EntityRepository entityRepository;

    protected Entity origin; // who, which entity, created this entity? (if applicable)

    protected Entity containsEntity;
    protected Entity hasEntered;

    /**
     * top left *cell* coordinate, not the top-left of unit image!
     * ie, even a Harvester (with 48x48 image size) is positioned on 32,32, a trike is as well.
     * both units are rendered differently, but that is taken care of the {@link RenderQueueEnrichableWithFacingLogic}
     **/
    protected Coordinate coordinate;

    public Entity(Coordinate coordinate, SpriteSheet spritesheet, EntityData entityData, Player player, EntityRepository entityRepository) {
        this.coordinate = coordinate;
        this.spritesheet = spritesheet;
        this.entityData = entityData;
        this.player = player;
        this.entityRepository = entityRepository;
        if (player != null) {
            // temporarily, because 'particle' does not belong to a player
            player.addEntity(this);
        }
    }

    /**
     * Returns the upper-left coordinate of this entity
     *
     * @return
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Returns center of this entity as coordinate. Which basically means adding half its size to the top-left coordinate.
     *
     * @return
     */
    public Coordinate getCenteredCoordinate() {
        return coordinate.add(getHalfSize());
    }

    /**
     * Returns distance from this entity to other, using centered coordinates for both entities.
     *
     * @param other
     * @return
     */
    public float distance(Entity other) {
        return getCenteredCoordinate().distance(other.getCenteredCoordinate());
    }

    /**
     * Returns distance from this entity to other Coordinate. Uses centered coordinate as start
     * coordinate. Does not influence given otherCoordinate param
     * @param otherCoordinate
     * @return
     */
    public float distanceToFromCentered(Coordinate otherCoordinate) {
        return getCenteredCoordinate().distance(otherCoordinate);
    }

    /**
     * Returns distance from this entity to other Coordinate. Does not influence given otherCoordinate param
     * @param otherCoordinate
     * @return
     */
    public float distanceTo(Coordinate otherCoordinate) {
        return coordinate.distance(otherCoordinate);
    }

    /**
     * Returns distance from this entity to other Map coordinate in 'cells'.
     *
     * @param otherCoordinate
     * @return
     */
    public int distanceToInMapCoords(MapCoordinate otherCoordinate) {
        return coordinate.toMapCoordinate().distanceInMapCoords(otherCoordinate);
    }

    public int getX() {
        return coordinate.getXAsInt();
    }

    public int getY() {
        return coordinate.getYAsInt();
    }

    public int getSight() {
        return entityData.sight;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean belongsToPlayer(Player other) {
        return other.equals(player);
    }

    public boolean isMovable() {
        return this instanceof Moveable;
    }

    public boolean isSelectable() {
        return this instanceof Selectable;
    }

    public boolean isUpdatable() {
        return this instanceof Updateable;
    }

    public boolean isDestructible() {
        return this instanceof Destructible;
    }

    public boolean isDestroyer() {
        return this instanceof Destroyer;
    }

    public boolean isFocusable() {
        return this instanceof Focusable;
    }

    public boolean isSuperPower() {
        return this instanceof SuperPower;
    }

    /**
     * Capable of harvesting stuff
     * @return
     */
    public boolean isHarvester() {
        return entityData.isHarvester;
    }

    /**
     * Capable of dealing with harvesters to return spice
     * @return
     */
    public boolean isRefinery() {
        return entityData.isRefinery;
    }

    public abstract EntityType getEntityType();

    public boolean isEntityTypeStructure() {
        return getEntityType().equals(EntityType.STRUCTURE);
    }

    public boolean removeFromPlayerSet(Entity entity) {
        if (player != null) {
            return player.removeEntity(entity);
        }
        return false;
    }

    public Coordinate getRandomPositionWithin() {
        return new Coordinate(Vector2D.random(getX(), getX() + entityData.getWidth(), getY(), getY() + entityData.getHeight()));
    }

    public Vector2D getDimensions() {
        return Vector2D.create(entityData.getWidth(), entityData.getHeight());
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {
        // by default do nothing
    }

    public Vector2D getHalfSize() {
        return Vector2D.create(entityData.getWidth() / 2, entityData.getHeight() / 2);
    }

    /**
     * Return the metadata about this entity.
     * @return
     */
    public EntityData getEntityData() {
        return this.entityData;
    }

    public int getWidthInCells() {
        return entityData.getWidthInCells();
    }

    public int getHeightInCells() {
        return entityData.getHeightInCells();
    }

    public List<MapCoordinate> getAllCellsAsMapCoordinates() {
        return entityData.getAllCellsAsCoordinates(coordinate);
    }

    public List<Coordinate> getAllCellsAsCoordinates() {
        List<MapCoordinate> allCellsAsCoordinates = entityData.getAllCellsAsCoordinates(coordinate);
        return allCellsAsCoordinates.stream().map(mc -> mc.toCoordinate()).collect(Collectors.toList());
    }

    public List<Coordinate> getAllCellsAsCenteredCoordinates() {
        List<MapCoordinate> allCellsAsCoordinates = entityData.getAllCellsAsCoordinates(coordinate);
        return allCellsAsCoordinates.stream().map(mc -> mc.toCoordinate().addHalfTile()).collect(Collectors.toList());
    }

    public Set<MapCoordinate> getAllSurroundingCellsAsMapCoordinates() {
        return getAllSurroundingCellsAsMapCoordinatesStartingFromTopLeft(1); // coordinate == top left
    }

    public Set<Coordinate> getAllSurroundingCellsAsCoordinates() {
        // coordinate == top left
        Set<MapCoordinate> mapCoordinates = getAllSurroundingCellsAsMapCoordinatesStartingFromTopLeft(1);
        return mapCoordinates
                .stream()
                .map(mapCoordinate -> mapCoordinate.toCoordinate())
                .collect(
                        Collectors.toSet()
                );
    }

    public Set<MapCoordinate> getAllSurroundingCellsAsMapCoordinatesStartingFromTopLeft(int distance) {
        return getAllSurroundingCellsAsMapCoordinatesStartingFromTopLeft(coordinate.toMapCoordinate(), distance, new HashSet<>());
    }

    /**
     * Goes around entity. Basically as a 'rectangle' around entity. Starting from the highest distance, then recursively
     * repeating logic until distance == 1
     * @param topLeftMapCoordinate
     * @param distance
     * @return
     */
    public Set<MapCoordinate> getAllSurroundingCellsAsMapCoordinatesStartingFromTopLeft(MapCoordinate topLeftMapCoordinate, int distance, Set<MapCoordinate> alreadyFound) {
        int currentX = topLeftMapCoordinate.getXAsInt();
        int currentY = topLeftMapCoordinate.getYAsInt();

        // first row
        int topLeftYMinusDistance = currentY - distance;
        int topLeftXMinusDistance = currentX - distance;

        int totalWidth = (distance + entityData.getWidthInCells() + distance) - 1;
        int totalHeight = (distance + entityData.getHeightInCells() + distance) - 1;

        Set<MapCoordinate> result = new HashSet<>(alreadyFound);

        // -1, because it is 0 based
        for (int x = 0; x <= totalWidth; x++) {
            result.add(MapCoordinate.create(topLeftXMinusDistance + x, topLeftYMinusDistance));
        }

        // then all 'sides' of the structure (left and right)
        // also start one row 'lower' since we do not want to calculate the top left/right twice (as we did it
        // in the above loop already)
        // totalHeight - 2 for same reason, -1 == zero based, -2 to reduce one row
        for (int y = 1; y <= (totalHeight - 1); y++) {
            int calculatedY = topLeftYMinusDistance + y;

            // left side
            result.add(MapCoordinate.create(topLeftXMinusDistance, calculatedY));

            // right side
            int rightX = topLeftXMinusDistance + totalWidth;
            result.add(MapCoordinate.create(rightX, calculatedY));
        }

        // bottom row
        int bottomRowY = topLeftYMinusDistance + totalHeight;
        for (int x = 0; x <= totalWidth; x++) {
            result.add(MapCoordinate.create(topLeftXMinusDistance + x, bottomRowY));
        }

        if (distance > 1) {
            return getAllSurroundingCellsAsMapCoordinatesStartingFromTopLeft(topLeftMapCoordinate, (distance-1), result);
        }
        return result;
    }

    public boolean isVisibleFor(Player player, Map map) {
        List<Cell> allCellsOccupiedByEntity = map.getAllCellsOccupiedByEntity(this);
        for (Cell cell : allCellsOccupiedByEntity) {
            if (cell.isVisibleFor(player)) return true;
        }
        return false;
    }

    public Entity setOrigin(Entity origin) {
        this.origin = origin;
        return this;
    }

    public boolean isEntityBuilder() {
        return this.entityData.entityBuilderType != EntityBuilderType.NONE;
    }

    public boolean isVectorWithin(Coordinate coordinate) {
        Rectangle entityRectangle = Rectangle.createWithDimensions(getCoordinate(), getDimensions());
        boolean result = entityRectangle.isVectorWithin(coordinate);
//        System.out.println("Checking if Coordinate (" + result + ") : " + coordinate + " is within Rectangle of [" + this.getEntityData().key + "] : " + entityRectangle);
        return result;
    }

    protected final java.util.Map<EventType, List<EventSubscription<? extends Entity>>> eventTypeListMap = new HashMap();

    public <T extends Entity> void onEvent(EventType eventType, T subscriber, Function<T, Void> eventHandler) {
        this.onEvent(eventType, subscriber, eventHandler, true);
    }

    protected <T extends Entity> void onEvent(EventType eventType, T subscriber, Function<T, Void> eventHandler, boolean registerDestroyEventHandler) {
        if (!eventTypeListMap.containsKey(eventType)) {
            eventTypeListMap.put(eventType, new LinkedList<>());
        }
        addEventSubscription(eventType, subscriber, eventHandler);

        if (registerDestroyEventHandler) {
            // when the subscriber gets destroyed, let this entity know it happened.
            subscriber.onEvent(EventType.ENTITY_DESTROYED, this, s -> s.removeEventSubscription(subscriber), false);

            if (subscriber != this) {
                // when this gets destroyed, tell the *other* subscriber that it happened.
                this.onEvent(EventType.ENTITY_DESTROYED, subscriber, s -> s.removeEventSubscription(this), false);
            }
        }
    }

    private <T extends Entity> EventSubscription addEventSubscription(EventType eventType, T subscriber, Function<T, Void> eventHandler) {
        EventSubscription eventSubscription = new EventSubscription(subscriber, eventHandler);
        if (!eventTypeListMap.get(eventType).contains(eventSubscription)) {
            eventTypeListMap.get(eventType).add(eventSubscription);
        }
        return eventSubscription;
    }

    protected <T extends Entity> Void removeEventSubscription(T subscriber) {
        System.out.println(this + " received removeEventSubscription, Entity destroyed is " + subscriber);

        for (List<EventSubscription<? extends Entity>> subscriptions : eventTypeListMap.values()) {

            // find all event subscriptions that have this subscriber
            List<EventSubscription<? extends Entity>> toBeDeleted = new ArrayList<>();
            for (EventSubscription subscription : subscriptions) {
                if (subscription.getSubscriber() == subscriber) { // check by reference!
                    toBeDeleted.add(subscription);
                }
            }

            // delete those
            for (EventSubscription subscription : toBeDeleted) {
                subscriptions.remove(subscription);
            }
        }
        return null;
    }

    /**
     * Called when entity is destroyed by {@link EntityRepository#removeEntity(Entity)}. Do not
     * call this directly from anywhere else. Instead use {@link #die()}
     */
    public void destroy() {
        emitEvent(EventType.ENTITY_DESTROYED); // tell all who are interested that we are destroyed
        UnitMoveIntents.instance.removeAllIntentsBy(this);
        EnterStructureIntent.instance.removeAllIntentsBy(this);
        eventTypeListMap.clear(); // now clear all our subscriptions explicitly
    }

    public void emitEvent(EventType eventType) {
        if (eventTypeListMap.containsKey(eventType)) {
            for (EventSubscription eventSubscription : eventTypeListMap.get(eventType)){
                System.out.println(this.toString() + " emits event " + eventType);
                eventSubscription.invoke();
            }
        }
    }

    /**
     * Look within this entity, if this entity takes up more than 1 coordinate, return the coordinate that
     * is closest.
     * @param centeredCoordinate
     */
    public Coordinate getClosestCoordinateTo(Coordinate centeredCoordinate) {
        List<Coordinate> allCellsAsCoordinates = getAllCellsAsCenteredCoordinates();
        if (allCellsAsCoordinates.size() == 0) allCellsAsCoordinates.get(0);
        Coordinate closest = allCellsAsCoordinates.stream().min((c1, c2) -> Float.compare(c1.distance(centeredCoordinate), c2.distance(centeredCoordinate))).get();
        return closest.minHalfTile();
    }

    /**
     * Look around the entity (surrounding cells) and finds the closets to given parameter
     * @param centeredCoordinate
     */
    public Coordinate getClosestCoordinateAround(Coordinate centeredCoordinate) {
        Set<Coordinate> allCellsAsCoordinates = getAllSurroundingCellsAsCoordinates();
        Coordinate closest = allCellsAsCoordinates
                .stream()
                .min((c1, c2) ->
                        Float.compare(c1.distance(centeredCoordinate), c2.distance(centeredCoordinate))
                )
                .get();
        return closest;
    }

    /**
     * This entity is entering another entity (given parameter).
     *
     * @param whichEntityWillBeEntered
     */
    public void enterOtherEntity(Entity whichEntityWillBeEntered) {
        whichEntityWillBeEntered.containsEntity = this;
        hasEntered = whichEntityWillBeEntered;
    }

    public void leaveOtherEntity() {
        if (hasEntered != null) {
            hasEntered.containsEntity = null;
            hasEntered = null;
        }
    }

    public boolean isWithinOtherEntity() {
        return hasEntered != null;
    }

    public String toStringShort() {
        return "[" + this.entityData.name + " (" + this.hashCode() + " at " + coordinate + "]";
    }

    public void log(String message) {
        if (Game.DEBUG_INFO) {
            System.out.println(toStringShort() + " - " + message);
        }
    }

    /**
     * Initiate dying of entity.
     */
    public abstract void die();

    public boolean hasMoveAnimation() {
        return entityData.hasMoveAnimation;
    }

    class EventSubscription<T extends Entity> {
        private T subscriber;
        private Function<T, Void> eventHandler;

        public EventSubscription(T subscriber, Function<T, Void> eventHandler) {
            this.subscriber = subscriber;
            this.eventHandler = eventHandler;
        }

        public void invoke() {
            eventHandler.apply(subscriber);
        }

        public T getSubscriber() {
            return subscriber;
        }
    }
}