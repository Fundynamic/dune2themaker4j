package com.fundynamic.d2tm.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;


public class StringUtilsTest {

    @Test
    public void constructorTest() {
        // ok this is just for coverage :'(
        new StringUtils();
    }

    @Test
    public void nullIsEmpty() {
        Assert.assertThat(StringUtils.isEmpty(null), is(true));
    }

    @Test
    public void emptyStringIsEmpty() {
        Assert.assertThat(StringUtils.isEmpty(""), is(true));
    }

    @Test
    public void stringWithSpaceIsNotEmpty() {
        Assert.assertThat(StringUtils.isEmpty(" "), is(false));
    }

    @Test
    public void stringWithContentIsNotEmpty() {
        Assert.assertThat(StringUtils.isEmpty("foo"), is(false));
    }

}