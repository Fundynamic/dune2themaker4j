package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.SlickException;

public interface Game {

	void init() throws Exception;

	void update() throws SlickException;

	void render() throws SlickException;

}