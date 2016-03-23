package fr.univavignon.courbes.stats;

public class EloRankSystem {
	
	private int K;

	public int NewEloWinner(int OldElo) {
		// http://setastyle.free.fr/chess/elo.php
		// http://gobase.org/studying/articles/elo/
	double theoricalScore = 0.5;
	int newElo = (int)(OldElo+K*(1-theoricalScore));
	return newElo;
	}
}
