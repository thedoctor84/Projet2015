package fr.univavignon.courbes.agents.groupe10;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

import java.util.Vector;
import java.util.HashSet;
import fr.univavignon.courbes.common.Board;

/**
 * Agent artificiel du groupe 10
 * @author uapv1504323
 * @author patate maison
 * @author uapv1402375
 */
public class AgentImpl extends Agent {

	/**
	 * Constructeur
	 * @param playerId
	 * id du joueur
	 */
	public AgentImpl(Integer playerId) {
		super(playerId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Direction processDirection() {
		// TODO Auto-generated method stub
		return null;
		
	}
	
	
	/**
	 * Récupère toutes les positions de tous les serpent et crée un set.
	 */
	public void CreateBoard()
	{
		HashSet<Position> hs = new HashSet<Position>();
		Board board = getBoard();
		
		for(Snake snake : board.snakes)
		{
			for(Position pos : snake.oldTrail)
			{
				hs.add(pos);
			}
			
			for (Position pos : snake.newTrail)
			{	
				hs.add(pos);  
			}
		}
	}
	
	
	/**
	 * @param dep position de départ
	 * @param arr position d'arrivée
	 * @return tableau contenant le chemin le plus court
	 */
	public Vector<Position> a_etoile(Position dep, Position arr)
	{
		//TODO a*
	}

}
