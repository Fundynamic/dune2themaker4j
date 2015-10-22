package com.fundynamic.d2tm.game.behaviors;


public interface Updateable {

    /**
     * Update internal state, with given float value of passed time in seconds. With high FPS, the deltaInSeconds
     * is very low. Ie, 2 FPS means deltaInSeconds of 0.5
     * @param deltaInSeconds
     */
    void update(float deltaInSeconds);
}
