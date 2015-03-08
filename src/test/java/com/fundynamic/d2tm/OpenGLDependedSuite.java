package com.fundynamic.d2tm;


import com.fundynamic.d2tm.game.entities.EntityRepositoryTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

//@RunWith(Suite.class)
//@SuiteClasses({EntityRepositoryTest.class})
public class OpenGLDependedSuite {

    @BeforeClass
    public static void setUp() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.setVSyncEnabled(true);
        Display.setTitle("d2tm unit test");
        Display.create();
    }

    @AfterClass
    public static void tearDown() {
        Display.destroy();
    }
}
