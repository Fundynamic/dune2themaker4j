package com.fundynamic.dune2themaker.game.players;

import com.fundynamic.dune2themaker.game.players.houses.House;
import com.fundynamic.dune2themaker.game.players.teams.Team;

public abstract class AbstractPlayer implements Player {

	private int credits;	// amount credits the player has
	private int score;		// score gained in the game
	
	private Team team;		// to what team does the player belong?
	private House house;	// 
	
}
