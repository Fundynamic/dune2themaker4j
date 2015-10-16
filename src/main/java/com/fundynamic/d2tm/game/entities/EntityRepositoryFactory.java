package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.newdawn.slick.SlickException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static com.fundynamic.d2tm.game.entities.EntitiesData.*;

public class EntityRepositoryFactory {

    public EntityRepositoryFactory() {

    }

    public EntitiesData load() throws SlickException {
        EntitiesData entitiesData = createNewEntitiesData();
        entitiesData.createParticle(EXPLOSION_NORMAL, "explosions/explosion_3.png", 48, 48, 5f);
        entitiesData.createParticle(EXPLOSION_SMALL_UNIT, "explosions/small_unit_explosion.png", 48, 48, 3f);
        entitiesData.createParticle(EXPLOSION_SMALL_BULLET, "explosions/small_bullet_explosion.png", 32, 32, 3f);

        entitiesData.createProjectile(ROCKET, "projectiles/LargeRocket.png", 48, 48, EXPLOSION_NORMAL, 160f, 200, 16);
        entitiesData.createProjectile(BULLET, "projectiles/SmallBullet.png", 6, 6, EXPLOSION_SMALL_BULLET, 160f, 15, 0);

        entitiesData.createUnit(QUAD, "units/quad.png", 32, 32, 3, 1.5F, 200, BULLET, EXPLOSION_SMALL_UNIT);
        entitiesData.createUnit(TRIKE, "units/trike.png", 28, 26, 4, 2.5F, 150, BULLET, EXPLOSION_SMALL_UNIT);

        entitiesData.createStructure(CONSTRUCTION_YARD, "structures/2x2_constyard.png", 64, 64, 5, 1000, EXPLOSION_NORMAL);
        entitiesData.createStructure(REFINERY, "structures/3x2_refinery.png", 96, 64, 5, 1500, EXPLOSION_NORMAL);
        return entitiesData;
    }

    public EntitiesData fromIni() {
        return fromResource(getClass().getResourceAsStream("rules.ini"));
    }

    public EntitiesData fromResource(InputStream inputStream) {
        try {
            Ini ini = new Ini(inputStream);
            Profile.Section structures = ini.get("STRUCTURES");
            String[] strings = structures.childrenNames();
            for (String structure : strings) {
                System.out.println("Structure " + structure + " found.");
            }
            return null;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read rules.ini", e);
        }
    }

    public EntitiesData createNewEntitiesData() {
        return new EntitiesData();
    }

    public EntityRepository create(Map map) {
        try {
            return new EntityRepository(map, new Recolorer(), load());
        } catch (SlickException e) {
            throw new IllegalStateException("Unable to create entity repository", e);
        }
    }

}
