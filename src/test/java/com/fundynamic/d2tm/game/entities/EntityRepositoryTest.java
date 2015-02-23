package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.map.Map;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EntityRepositoryTest {

    @Mock
    public Map map;
    private EntityRepository entityRepository;

    @Before
    public void setUp() throws SlickException {
        entityRepository = new EntityRepository(map, new HashMap<String, EntityRepository.EntityData>()) {
            @Override
            protected Image loadImage(String pathToImage) throws SlickException {
                return mock(Image.class);
            }
        };
    }

    @Test
    public void createUnitCreatesUnitData() throws SlickException {
        int widthInPixels = 32;
        int heightInPixels = 32;
        int idOfEntity = 1;
        entityRepository.createUnit(idOfEntity, "quad.png", widthInPixels, heightInPixels);

        EntityRepository.EntityData data = entityRepository.getEntityData(EntityRepository.EntityType.UNIT, idOfEntity);

        Assert.assertEquals(EntityRepository.EntityType.UNIT, data.type);
        Assert.assertEquals(widthInPixels, data.width);
        Assert.assertEquals(heightInPixels, data.height);
    }

    @Test
    public void createStructureCreatesStructureData() throws SlickException {
        int widthInPixels = 64;
        int heightInPixels = 64;
        int idOfEntity = 1;
        entityRepository.createStructure(idOfEntity, "constyard.png", widthInPixels, heightInPixels);

        EntityRepository.EntityData data = entityRepository.getEntityData(EntityRepository.EntityType.STRUCTURE, idOfEntity);

        Assert.assertEquals(EntityRepository.EntityType.STRUCTURE, data.type);
        Assert.assertEquals(widthInPixels, data.width);
        Assert.assertEquals(heightInPixels, data.height);
    }

}