package com.fundynamic.d2tm.util;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Bootstraps the GLContext while running unit tests.
 */
public class Bootstrapper extends RunListener {

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);

        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.setVSyncEnabled(true);
        Display.setTitle("d2tm unit test");
        Display.create();
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);

        Display.destroy();
    }
}
