package com.fundynamic.dune2themaker.game.players.teams;

import java.util.LinkedList;
import java.util.List;

import com.fundynamic.dune2themaker.game.players.Player;

/**
 * This objects represents a team. It has a list of all players that belong
 * to the same team.
 * 
 * @author Stefan
 *
 */
public class Team {

	private List<Player> players;
	
	public Team() {
		players = new LinkedList<Player>();
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public void removePlayer(Player player) {
		players.remove(player);
	}

	public boolean hasPlayer(Player player) {
		return players.contains(player);
	}
	
}
