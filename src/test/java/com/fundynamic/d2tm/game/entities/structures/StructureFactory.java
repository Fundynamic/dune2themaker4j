package com.fundynamic.d2tm.game.entities.structures;


import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;

import static org.mockito.Mockito.mock;

public class StructureFactory {

    public static Structure makeStructure(Player player, int hitPoints, EntityRepository entityRepository) {
        EntityData entityData = new EntityData(32, 32, 2);
        entityData.hitPoints = hitPoints;
        return new Structure(Vector2D.zero(), mock(Image.class), player, entityData, entityRepository);
    }

}
