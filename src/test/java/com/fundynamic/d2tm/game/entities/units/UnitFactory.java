package com.fundynamic.d2tm.game.entities.units;


import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;

import static org.mockito.Mockito.mock;

public class UnitFactory {

    public static Unit makeUnit(Player player, int hitPoints) {
        return makeUnit(player, hitPoints, Vector2D.zero());
    }

    public static Unit makeUnit(Player player, int hitPoints, Vector2D mapCoordinates) {
        EntityData entityData = new EntityData(32, 32, 2);
        entityData.hitPoints = hitPoints;
        return new Unit(mock(Map.class), mapCoordinates, mock(Image.class), player, entityData, mock(EntityRepository.class));
    }
}
