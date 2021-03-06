package fr.univavignon.courbes.stats;

import java.io.IOException;

import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.network.central.ProfilRes;

/**
 * @author antoine
 * La classe EloRankSystem sert pour le calcul du classement des joueurs
 * C'est une version adaptée pour le multijoueurs
 */
public class EloRankSystem {

	/**
	 * Le K-Factor
	 */
	private static int K = 32;
	/**
	 * Tableau de Joueurs, qui sera ordonné par score décroissant pour déterminer le classement
	 */
	private static Player[] players;
	
	/**
	 * Tableau d'entiers, contenant les classements Elo des joueurs (même ordre que le tableau players)
	 */
	private static int[] eloRankPlayers;
	/**
	 * Tableau de doubles contenant le score réel des joueurs (de 0 à 1)
	 */
	private static double[] scoreReelPlayers;
	/**
	 * Tableau de doubles contenant le score théorique des joueurs (de 0 à 1) en fonction de son classement
	 */
	private static double[] scoreTheoriquePlayers;
	
	
	
	/**
	 * Mets a jour le classement des joueurs qui était dans la partie
	 * @param allPlayers
	 * On récupère le tableau des Joueurs (class Player) en fonction de leur playerId
	 */
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
		getEloPlayers();
		scoreTheoriqueMultijoueurs();
		scoreReelMultijoueurs();
		NewElo();
		//affiche();
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
	/**
	 * Calcule le nouveau classement Elo de chaque joueur
	 */
	public static void NewElo()
	{
		String url ="";
		ProfilRes joueur;
		int nouvelElo = 0;
		for(int i=0; i<eloRankPlayers.length;i++)
		{
			nouvelElo = (int)(eloRankPlayers[i]+K*(scoreReelPlayers[i]-scoreTheoriquePlayers[i]));
			url="http://93.118.34.229/addelo.php?pseudo="+players[i].profile.userName+"&elo="+nouvelElo;
			joueur = new ProfilRes(url);
			try {
				joueur.get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("Ancien elo de "+players[i].profile.userName+" = "+eloRankPlayers[i]);
			//System.out.println("Nouvel elo de "+players[i].profile.userName+" = "+nouvelElo);
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
		String url ="http://93.118.34.229/returnelo.php?pseudo="+Pseudo;
		String eloString="";
		int elo = 0;
		ProfilRes joueur = new ProfilRes(url);
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
					score = score+scoreTheorique2Joueurs(eloRankPlayers[j],eloRankPlayers[i]) ;
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
