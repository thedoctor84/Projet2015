package fr.univavignon.courbes.stats;

import java.util.Map;

/**
 * @author antoine
 * La classe EloRankSystem sert pour le calcul du classement des joueurs
 * C'est une version adaptée pour le multijoueurs
 */
public class EloRankSystem {
	// http://setastyle.free.fr/chess/elo.php
	// http://gobase.org/studying/articles/elo/
	

	/**
	 * Le K-Factor
	 */
	private static int K = 32;

	/**
	 * Map contenant pour chaque id joueur le rank Elo associé
	 */
	private static Map<Integer,Integer> eloRank;

	
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
	public static double scoreTheoriqueMultijoueurs(int playerID) {
		
		return 1.0;
	}
}
