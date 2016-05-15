package fr.univavignon.courbes.agents.groupe10;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashMap;
import fr.univavignon.courbes.common.Board;

/**
 * Agent artificiel du groupe 10
 * @author uapv1504323
 * @author uapv1402334
 * @author uapv1402375
 */
public class AgentImpl extends Agent {

	/** Notre agent */
	Snake agentsnake;
	/** Distance en pixels à partir de laquelle on considère qu'on est dans un coin */
	private static int CORNER_THRESHOLD = 100;
	/** Direction précédemment choisie par cet agent */
	private Direction previousDirection = Direction.NONE;
	
	/**
	 * La classe Node representant un noeud sert à l'algorithme A*
	 *
	 */
	class Node {
		public Position pos; 
		public Node parent;
		public double cout,heuristique;
	}
	
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
		checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction dir = null;
		Board board = getBoard();
		if(board == null)
			return Direction.NONE;
		
		
		agentsnake = board.snakes[getPlayerId()];
		Position asnake = new Position(agentsnake.currentX, agentsnake.currentY);
		HashSet<Position> result = null;
		for(Snake snake : board.snakes)
		{
			checkInterruption();
			Position othersnake = new Position(snake.currentX, snake.currentY);
			if(snake.playerId != getPlayerId())
			{
				result = a_etoile(asnake,othersnake);
			}
		}
		
		if(result != null)
		{
			/**if(previousDirection!=Direction.NONE && isInCorner())
			{
				dir = previousDirection;
			}
			else
			{
				updateAngles();
				System.out.println("BBBB");
				double closestObstacle[] = {Double.POSITIVE_INFINITY, 0};

				for(int i=0;i<board.snakes.length;++i)
				{	checkInterruption();	// on doit tester l'interruption au début de chaque boucle
					Snake snake = board.snakes[i];
	
					// on traite seulement les serpents des autres joueurs
					if(i != getPlayerId())
						// on met à jour la distance à l'obstacle le plus proche
						processObstacleSnake(snake, closestObstacle);
				}

				processObstacleBorder(closestObstacle);

				dir = getDodgeDirection(closestObstacle[1]);*/

				//if(dir == Direction.NONE)
				{	
					//System.out.println("ARRRRR");
					for(Position pos : result)
					{
						checkInterruption();
						//System.out.println(pos);
						previousDirection = trouveroute(agentsnake, pos);;
						return trouveroute(agentsnake, pos);
					}
				}
			//}
		}
		//previousDirection = dir;
		return dir;
		
	}
	/** Moitié de l'angle de vision de l'agent, i.e. délimitant la zone traitée devant lui pour détecter des obstacles. Contrainte : doit être inférieure à PI */
	private static double ANGLE_WIDTH = Math.PI/2;
	
	/** Direction courante du serpent de l'agent */
	private double currentAngle;
	/** Borne inférieure de l'angle de vision de l'agent */
	private double lowerBound;
	/** Borne supérieure de l'angle de vision de l'agent */
	private double upperBound;
	
	/**
	 * Met à jour l'angle de vision de l'agent.
	 */
	private void updateAngles()
	{	
		checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// angle de déplacement
		currentAngle = agentsnake.currentAngle;
		// calcul des bornes de l'angle de vision du serpent
		lowerBound = currentAngle - ANGLE_WIDTH;
		upperBound = currentAngle + ANGLE_WIDTH;
	}
	
	/**
	 * Détermine si on considère que la tête du serpent de l'agent
	 * se trouve dans un coin de l'aire de jeu.
	 *  
	 * @return
	 * 		{@code true} ssi l'agent est dans un coin.
	 */
	private boolean isInCorner()
	{	
		checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		boolean result = agentsnake.currentX<CORNER_THRESHOLD && agentsnake.currentY<CORNER_THRESHOLD
			|| getBoard().width-agentsnake.currentX<CORNER_THRESHOLD && agentsnake.currentY<CORNER_THRESHOLD
			|| agentsnake.currentX<CORNER_THRESHOLD && getBoard().height-agentsnake.currentY<CORNER_THRESHOLD
			|| getBoard().width-agentsnake.currentX<CORNER_THRESHOLD && getBoard().height-agentsnake.currentY<CORNER_THRESHOLD;
		return result;
	}
	
	/**
	 * Reçoit un serpent et détermine le point le plus proche de sa
	 * trainée, ainsi que l'angle formé avec la position courante
	 * de la tête du serpent de cet agent.
	 * 
	 * @param snake
	 * 		Le serpent à traiter (un autre joueur).
	 * @param result
	 * 		Un tableau de réel contenant la distance du pixel le plus
	 * 		proche appartenant à un obstacle, et l'angle qu'il forme
	 * 		avec la position courante de cet agent.
	 */
	private void processObstacleSnake(Snake snake, double result[])
	{	
		checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
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
	
	/**
	 * Détermine le point le plus proche de la bordure constituant un
	 * obstacle pour cet agent, ainsi que l'angle formé avec la position 
	 * courante de la tête du serpent de cet agent.
	 * 
	 * @param result
	 * 		Un tableau de réel contenant la distance du pixel le plus
	 * 		proche appartenant à un obstacle, et l'angle qu'il forme
	 * 		avec la position courante de cet agent.
	 */
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
	
	/**
	 * Renvoie la direction permettant au serpent de s'écarter d'un angle donné.
	 * 
	 * @param angle 
	 * 		L'angle traité (entre 0 et 2PI).
	 * @return 
	 * 		Direction permettant de s'écarter de cet angle (ou {@code null} si 
	 * 		l'angle n'est pas visible).
	 */
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
	
	
	/**
	 * Teste si un angle donné est compris dans l'angle de vision d'un agent.
	 * 
	 * @param angle 
	 * 		L'angle testé (entre 0 et 2PI).
	 * @return 
	 * 		{@code true} ssi l'angle est visible par le serpent.
	 */
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
	 * En fonction de l'angle entre le snake et l'horizontale et en fonction de la où on veut aller
	 * retourne une direction
	 * @param bot snake du bot
	 * @param dest destination trouvé par a *
	 * @return direction
	 */
	public Direction trouveroute(Snake bot, Position dest)
	{
		checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		if(bot.currentAngle >= (3*Math.PI)/2 && bot.currentAngle <= (7*Math.PI)/4) // le cercle des radians est inversé.
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
	 * retourne le noeud avec la plus petite heuristique dans la map map
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
			Node u = plusPetitHeuristique(open); // on check en premier le noeud avec le plus petit cout heuristique dans open
												// car il se rapproche le  plus de l'arrivée
			open.remove(u);						//on enleve le noeud que l'on verifie
		
			if((u.pos.x <= arr.x + 10 && u.pos.x >= arr.x - 10) && (u.pos.y <= arr.y + 10 && u.pos.y >= arr.y - 10)) // si on atteint l'objectif
			{
				HashSet<Position> chemin = new HashSet<Position>();
				Node temp = u;
				
				while(temp.parent != null)
				{
					chemin.add(temp.pos);		// on met le chemin dans chemin X)
					temp = temp.parent;
				}
				System.out.println("------------------------------------------------------");
				return chemin;  // et on retourne
			}

			else
			{
				HashSet<Position> voisin = returnNeighbors(u.pos); // on regarde tous les voisins de notre position
				for(Position pos : voisin)
				{
					checkInterruption();
					
					if(PosLibre(pos)) // si il n'y a pas d'obstacle à cette position
					{
						
						if(PosExistWithInfCost(pos,closed,(u.cout+1)) || PosExistWithInfCost(pos,open,(u.cout+1))) // si la position existe deja dans open ou closed avec un cout inferieur on ne fait rien
						{
							//System.out.println("ARRRRRRRRRRRRRRRRGGGGGGGGGGGGGGGGGG");
							continue;
						}
						else
						{
							
							if(PosExist(pos,open)) // si la position existe deja mais avec un cout supérieur on la remplace
							{
								
								Node temp = findWithPos(pos,open);
								temp.cout = u.cout+1;
								temp.heuristique = temp.cout + volOiseau(pos,arr);
								temp.parent = u;
								temp.pos = pos;
							}
							else // sinon on la crée
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
			closed.put(u, u.heuristique); // on ajoute le noeud traité dans closed (qui regroupe les noeuds deja traités).
		}
		System.out.println("Erreur dans la fonction a*");
		return null;

	}
	
	/**
	 * verifie si la position présente un obstacle ou non
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

			if((a.x <= pos.x + 5 && a.x >= pos.x - 5) && (a.y <= pos.y + 5 && a.y >= pos.y - 5))
			{
				
				System.out.println(pos);
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
