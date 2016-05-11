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
	 * @return hs set contenant toutes les positions des snakes
	 */
	public HashSet<Position>  CreateBoard()
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
		
		return hs;
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
	 * @param map map où chercher
	 * @return noeud avec le cout le plus faible.
	 */
	public Node plusPetitHeuristique(HashMap<Node,Integer> map)
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
	 * Retourne le noeud correspondant à la Position xy dans la HashMap map
	 * @param xy Position a trouver
	 * @param map HashMap dans laquelle trouver la position
	 * @return Node le noeud à la bonne position
	 */
	public Node findWithPos(Position xy, HashMap<Node,Integer> map)
	{
		for(Map.Entry<Node,Integer> e : map.entrySet())
		{
			if(e.getKey().pos.x == xy.x && e.getKey().pos.y == xy.y)
			{
				return e.getKey();
			}
		}
		return null;
	}
	
	/**
	 * Verifie si la position existe dans la map
	 * @param a Position a verifier
	 * @param map Map a verifier
	 * @return true or false
	 */
	public boolean PosExist(Position a, HashMap<Node,Integer> map)
	{
		for(Map.Entry<Node,Integer> e : map.entrySet())
		{
			if(e.getKey().pos.x == a.x && e.getKey().pos.y == a.y)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifie que le noeud avec la position a existe et que son cout est inférieur à cost
	 * @param a la position à verifier
	 * @param map la map où verifier
	 * @param cost le cout à ne pas dépasser
	 * @return true or false
	 */
	public boolean PosExistWithInfCost(Position a, HashMap<Node,Integer> map, int cost)
	{
		if(!PosExist(a,map))
		{
			return false;
		}
		
		Node temp = findWithPos(a, map);
		
		if(temp.cout <= cost)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @param pos Position depuis laquelle trouver les voisins
	 * @return HashSet contenant les positions de tous les voisins
	 */
	public HashSet<Position> returnNeighbors(Position pos)
	{
		HashSet<Position> voisin = new HashSet<Position>();
		
		Position un = new Position(pos.x-1,pos.y-1);
		Position deux = new Position(pos.x-1,pos.y);
		Position trois = new Position(pos.x-1,pos.y+1);
		Position quatre = new Position(pos.x,pos.y-1);
		Position cinq = new Position(pos.x,pos.y+1);
		Position six = new Position(pos.x+1,pos.y-1);
		Position sept = new Position(pos.x+1,pos.y);
		Position huit = new Position(pos.x+1,pos.y+1);
		
		voisin.add(un);
		voisin.add(deux);
		voisin.add(trois);
		voisin.add(quatre);
		voisin.add(cinq);
		voisin.add(six);
		voisin.add(sept);
		voisin.add(huit);
		
		return voisin;
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
			Node u = plusPetitHeuristique(open);
			open.remove(u);
			if(u.pos.x == arr.x && u.pos.y == arr.y)
			{
				//TODO reconstituer et retourner le chemin
			}
			
			else
			{
				HashSet<Position> voisin = returnNeighbors(u.pos);
				for(Position pos : voisin)
				{
					
				}
			}
		}
		System.out.println("Erreur dans la fonction a*");
		return null;
		
	}

}
