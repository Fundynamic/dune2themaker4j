package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.behaviors.*;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.entitybuilders.AbstractBuildableEntity;
import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import com.fundynamic.d2tm.game.entities.entitybuilders.SingleEntityBuilder;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.Colors;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Structure extends Entity implements Selectable, Destructible, Focusable, EntityBuilder {

    private EntityBuilder entityBuilder;

    // Behaviors
    private final FadingSelection fadingSelection;
    protected final HitPointBasedDestructibility hitPointBasedDestructibility;

    // Temporarily timer for refinery 'credits giving' logic
    private float thinkTimer = 0.0f;

    // Implementation
    private float animationTimer = 0.0f;
    private int animationFrame = 0;
    private static final int FLAG_ANIMATION_FRAME_COUNT = 2;

    public Structure(Coordinate coordinate, SpriteSheet spritesheet, Player player, EntityData entityData, EntityRepository entityRepository) {
        super(coordinate,
                spritesheet,
                entityData,
                player,
                entityRepository
        );

        this.fadingSelection = new FadingSelection(entityData.getWidth(), entityData.getHeight());
        this.hitPointBasedDestructibility = new HitPointBasedDestructibility(entityData.hitPoints, entityData.getWidth());

        List<EntityData> entityDatas = new ArrayList<>();
        if (entityData.entityBuilderType == EntityBuilderType.STRUCTURES) {
            for (String buildableEntity : entityData.getEntityDataKeysToBuild()) {
                entityDatas.add(entityRepository.getEntityData(EntityType.STRUCTURE, buildableEntity));
            }
        }

        if (entityData.entityBuilderType == EntityBuilderType.UNITS) {
            for (String buildableEntity : entityData.getEntityDataKeysToBuild()) {
                entityDatas.add(entityRepository.getEntityData(EntityType.UNIT, buildableEntity));
            }
        }

        if (entityData.entityBuilderType == EntityBuilderType.SUPERPOWERS) {
            for (String buildableEntity : entityData.getEntityDataKeysToBuild()) {
                entityDatas.add(entityRepository.getEntityData(EntityType.SUPERPOWER, buildableEntity));
            }
        }

        this.entityBuilder = new SingleEntityBuilder(entityDatas, this, player);

        if (entityData.hasOnPlacementSpawnUnitId()) {
            // TODO-HARVESTER: This is used in the unit spawnment as well, move somewhere, abstract?!
            Set<MapCoordinate> allSurroundingCellsAsCoordinates = getAllSurroundingCellsAsMapCoordinates();
            for (MapCoordinate potentiallySpawnableCoordinate : allSurroundingCellsAsCoordinates) {
                EntityRepository.PassableResult passableResult = this.entityRepository.isPassableWithinMapBoundaries(this, potentiallySpawnableCoordinate);

                if (passableResult.isPassable()) {
                    Coordinate absoluteCoordinate = potentiallySpawnableCoordinate.toCoordinate();
                    this.entityRepository.placeOnMap(
                            absoluteCoordinate,
                            entityRepository.getEntityData(EntityType.UNIT, entityData.onPlacementSpawnUnitId), // this can be made configurable
                            this.player
                    );
                    break;
                }
            }
        }
    }

    public Image getSprite() {
        int verticalCount = spritesheet.getVerticalCount();
        if (animationFrame > verticalCount) {
            log("I intent to animate frame " + animationFrame + " but the max frames is " + verticalCount + " - so I fall back to sprite 0.");
            return spritesheet.getSprite(0, 1);
        }
        return spritesheet.getSprite(0, animationFrame);
    }

    public void update(float deltaInSeconds) {
        if (this.isDestroyed()) {
            System.out.println("I (" + this.toString() + ") am dead, so I won't update anymore.");
            return;
        }
        float animationSpeed = 0.5f;

        animationTimer += deltaInSeconds;
        if (animationTimer > animationSpeed) {
            animationFrame++;
            animationTimer -= animationSpeed;
        }

        // TODO-HARVESTER: Introduce structure states, like units!?
        // If refinery expects delivery then animate the delivery animation
        if (entityData.isRefinery &&
            EnterStructureIntent.instance.containsIntentToEnterAt(this)) {
            refineryDune2UnitWillEnterRefineryAnimation(deltaInSeconds);
        } else {
            flagAnimation();
        }

        this.fadingSelection.update(deltaInSeconds);

        this.entityBuilder.update(deltaInSeconds);

        handleUnitConstructedAndNeedsToBeSpawnedLogic();
    }

    public void flagAnimation() {
        if (animationFrame >= FLAG_ANIMATION_FRAME_COUNT) {
            animationFrame = 0;
        }
    }

    public void refineryDune2UnitWillEnterRefineryAnimation(float deltaInSeconds) {
        // TODO: Make animation configurable
        if (containsEntity != null) {
            if (animationFrame < 5) animationFrame = 5;
            if (animationFrame > 6) animationFrame -= 2;
        } else {
            if (animationFrame < 1) animationFrame = 1;
            if (animationFrame > 4) animationFrame -= 4;
        }

        thinkTimer += deltaInSeconds;
        while (thinkTimer > 0.5F) {
            thinkTimer -= 0.5F;
        }
    }

    public void handleUnitConstructedAndNeedsToBeSpawnedLogic() {
        if (isAwaitingSpawning()) {
            // TODO-HARVESTER: This is used in the constructor as well, move somewhere
            Set<MapCoordinate> allSurroundingCellsAsCoordinates = getAllSurroundingCellsAsMapCoordinates();
            Unit firstEntityThatBlocksExit = null;
            for (MapCoordinate potentiallySpawnableCoordinate : allSurroundingCellsAsCoordinates) {
                EntityRepository.PassableResult passableResult = this.entityRepository.isPassableWithinMapBoundaries(this, potentiallySpawnableCoordinate);

                if (passableResult.isPassable()) {
                    AbstractBuildableEntity buildingEntity = entityBuilder.getBuildingEntity();
                    Coordinate absoluteCoordinate = potentiallySpawnableCoordinate.toCoordinate();

                    Entity entity = this.entityRepository.placeOnMap(
                                absoluteCoordinate,
                                buildingEntity.getEntityData(),
                                this.player
                        );
                    this.entityIsDelivered(entity);
                    break;
                } else {
                    if (firstEntityThatBlocksExit == null) {
                        System.out.println("Found entities set that blocks units:");
                        System.out.println(passableResult.getEntities());
                        System.out.println("END");
                        firstEntityThatBlocksExit = (Unit) passableResult.getEntities().getFirst(Predicate.ofType(EntityType.UNIT));
                    }
                }
            }

            // we did not succeed in spawning an entity
            if (isAwaitingSpawning()) {
                // TODO: fly it in, nudge other units to move away, etc.
                // For now we just kill the blocking unit or we forget about it

                // THIS IS JUST FOR FUN
                if (firstEntityThatBlocksExit != null) {
                    // kill it, so we can try again next frame
                    System.out.println("------------------" + System.currentTimeMillis());
                    System.out.println("ERROR: Unable to spawn unit next to structure - but found a unit that was blocking it and we killed it to make room!");
                    System.out.println("------------------");
                    firstEntityThatBlocksExit.die();
                } else {
                    // For now, forget it :/
                    System.out.println("ERROR: Unable to spawn unit next to structure [" + this + "]");
                    this.entityIsDelivered(this);
                }
            }
        }
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.STRUCTURE;
    }

    @Override
    public void render(Graphics graphics, int x, int y) {
        Image sprite = getSprite();
        graphics.drawImage(sprite, x, y);

        if (Game.DEBUG_INFO) {
            // render build-range
            Vector2D halfSize = getHalfSize();
            Circle circle = new Circle(x + halfSize.getXAsInt(), y + halfSize.getYAsInt(), entityData.buildRange);
            graphics.setColor(Colors.YELLOW_ALPHA_32);
            ShapeRenderer.fill(circle);
        }
    }

    public void select() {
        fadingSelection.select();
    }

    public void deselect() {
        fadingSelection.deselect();
    }

    @Override
    public boolean isSelected() {
        return fadingSelection.isSelected();
    }

    @Override
    public void getsFocus() {
        fadingSelection.getsFocus();
    }

    @Override
    public void lostFocus() {
        fadingSelection.lostFocus();
    }

    @Override
    public String toString() {
        return "Structure{" +
                "fadingSelection=" + fadingSelection +
                ", animationTimer=" + animationTimer +
                ", hitPoints=" + hitPointBasedDestructibility +
                '}';
    }

    @Override
    public void takeDamage(int hitPoints, Entity origin) {
        hitPointBasedDestructibility.reduce(hitPoints);
        if (hitPointBasedDestructibility.isZero()) {
            die();
        }
    }

    @Override
    public boolean isDestroyed() {
        return hitPointBasedDestructibility.isZero();
    }

    @Override
    public void die() {
        if (containsEntity != null) {
            containsEntity.die();
        }

        for (Coordinate centeredPos : entityData.getAllCellsAsCenteredCoordinates(coordinate)) {
            entityRepository.explodeAt(centeredPos, entityData, player);
        }

        hitPointBasedDestructibility.toZero();
        containsEntity = null;
    }

    @Override
    public int getHitPoints() {
        return hitPointBasedDestructibility.getCurrent();
    }

    @Override
    public void enrichRenderQueue(RenderQueue renderQueue) {

        if (Game.DEBUG_INFO && EnterStructureIntent.instance.containsIntentToEnterAt(this)) {
            Entity unitThatWantsToEnterThisStructure = EnterStructureIntent.instance.getEnterIntentFrom(this);
            LineBetweenEntities lineBetweenEntities = new LineBetweenEntities(unitThatWantsToEnterThisStructure, renderQueue);
            renderQueue.putEntityGui(lineBetweenEntities, this.getCenteredCoordinate());
        }

        if (isSelected()) {
            renderQueue.putEntityGui(this.hitPointBasedDestructibility, this.getCoordinate());
            renderQueue.putEntityGui(this.fadingSelection, this.getCoordinate());
        } else {
            if (fadingSelection.hasFocus()) {
                renderQueue.putEntityGui(this.hitPointBasedDestructibility, this.getCoordinate());
            }
        }
    }

    public boolean hasFocus() {
        return fadingSelection.hasFocus();
    }

    @Override
    public List<AbstractBuildableEntity> getBuildList() {
        return this.entityBuilder.getBuildList();
    }

    @Override
    public boolean isBuildingAnEntity() {
        return this.entityBuilder.isBuildingAnEntity();
    }

    @Override
    public AbstractBuildableEntity getBuildingEntity() {
        return this.entityBuilder.getBuildingEntity();
    }

    @Override
    public void buildEntity(AbstractBuildableEntity abstractBuildableEntity) {
        this.entityBuilder.buildEntity(abstractBuildableEntity);
    }

    @Override
    public boolean isAwaitingPlacement(AbstractBuildableEntity placementBuildableEntity) {
        return this.entityBuilder.isAwaitingPlacement(placementBuildableEntity);
    }

    @Override
    public boolean isAwaitingPlacement(EntityData entityData) {
        return this.entityBuilder.isAwaitingPlacement(entityData);
    }

    @Override
    public boolean isAwaitingSpawning() {
        return this.entityBuilder.isAwaitingSpawning();
    }

    @Override
    public void entityIsDelivered(Entity entity) {
        this.entityBuilder.entityIsDelivered(entity);
    }

    @Override
    public boolean isBuildingAnEntity(AbstractBuildableEntity placementBuildableEntity) {
        return this.entityBuilder.isBuildingAnEntity(placementBuildableEntity);
    }
}
