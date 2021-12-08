package GUI.Generator;

import API.Models.Node;


/**
 * Interface. Classes that generate labyrinths should implement this.
 * @author
 *
 */
public interface LabyrinthGenerator {
	/**
	 * Should return a matrix with blocked and unblocked Nodes and a start and an end Node. 
	 * @param x
	 * @param y
	 * @return Node[][]
	 */
	Node[][] generateLabyrinth(int x, int y);

}
