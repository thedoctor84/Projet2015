package fr.univavignon.courbes.stats;

import java.io.IOException;
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
	private static int[] eloRankPlayers;
	private static double[] scoreReelPlayers;
	private static double[] scoreTheoriquePlayers;
	
	
	
	public static void majElo(Player[] allPlayers) {
		players = new Player[allPlayers.length];
		players = allPlayers;
		// On trie le tableau par score décroissant, le meilleur score est a l'indice 0 après le tri
		for(int i=players.length-1;i>=1;i--)
		{
			for(int j=0;j<=i-1;j++)
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
			System.out.println("Apres echange : indice:"+k+",nom : "+player.profile.userName+", score :"+player.totalScore+", playerId :"+player.playerId);
			//eloRank.put(player.profile.profileId, i+700); // CHANGER i+700 AVEC GETELO
		}
		getEloPlayers();
		scoreTheoriqueMultijoueurs();
		scoreReelMultijoueurs();
		NewElo();
		affiche();
	}
	/**
	 * Affiche les valeurs des tableaux (
	 */
	public static void affiche()
	{
		for(int k=0;k<players.length;k++)
		{
			System.out.println("Tableau players : Length :"+players.length+"indice:"+k+",nom : "+players[k].profile.userName+", score :"+players[k].totalScore+", playerId :"+players[k].playerId);
			System.out.println("Tableau Elo Classement : Length :"+eloRankPlayers.length+"indice:"+k+",Elo : "+eloRankPlayers[k]);
			System.out.println("Tableau scoreReelPlayers : Length :"+scoreReelPlayers.length+"indice:"+k+",score: "+scoreReelPlayers[k]);
			System.out.println("Tableau scoreTheoriquePlayers : Length :"+scoreTheoriquePlayers.length+"indice:"+k+",score: "+scoreTheoriquePlayers[k]);
		}
	}
	public static void NewElo()
	{
		int nouvelElo = 0;
		for(int i=0; i<eloRankPlayers.length;i++)
		{
			nouvelElo = (int)(eloRankPlayers[i]+K*(scoreReelPlayers[i]-scoreTheoriquePlayers[i]));
			System.out.println("Ancien elo de "+players[i].profile.userName+" = "+eloRankPlayers[i]);
			System.out.println("Nouvel elo de "+players[i].profile.userName+" = "+nouvelElo);
		}
		
	}
	
	
	/**
	 * @param Pseudo
	 * Le Pseudo du joueur
	 * @return 
	 * Retourne son classement Elo
	 */
	public static int getElo(String Pseudo)
	{
		String url ="http://93.118.34.229//returnelo.php?pseudo="+Pseudo;
		String eloString="";
		int elo = 0;
		Profil_Res joueur = new Profil_Res(url);
		try {
			eloString = joueur.get();
			elo = Integer.parseInt(eloString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elo;
		
	}
	/**
	 * Récupère le Elo de chaque joueur de la partie
	 */
	public static void getEloPlayers(){
		int n = players.length;
		eloRankPlayers = new int[n];
		for(int i=0; i < n; i++)
		{
			eloRankPlayers[i] = getElo(players[i].profile.userName);
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
	public static double scoreTheorique2Joueurs(int ancienEloA, int ancienEloB) {
		return 1.0/(1.0+Math.pow(10.0, (((double)(ancienEloA-ancienEloB))/400.0)));
	}
	
	
	/**
	 * Calcule l'estimation du score (score théorique) pour chaque joueur
	 */
	public static void scoreTheoriqueMultijoueurs() {
		int n = players.length;
		scoreTheoriquePlayers = new double[n];
		double score = 0;
		for(int i=0; i < n; i++)
		{
			for(int j=0; j < n; j++)
			{
				if(i!=j)
				{
					score = score+scoreTheorique2Joueurs(eloRankPlayers[i],eloRankPlayers[j]) ;
				}
			}
			score = score/(double)(n*(n-1)/2);
			scoreTheoriquePlayers[i] = score;
			score=0;
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
			score=(double)(n-(i+1))/(double)(n*(n-1)/2);
			scoreReelPlayers[i] = score;
		}
	}
}
