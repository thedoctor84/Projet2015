package fr.univavignon.courbes.agents.groupe10;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

import java.util.Vector;
import java.util.HashSet;
import java.util.Map;
import java.lang.reflect.Array;
import java.util.HashMap;
import fr.univavignon.courbes.common.Board;

/**
 * Agent artificiel du groupe 10
 * @author uapv1504323
 * @author uapv1402334
 * @author uapv1402375
 */
public class AgentImpl extends Agent {

	
	class Node{public Position pos; public Node parent;public int cout,heuristique;}
	
	/**
	 * Constructeur
	 * @param playerId
	 * id du joueur
	 */
	public AgentImpl(Integer playerId) {
		super(playerId);
	}

	@Override
	public Direction processDirection() {
		Board b = getBoard();
		if(b == null)
			return Direction.NONE;
		
		
		Snake agentsnake = b.snakes[getPlayerId()];
		Position asnake = new Position(agentsnake.currentX, agentsnake.currentY);
		HashSet<Position> result = null;
		for(Snake snake : b.snakes)
		{
			Position othersnake = new Position(snake.currentX, snake.currentY);
			if(snake.playerId != getPlayerId())
			{
			}
		}
		Position pos = new Position();
		pos.x=100;
		pos.y=100;
		//System.out.println(pos.x +" "+pos.y);
		System.out.println(trouveroute(agentsnake,pos));
		return trouveroute(agentsnake, pos);
		
		
		/**if(result != null)
		{
			for(Position pos : result)
			{
				pos.x=100;
				pos.y=100;
				//System.out.println(pos.x +" "+pos.y);
				System.out.println(trouveroute(agentsnake,pos));
				return trouveroute(agentsnake, pos);
			}
		}*/
		
		//return Direction.NONE;
		
	}
	
	
	/**
	 * @param snake snake du bot
	 * @param dest destination trouvé par a *
	 * @return direction
	 */
	public Direction trouveroute(Snake bot, Position dest)
	{
		
		
		if(bot.currentAngle >= (3*Math.PI)/2 && bot.currentAngle <= 2*Math.PI)
		{
			System.out.println("1");
			if(bot.currentX <= dest.x && bot.currentY >= dest.y)
			{
				return Direction.LEFT;
			}
			
			if(bot.currentX <= dest.x && bot.currentY <= dest.y)
			{
				return Direction.LEFT;
			}
			
			if(bot.currentX >= dest.x && bot.currentY >= dest.y)
			{
				return Direction.RIGHT;
			}
			
			if(bot.currentX >= dest.x && bot.currentY <= dest.y)
			{
				return Direction.RIGHT;
			}
		}
		
		if(bot.currentAngle >= Math.PI && bot.currentAngle <= (3*Math.PI)/2)
		{
			System.out.println("2");
			if(bot.currentX <= dest.x && bot.currentY >= dest.y)
			{
				return Direction.LEFT;
			}
			
			if(bot.currentX <= dest.x && bot.currentY <= dest.y)
			{
				return Direction.RIGHT;
			}
			
			if(bot.currentX >= dest.x && bot.currentY >= dest.y)
			{
				return Direction.LEFT;
			}
			
			if(bot.currentX >= dest.x && bot.currentY <= dest.y)
			{
				return Direction.RIGHT;
			}
		}
		
		if(bot.currentAngle >= Math.PI/2 && bot.currentAngle <= Math.PI)
		{
			System.out.println("3");
			if(bot.currentX <= dest.x && bot.currentY >= dest.y)
			{
				return Direction.LEFT;
			}
			
			if(bot.currentX <= dest.x && bot.currentY <= dest.y)
			{
				return Direction.RIGHT;
			}
			
			if(bot.currentX >= dest.x && bot.currentY >= dest.y)
			{
				return Direction.LEFT;
			}
			
			if(bot.currentX >= dest.x && bot.currentY <= dest.y)
			{
				return Direction.RIGHT;
			}
		}
		
		if(bot.currentAngle >= 0 && bot.currentAngle <= Math.PI/2)
		{
			System.out.println("4");
			if(bot.currentX <= dest.x && bot.currentY >= dest.y)
			{
				return Direction.RIGHT;
			}
			
			if(bot.currentX <= dest.x && bot.currentY <= dest.y)
			{
				return Direction.LEFT;
			}
			
			if(bot.currentX >= dest.x && bot.currentY >= dest.y)
			{
				return Direction.RIGHT;
			}
			
			if(bot.currentX >= dest.x && bot.currentY <= dest.y)
			{
				return Direction.LEFT;
			}
		}
		
		
		return Direction.NONE;
		
		
		
	}
	
	
	/**
	 * Récupère toutes les positions de tous les serpent et crée un set.
	 * @return hs set contenant toutes les positions des snakes
	 */
	public HashSet<Position>  CreateBoard()
	{
		checkInterruption();
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
		checkInterruption();
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
		checkInterruption();
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
		checkInterruption();
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
		checkInterruption();
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
		checkInterruption();
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
		checkInterruption();
		HashSet<Position> voisin = new HashSet<Position>();
		
		Position un = new Position(pos.x-10,pos.y-10);
		Position deux = new Position(pos.x-10,pos.y);
		Position trois = new Position(pos.x-10,pos.y+10);
		Position quatre = new Position(pos.x,pos.y-10);
		Position cinq = new Position(pos.x,pos.y+10);
		Position six = new Position(pos.x+10,pos.y-10);
		Position sept = new Position(pos.x+10,pos.y);
		Position huit = new Position(pos.x+10,pos.y+10);
		
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
	public HashSet<Position> a_etoile(Position dep, Position arr)
	{
		checkInterruption();
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
			if((u.pos.x <= arr.x + 10 && u.pos.x >= arr.x - 10) && (u.pos.y <= arr.y + 10 && u.pos.y >= arr.y - 10))
			{
				//System.out.println("Fin");
				HashSet<Position> chemin = new HashSet<Position>();
				Node temp = u;
				
				while(temp.parent != null)
				{
					
					chemin.add(temp.pos);
					temp = temp.parent;
				}
				return chemin; 
			}

			else
			{
				HashSet<Position> voisin = returnNeighbors(u.pos);
				for(Position pos : voisin)
				{
					if(PosLibre(pos))
					{
						if(PosExistWithInfCost(pos,closed,(u.cout+1)) || PosExistWithInfCost(pos,open,(u.cout+1)))
						{
							continue;
						}
						else
						{
							if(PosExist(pos,open))
							{
								Node temp = findWithPos(pos,open);
								temp.cout = u.cout+1;
								temp.heuristique = temp.cout + volOiseau(pos,arr);
								temp.parent = u;
								temp.pos = pos;
							}
							else
							{	
								Node n = new Node();
								n.cout = u.cout + 1;
								n.heuristique = n.cout + volOiseau(pos,arr);
								n.parent = u;
								n.pos = pos;
								open.put(n, n.heuristique);
							}
						}
					}
					else
					{
						continue;
					}
				}
			}
			closed.put(u, u.heuristique);
		}
		System.out.println("Erreur dans la fonction a*");
		return null;

	}
	
	/**
	 * @param a position à verifier
	 * @return true or false
	 */
	public boolean PosLibre(Position a)
	{
		checkInterruption();
		boolean check = true;
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
		
		for(Position pos : hs) 
		{

			if((a.x <= pos.x + 10 && a.x >= pos.x - 10) && (a.y <= pos.y + 10 && a.y >= pos.y - 10))
			{
				check = false;
			}

		}

		if(a.x >= board.width - 10 || a.x <= 10)
		{
			check = false;
		}

		if(a.y >= board.height - 10 || a.y <= 10)
		{
			check= false;
		}
		
		return check;
	}

}
