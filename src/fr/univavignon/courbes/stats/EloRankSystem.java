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

	/**
	 * Map contenant pour chaque id joueur le rank Elo associé
	 */
	private static Map<Integer,Integer> eloRank;
	private static Map<Integer,Integer> scoreTheorique;
	private static Map<Integer,Integer> scoreReel;
	private static Map<Integer,Integer> classementPartieJoueurs;
	
	
	public static void majElo(Player[] players) {
		for(int i=0;i<players.length;i++)
		{
			Player player = players[i];
			classementPartieJoueurs.put(player.profile.profileId,players.length-i);
			System.out.println("nom : "+player.profile.userName+", score :"+player.totalScore+", playerId :"+player.playerId);
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
		int n = classementPartieJoueurs.size();
		int score = 0;
		for(Map.Entry<Integer, Integer> entry : classementPartieJoueurs.entrySet())
		{
			score = (n-entry.getValue())/(n*(n-1)/2);
			scoreTheorique.put(entry.getKey(),score);
		}
		return 1.0;
	}
	/**
	 * Mets dans la map scoreReel le taux scoreReel de la fin de la partie
	 */
	public static void scoreReelMultijoueurs() {
		// nombre de joueurs dans la partie
		int n = classementPartieJoueurs.size();
		// score reel de chaque joueur, init a 0
		int score = 0;
		for(Map.Entry<Integer, Integer> entry : classementPartieJoueurs.entrySet())
		{
			score = (n-entry.getValue())/(n*(n-1)/2);
			scoreReel.put(entry.getKey(),score);
		}
	}
}
