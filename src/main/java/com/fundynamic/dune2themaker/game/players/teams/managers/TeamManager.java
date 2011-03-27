package com.fundynamic.dune2themaker.game.players.teams.managers;

import java.util.LinkedList;
import java.util.List;

import com.fundynamic.dune2themaker.game.players.Player;
import com.fundynamic.dune2themaker.game.players.teams.Team;

public class TeamManager {

	private final List<Team> teams;
	
	public TeamManager() {
		teams = new LinkedList<Team>();
	}
	
	public void addTeam(Team team) {
		this.teams.add(team);
	}
	
	public void removeTeam(Team team) {
		this.teams.remove(team);
	}
	
	public boolean isInTeam(Player player) {
		for (Team team : teams) {
			if (team.hasPlayer(player)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Team> getTeamsForPlayer(Player player) {
		List<Team> result = new LinkedList<Team>();
		for (Team team : teams) {
			if (team.hasPlayer(player)) {
				result.add(team);
			}
		}
		return result;
	}

	public List<Team> getTeams() {
		return teams;
	}
	
}
