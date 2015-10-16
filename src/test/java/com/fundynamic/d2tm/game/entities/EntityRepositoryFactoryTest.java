package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class EntityRepositoryFactoryTest extends AbstractD2TMTest {

    private EntitiesData entitiesData;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        entitiesData = entityRepositoryFactory.fromResource(getClass().getResourceAsStream("test-rules.ini"));
    }

    @Test
    public void readsStructureFromIniFile() {
        EntityData constyard = entitiesData.getEntityData(EntityType.STRUCTURE, "CONSTYARD");
        assertThat(constyard, is(not(nullValue())));
        assertThat(constyard.hitPoints, is(230));
        assertThat(constyard.image, is(not(nullValue())));
        assertThat(constyard.width, is(64));
        assertThat(constyard.height, is(64));
        assertThat(constyard.sight, is(4));
        assertThat(constyard.explosionId, is("BOOM"));
    }


    @Test
    public void readsExplosionFromIniFile() {
        EntityData boom = entitiesData.getEntityData(EntityType.PARTICLE, "BOOM");
        assertThat(boom, is(not(nullValue())));
        assertThat(boom.image, is(not(nullValue())));
        assertThat(boom.width, is(48));
        assertThat(boom.height, is(48));
    }

}