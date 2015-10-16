package com.fundynamic.d2tm.game.entities;

import org.junit.Test;

import static org.junit.Assert.*;


public class EntityRepositoryFactoryTest {

    @Test
    public void bla() {
        EntityRepositoryFactory entityRepositoryFactory = new EntityRepositoryFactory();
        entityRepositoryFactory.fromResource(getClass().getResourceAsStream("test-rules.ini"));
    }

}