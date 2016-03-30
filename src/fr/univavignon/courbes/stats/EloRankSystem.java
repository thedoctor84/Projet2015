package fr.univavignon.courbes.stats;

import java.util.Map;

import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.network.central.Profil_Res;

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
	private static double[] scoreReelPlayers;
	private static double[] scoreTheoriquePlayers;
	
	
	public static void majElo(Player[] allPlayers) {
		players = new Player[allPlayers.length];
		players = allPlayers;
		for(int k=0;k<players.length;k++)
		{
			System.out.println("AVANT echange : indice:"+k+",nom : "+players[k].profile.userName+", score :"+players[k].totalScore+", playerId :"+players[k].playerId);
		}
		for(int i=players.length-1;i>=1;i--)
		{
			for(int j=0;j<=i-1;j++)
			{
				if(players[j].totalScore < players[j+1].totalScore)
				{
					System.out.println("Score de "+j+" :"+players[j].totalScore);
					Player tmp = players[j+1];
					players[j+1] = players[j];
					players[j] = tmp;
					System.out.println("Score de "+j+" apres echange:"+players[j].totalScore);
				}
			}
		}
		for(int k=0;k<players.length;k++)
		{
			Player player = players[k];
			//classementPartieJoueurs.put(player.profile.profileId,players.length-i);
			System.out.println("Apres echange : indice:"+k+",nom : "+player.profile.userName+", score :"+player.totalScore+", playerId :"+player.playerId);
			//eloRank.put(player.profile.profileId, i+700); // CHANGER i+700 AVEC GETELO
			scoreTheoriqueMultijoueurs();
			scoreReelMultijoueurs();
		}
		Affiche();
	}
	public static void Affiche()
	{
		for(int k=0;k<players.length;k++)
		{
			System.out.println("Tableau players : Length :"+players.length+"indice:"+k+",nom : "+players[k].profile.userName+", score :"+players[k].totalScore+", playerId :"+players[k].playerId);
			System.out.println("Tableau scoreReelPlayers : Length :"+scoreReelPlayers.length+"indice:"+k+",score: "+scoreReelPlayers[k]);
			System.out.println("Tableau scoreTheoriquePlayers : Length :"+scoreTheoriquePlayers.length+"indice:"+k+",score: "+scoreTheoriquePlayers[k]);
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
		return 1.0/(1.0+Math.pow(10.0, (((double)(ancienEloA-ancienEloB))/400.0)));
	}
	
	/**
	 * Adaptation du système Elo
	 * @return
	 */
	
	/*public static int getElo(String Pseudo)
	{
		Profil_Res joueur = new Profil_Res("http://93.118.34.229//returnelo.php?pseudo=");
		
	}*/
	public static void scoreTheoriqueMultijoueurs() {
		int n = players.length;
		
		String pseudo="";
		scoreTheoriquePlayers = new double[n];
		double score = 0;
		for(int i=0; i < n; i++)
		{
			//pseudo=players.
			for(int j=0; j < n; j++)
			{
				if(i!=j)
				{
					//pseudo=
					score = score ;
				}
				
			}
			score = 5.0;
			scoreTheoriquePlayers[i] = score;
			
		}
	}
	/**
	 * Mets dans la map scoreReel le taux scoreReel de la fin de la partie
	 */
	public static void scoreReelMultijoueurs() {
		// nombre de joueurs dans la partie
		int n = players.length;
		scoreReelPlayers = new double[n];
		double score = 0;
		for(int i=0; i < n; i++)
		{
			System.out.println("coucou scorereel");
			score=(double)(n-(i+1))/(double)(n*(n-1)/2);
			System.out.println("score calcule:"+score);
			scoreReelPlayers[i] = score;
		}
	}
}
