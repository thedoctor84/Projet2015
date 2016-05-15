package fr.univavignon.courbes.agents.groupe10;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

import java.util.Vector;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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

	Snake agentsnake;
	private static int CORNER_THRESHOLD = 100;
	private Direction previousDirection = Direction.NONE;
	
	class Node{public Position pos; public Node parent;public double cout,heuristique;}
	
	/**
	 * Constructeur
	 * @param playerId
	 * id du joueur
	 */
	public AgentImpl(Integer playerId) {
		super(playerId);
	}

	@Override
	public Direction processDirection() 
	{
		Direction dir = null;
		Board b = getBoard();
		if(b == null)
			return Direction.NONE;
		
		
		agentsnake = b.snakes[getPlayerId()];
		Position asnake = new Position(agentsnake.currentX, agentsnake.currentY);
		HashSet<Position> result = null;
		for(Snake snake : b.snakes)
		{
			Position othersnake = new Position(snake.currentX, snake.currentY);
			if(snake.playerId != getPlayerId())
			{
				result = a_etoile(asnake,othersnake);
			}
		}
		
		if(result != null)
		{
			if(previousDirection!=Direction.NONE && isInCorner())
				dir = previousDirection;
			
			else
			{

				updateAngles();
				System.out.println("BBBB");
				double closestObstacle[] = {Double.POSITIVE_INFINITY, 0};

				for(int i=0;i<b.snakes.length;++i)
				{	checkInterruption();	// on doit tester l'interruption au début de chaque boucle
				Snake snake = b.snakes[i];

				// on traite seulement les serpents des autres joueurs
				if(i != getPlayerId())
					// on met à jour la distance à l'obstacle le plus proche
					processObstacleSnake(snake, closestObstacle);
				}

				processObstacleBorder(closestObstacle);

				dir = getDodgeDirection(closestObstacle[1]);

				if(dir == Direction.NONE)
				{	
					System.out.println("ARRRRR");
					for(Position pos : result)
					{
						System.out.println(pos);
						previousDirection = trouveroute(agentsnake, pos);;
						return trouveroute(agentsnake, pos);
					}
				}

			}
		}
		previousDirection = dir;
		return dir;
		
	}
	
	private static double ANGLE_WIDTH = Math.PI/2;
	
	/** Direction courante du serpent de l'agent */
	private double currentAngle;
	/** Borne inférieure de l'angle de vision de l'agent */
	private double lowerBound;
	/** Borne supérieure de l'angle de vision de l'agent */
	private double upperBound;
	
	private void updateAngles()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// angle de déplacement
		currentAngle = agentsnake.currentAngle;
		// calcul des bornes de l'angle de vision du serpent
		lowerBound = currentAngle - ANGLE_WIDTH;
		upperBound = currentAngle + ANGLE_WIDTH;
	}
	
	private boolean isInCorner()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		boolean result = agentsnake.currentX<CORNER_THRESHOLD && agentsnake.currentY<CORNER_THRESHOLD
			|| getBoard().width-agentsnake.currentX<CORNER_THRESHOLD && agentsnake.currentY<CORNER_THRESHOLD
			|| agentsnake.currentX<CORNER_THRESHOLD && getBoard().height-agentsnake.currentY<CORNER_THRESHOLD
			|| getBoard().width-agentsnake.currentX<CORNER_THRESHOLD && getBoard().height-agentsnake.currentY<CORNER_THRESHOLD;
		return result;
	}
	
	
	private void processObstacleSnake(Snake snake, double result[])
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// on récupère les positions de la trainée (complète) du serpent
		Set<Position> trail = new TreeSet<Position>(snake.oldTrail);
		trail.addAll(snake.newTrail);
		
		// pour chaque position de cette trainée
		for(Position position: trail)
		{	checkInterruption();	// une boucle, donc un autre test d'interruption
			
			// on récupère l'angle entre la tête du serpent de l'agent 
			// et la position traitée (donc une valeur entre 0 et 2*PI)
			double angle = Math.atan2(position.y-agentsnake.currentY, position.x-agentsnake.currentX);
			if(angle<0)
				angle = angle + 2*Math.PI;
				
			// si la position est visible par le serpent de l'agent
			if(isInSight(angle))
			{	// on calcule la distance entre cette position et la tête du serpent de l'agent
				double dist = Math.sqrt(
					Math.pow(agentsnake.currentX-position.x, 2) 
					+ Math.pow(agentsnake.currentY-position.y,2));
					
				// si la position est plus proche que le plus proche obstacle connu : on met à jour
				if(dist<result[0])
				{	result[0] = dist;	// mise à jour de la distance
					result[1] = angle;	// mise à jour de l'angle
				}			
			}
		}
	}
	
	
	private void processObstacleBorder(double result[])
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// x = 0
		if(result[0]==Double.POSITIVE_INFINITY 
				|| isInSight(Math.PI) && agentsnake.currentX<result[0])
		{	result[0] = agentsnake.currentX;
			result[1] = Math.PI;
		}
		
		// y = 0
		if(isInSight(3*Math.PI/2) && agentsnake.currentY<result[0])
		{	result[0] = agentsnake.currentY;
			result[1] = 3*Math.PI/2;
		}
		
		// x = max_x
		if(isInSight(0) && getBoard().width-agentsnake.currentX<result[0])
		{	result[0] = getBoard().width - agentsnake.currentX;
			result[1] = 0;
		}
		
		// y == max_y
		if(isInSight(Math.PI/2) && getBoard().height-agentsnake.currentY<result[0])
		{	result[0] = getBoard().height - agentsnake.currentY;
			result[1] = Math.PI/2.0;
		}
	}
	
	
	private Direction getDodgeDirection(double angle) 
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction result = Direction.NONE;
		
		// on teste si l'angle est entre lowerBound et currentAngle 
		// attention : l'axe des y est orienté vers le bas
		// (en conséquence, par exemple, PI/2 est orienté vers le bas)
		if(angle>=lowerBound && angle<=currentAngle)
			result = Direction.RIGHT;

		// on teste si l'angle est entre currentAngle et upperBound
		else if(angle>=currentAngle && angle<=upperBound)
			result = Direction.LEFT;
		
		// premier cas limite : si la borne supérieure dépasse 2PI
		// on teste si l'angle est inférieur à upperBound - 2pi
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = Direction.LEFT;
		
		// second cas limite : si la borne inférieure est négative
		// on teste si l'angle est supérieur à lowerBound + 2PI
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = Direction.RIGHT;
		
		return result;
	}
	
	
	
	private boolean isInSight(double angle)
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		boolean result = false;
		
		if(angle>=lowerBound && angle<=upperBound)
			result = true;

		// premier cas limite : si la borne supérieure dépasse 2PI
		// on teste si l'angle est inférieur à upperBound - 2pi.
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = true;
			
		// second cas limite : si la borne inférieure est négative 
		// on teste si l'angle est supérieur à lowerBound + 2PI
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = true;
			
		return result;
	}
	
	
	/**
	 * @param snake snake du bot
	 * @param dest destination trouvé par a *
	 * @return direction
	 */
	public Direction trouveroute(Snake bot, Position dest)
	{
		checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		if(bot.currentAngle >= (3*Math.PI)/2 && bot.currentAngle <= (7*Math.PI)/4)
		{
			
			if(bot.currentX <= dest.x && bot.currentY >= dest.y)
			{
				return Direction.RIGHT;
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
				return Direction.LEFT;
			}
		}
		
		if(bot.currentAngle >= (7*Math.PI)/4 && bot.currentAngle <= (2*Math.PI)/1)
		{
			
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
		

		if(bot.currentAngle <= (3*Math.PI)/2 && bot.currentAngle >= (5*Math.PI)/4)
		{
			
			if(bot.currentX <= dest.x && bot.currentY >= dest.y)
			{
				return Direction.RIGHT;
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
				return Direction.LEFT;
			}
		}
		
		if(bot.currentAngle <= (5*Math.PI)/4 && bot.currentAngle >= (1*Math.PI)/1)
		{
			
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
		
	
		if(bot.currentAngle <= (1*Math.PI)/1 && bot.currentAngle >= (3*Math.PI)/4)
		{
			
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
		
		if(bot.currentAngle <= (3*Math.PI)/4 && bot.currentAngle >= (1*Math.PI)/2)
		{
			
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
		
		if(bot.currentAngle <= (1*Math.PI)/2 && bot.currentAngle >= (1*Math.PI)/4)
		{
			
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
		
		if(bot.currentAngle <= (1*Math.PI)/4 && bot.currentAngle >= 0)
		{
			
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
	public double volOiseau(Position dep, Position arr)
	{
		checkInterruption();
		double dist = Math.sqrt(
				Math.pow(dep.x-arr.x, 2) 
				+ Math.pow(dep.y-arr.y,2));

		return dist;
	}
	
	/**
	 * @param map map où chercher
	 * @return noeud avec le cout le plus faible.
	 */
	public Node plusPetitHeuristique(HashMap<Node,Double> map)
	{
		checkInterruption();
		double tmp = 1000000000;
		Node a = null;
		
		for(Map.Entry<Node,Double> e : map.entrySet())
		{
			if(e.getValue() < tmp)
			{
				tmp = e.getValue();
				a = e.getKey();
			}
		}
		//System.out.println("a "+a.cout);
		return a;
	}
	
	/**
	 * Retourne le noeud correspondant à la Position xy dans la HashMap map
	 * @param xy Position a trouver
	 * @param map HashMap dans laquelle trouver la position
	 * @return Node le noeud à la bonne position
	 */
	public Node findWithPos(Position xy, HashMap<Node,Double> map)
	{
		checkInterruption();
		for(Map.Entry<Node,Double> e : map.entrySet())
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
	public boolean PosExist(Position a, HashMap<Node,Double> map)
	{
		checkInterruption();
		for(Map.Entry<Node,Double> e : map.entrySet())
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
	public boolean PosExistWithInfCost(Position a, HashMap<Node,Double> map, double cost)
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
		HashMap<Node,Double> closed = new HashMap<Node,Double>();
		HashMap<Node,Double> open = new HashMap<Node,Double>();
		open.put(current, current.heuristique);
	
		while(!open.isEmpty())
		{
			//System.out.println(open.size());
			checkInterruption();
			Node u = plusPetitHeuristique(open);
			open.remove(u);
		
			if((u.pos.x <= arr.x + 10 && u.pos.x >= arr.x - 10) && (u.pos.y <= arr.y + 10 && u.pos.y >= arr.y - 10))
			{
				HashSet<Position> chemin = new HashSet<Position>();
				Node temp = u;
				
				while(temp.parent != null)
				{
					chemin.add(temp.pos);
					temp = temp.parent;
				}
				System.out.println("------------------------------------------------------");
				return chemin; 
			}

			else
			{
				HashSet<Position> voisin = returnNeighbors(u.pos);
				for(Position pos : voisin)
				{
					checkInterruption();
					
					if(PosLibre(pos))
					{
						
						if(PosExistWithInfCost(pos,closed,(u.cout+1)) || PosExistWithInfCost(pos,open,(u.cout+1)))
						{
							//System.out.println("ARRRRRRRRRRRRRRRRGGGGGGGGGGGGGGGGGG");
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
		HashSet<Position> hs = new HashSet<Position>();
		Board board = getBoard();

		for(Snake snake : board.snakes)
		{
			for(Position pos : snake.oldTrail)
			{
				hs.add(pos);
			}

			/**for (Position pos : snake.newTrail)
			{	
				hs.add(pos);  
			}*/
		}
		
		for(Position pos : hs) 
		{

			if((a.x <= pos.x + 1 && a.x >= pos.x - 1) && (a.y <= pos.y + 1 && a.y >= pos.y - 1))
			{
				//System.out.println("TEST");
				return false;
			}

		}

		if(a.x >= board.width - 10 || a.x <= 10)
		{
			return false;
		}

		if(a.y >= board.height - 10 || a.y <= 10)
		{
			return false;
		}
		
		return true;
	}

}
