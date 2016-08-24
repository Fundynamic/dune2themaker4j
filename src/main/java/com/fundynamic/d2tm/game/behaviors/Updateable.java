package com.fundynamic.d2tm.game.behaviors;


/**
 * An interface marking a thing that can be 'updated'. This implies any class that implements this interface
 * has state that has to be updated within the game.
 */
public interface Updateable {

    /**
     * <p>
     *     Update internal state, with given float value of passed time in seconds.
     * </p>
     * <p>
     *     With high FPS, the deltaInSeconds is very low. Ie, 2 FPS means deltaInSeconds of 0.5
     * </p>
     * @param deltaInSeconds
     */
    void update(float deltaInSeconds);
}
