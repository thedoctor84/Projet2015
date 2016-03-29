package fr.univavignon.courbes.stats;

import java.util.Map;

import fr.univavignon.courbes.common.Player;

/**
 * @author antoine
 * La classe EloRankSystem sert pour le calcul du classement des joueurs
 * C'est une version adaptée pour le multijoueurs
 */
public class EloRankSystem {
	// http://setastyle.free.fr/chess/elo.php
	// http://gobase.org/studying/articles/elo/
	// http://sradack.blogspot.fr/2008/06/elo-rating-system-multiple-players.html

	/**
	 * Le K-Factor
	 */
	private static int K = 32;
	private static Player[] players;
	
	
	public static void majElo(Player[] allPlayers) {
		EloRankSystem.players = allPlayers;
		for(int i=0;i<players.length;i++)
		{
			Player player = players[i];
			//classementPartieJoueurs.put(player.profile.profileId,players.length-i);
			System.out.println("indice:"+i+",nom : "+player.profile.userName+", score :"+player.totalScore+", playerId :"+player.playerId);
			//eloRank.put(player.profile.profileId, i+700); // CHANGER i+700 AVEC GETELO
			scoreTheoriqueMultijoueurs();
			scoreReelMultijoueurs();
		}
	}
	
	/**
	 * 
	 * @param ancienEloA
	 * Ancien rank Elo d'un joueur A
	 * @param ancienEloB
	 * Ancien rank Elo d'un joueur B
	 * @return
	 * Retourne Le score attendu en fonction du rank des 2 joueurs (entre 0 et 1)
	 */
	public static double scoreTheorique2joueurs(int ancienEloA, int ancienEloB) {
		return 1.0/(1.0+Math.pow(10.0, (((double)(ancienEloB-ancienEloA))/400.0)));
	}
	
	/**
	 * Adaptation du système Elo
	 * @return
	 */
	public static double scoreTheoriqueMultijoueurs() {
		int n = players.length;
		int score = 0;
			score = score/(n*(n-1)/2);
		return score;
	}
	/**
	 * Mets dans la map scoreReel le taux scoreReel de la fin de la partie
	 */
	public static void scoreReelMultijoueurs() {
		// nombre de joueurs dans la partie
		int n = players.length;
		// score reel de chaque joueur, init a 0
		int score = 0;
			score = n/(n*(n-1)/2);
	}
}
