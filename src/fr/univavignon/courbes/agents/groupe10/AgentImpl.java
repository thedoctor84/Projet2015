package fr.univavignon.courbes.agents.groupe10;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

import java.util.Vector;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import fr.univavignon.courbes.common.Board;

/**
 * Agent artificiel du groupe 10
 * @author uapv1504323
 * @author patate maison
 * @author uapv1402375
 */
public class AgentImpl extends Agent {

	
	class Node{public Position pos, parent;public int cout,heuristique;}
	
	
	
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
	 * @param dep Position de départ
	 * @param arr Position d'arrivé
	 * @return distance entre les deux sans prendre en compte les obstacles
	 */
	public int volOiseau(Position dep, Position arr)
	{
		int result = 0;
		if(dep.x >= arr.x)result += dep.x - arr.x;
		else result += arr.x - dep.x;
		if(dep.y >= arr.y) result += dep.y - arr.y;
		else result += arr.y - dep.y;
		return result;
	}
	
	/**
	 * @param map
	 * @return noeud avec le cout le plus faible.
	 */
	public Node plusPetitCout(HashMap<Node,Integer> map)
	{
		int tmp = 1000000000;
		Node a = null;
		
		for(Map.Entry<Node,Integer> e : map.entrySet())
		{
			if(e.getValue() < tmp)
			{
				tmp = e.getValue();
				a = e.getKey();
			}
		}
		
		return a;
	}
	
	
	/**
	 * @param dep position de départ
	 * @param arr position d'arrivée
	 * @return tableau contenant le chemin le plus court
	 */
	public Vector<Position> a_etoile(Position dep, Position arr)
	{
		Node current = new Node();
		current.pos = dep;
		current.cout = 0;
		current.heuristique = volOiseau(dep,arr);
		HashMap<Node,Integer> closed = new HashMap<Node,Integer>();
		HashMap<Node,Integer> open = new HashMap<Node,Integer>();
		open.put(current, current.heuristique);
		
		while(!open.isEmpty())
		{
			Node u = plusPetitCout(open);
			open.remove(u);
			if(u.pos.x == arr.x && u.pos.y == arr.y)
			{
				//TODO reconstituer et retourner le chemin
			}
			
			else
			{
				
			}
		}
		System.out.println("Erreur dans la fonction a*");
		return null;
		
	}

}
