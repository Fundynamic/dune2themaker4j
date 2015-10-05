package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class IsEntityTest {

    @Test
    public void testEntity() {
        Entity entity = mock(Entity.class);
        Entity entity2 = mock(Entity.class);
        IsEntity isEntity = new IsEntity(entity);

        assertThat(isEntity.test(entity), is(true));
        assertThat(isEntity.test(entity2), is(false));
    }


}