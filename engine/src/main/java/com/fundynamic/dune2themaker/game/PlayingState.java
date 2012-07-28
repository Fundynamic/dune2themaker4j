package com.fundynamic.dune2themaker.game;


import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.infrastructure.control.Keyboard;
import com.fundynamic.dune2themaker.infrastructure.control.Mouse;
import com.fundynamic.dune2themaker.infrastructure.math.Random;
import com.fundynamic.dune2themaker.infrastructure.math.Vector2D;

import sun.reflect.Reflection;

public class PlayingState {

	private final TerrainFactory terrainFactory;

	private Map map;
	private Graphics graphics;
	private GameContainer gameContainer;

	private Keyboard keyboard;
	private Mouse mouse;


	private List<DrawableViewPort> drawableViewPorts = new ArrayList<DrawableViewPort>();
	private Viewport viewport;

	private boolean initialized;


	public PlayingState(GameContainer gameContainer, TerrainFactory terrainFactory) throws SlickException {
		this.terrainFactory = terrainFactory;
		this.graphics = gameContainer.getGraphics();
		this.gameContainer = gameContainer;

		this.keyboard = new Keyboard(gameContainer.getInput());
		this.mouse = new Mouse(gameContainer.getInput());

		// on map load...
		this.map = new Map(terrainFactory, 64, 64);
	}

	public void init() throws SlickException {
		if (!initialized) {
			this.map.init();
			initialized = true;
		}
	}

	public void update() {
		for (DrawableViewPort drawableViewPort : drawableViewPorts) {
			drawableViewPort.update();
		}

		if (mouse.isLeftMouseButtonPressed()) {
			try {
				Vector2D viewPortDrawingPosition = mouse.getVector2D();
				final Viewport newViewport;
				newViewport = new Viewport(Random.getRandomBetween(125, 350), Random.getRandomBetween(125, 350), this.map);
				drawableViewPorts.add(new DrawableViewPort(newViewport, viewPortDrawingPosition, new Vector2D(Random.getRandomBetween(0, 2048), Random.getRandomBetween(0, 2048))));
			} catch (SlickException e) {
				throw new IllegalStateException("Unable to create new viewport!");
			}
		}

		if (keyboard.isEscPressed()) {
			this.gameContainer.exit();
		}
	}

	public void render() throws SlickException {
		for (DrawableViewPort drawableViewPort : drawableViewPorts) {
			drawableViewPort.render();
		}
		this.graphics.drawString("Drawing " + drawableViewPorts.size() + " viewports.", 10, 30);
	}

	private class DrawableViewPort {
		private final Vector2D viewPortDrawingPosition;
		private Vector2D viewPortViewingPosition;
		private final Viewport viewport;

		private DrawableViewPort(Viewport viewport, Vector2D viewPortDrawingPosition, Vector2D viewPortViewingPosition) {
			this.viewPortDrawingPosition = viewPortDrawingPosition;
			this.viewPortViewingPosition = viewPortViewingPosition;
			this.viewport = viewport;
		}

		void render() throws SlickException {
			viewport.draw(graphics, viewPortDrawingPosition, viewPortViewingPosition);
		}

		void update() {
			if (keyboard.isKeyUpPressed()) {
				viewPortViewingPosition = viewPortViewingPosition.moveUp();
			}
			if (keyboard.isKeyDownPressed()) {
				viewPortViewingPosition = viewPortViewingPosition.moveDown();
			}
			if (keyboard.isKeyLeftPressed()) {
				viewPortViewingPosition = viewPortViewingPosition.moveLeft();
			}
			if (keyboard.isKeyRightPressed()) {
				viewPortViewingPosition = viewPortViewingPosition.moveRight();
			}
		}
	}

}
