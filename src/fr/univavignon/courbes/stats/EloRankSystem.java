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
	private static int[] scoreReelPlayers;
	
	
	public static void majElo(Player[] allPlayers) {
		EloRankSystem.players = allPlayers;
		for(int i=players.length-1;i>=1;i--)
		{
			for(int j=0;j<i-1;j++)
			{
				if(players[j].totalScore < players[j+1].totalScore)
				{
					Player tmp = players[j+1];
					players[j+1] = players[j];
					players[j] = tmp;
				}
			}
		}
		for(int k=0;k<players.length;k++)
		{
			Player player = players[k];
			//classementPartieJoueurs.put(player.profile.profileId,players.length-i);
			System.out.println("indice:"+k+",nom : "+player.profile.userName+", score :"+player.totalScore+", playerId :"+player.playerId);
			//eloRank.put(player.profile.profileId, i+700); // CHANGER i+700 AVEC GETELO
			scoreTheoriqueMultijoueurs();
			scoreReelMultijoueurs();
		}
	}
	
	/*public static NewElo(int profileID)
	{
		int nouvelElo = getElo(profileID)+K*()
	}/
	
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
	public static void scoreTheoriqueMultijoueurs() {
		int n = players.length;
		int score = 0;
			score = score/(n*(n-1)/2);
		//return score;
	}
	/**
	 * Mets dans la map scoreReel le taux scoreReel de la fin de la partie
	 */
	public static void scoreReelMultijoueurs() {
		// nombre de joueurs dans la partie
		int n = players.length;
		int score = 0;
		for(int i=0; i < players.length; i++)
		{
			score=(n-(i+1))/(n*(n-1)/2);
			scoreReelPlayers[i] = score;
		}
	}
}
