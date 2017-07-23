package com.fundynamic.d2tm.game.entities.predicates;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Predicate;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

public class OrPredicateTest {

    @Test
    public void returnsTrueWhenOnlyPredicateReturnsTrue() {
        OrPredicate orPredicate = new OrPredicate(new Predicate<Entity>() {
            @Override
            public boolean test(Entity entity) {
                return true;
            }
        });

        Assert.assertTrue(orPredicate.test(null));
    }

    @Test
    public void returnsTrueWhenOneOfPredicatesReturnsTrue() {
        OrPredicate orPredicate = new OrPredicate(Arrays.asList(
                new Predicate<Entity>() {
                    @Override
                    public boolean test(Entity entity) {
                        return false;
                    }
                },
                new Predicate<Entity>() {
                    @Override
                    public boolean test(Entity entity) {
                        return false;
                    }
                },
                new Predicate<Entity>() {
                    @Override
                    public boolean test(Entity entity) {
                        return true;
                    }
                }
        ));

        Assert.assertTrue(orPredicate.test(null));
    }

    @Test
    public void returnsFalseWhenNoneOfPredicatesReturnsTrue() {
        OrPredicate orPredicate = new OrPredicate(Arrays.asList(
                new Predicate<Entity>() {
                    @Override
                    public boolean test(Entity entity) {
                        return false;
                    }
                },
                new Predicate<Entity>() {
                    @Override
                    public boolean test(Entity entity) {
                        return false;
                    }
                }
        ));

        Assert.assertFalse(orPredicate.test(null));
    }
}