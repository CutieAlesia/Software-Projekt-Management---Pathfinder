import Util.Node;
import Util.NodeType;

/**
 * Base Class for all search algorithms
 * 
 * @author Finn
 */
public abstract class SearchAlgorithm {

	protected Node[][] field;
	protected Node start;
	protected Node end;
	
	/**
	 * Initializes the field and finds the start and end node
	 * @param field Node[][] that the algorithm is supposed to work with
	 */
	public SearchAlgorithm(Node[][] field) {
		this.field = field;
		findLocations();
	}
	
	public Node[][] getField() {
		return this.field;
	}
	
	/**
	 * Iterates over the field to find the start and end node 
	 */
	private void findLocations() {
		for(int i=0; i<field.length; i++) {
			for(int j=0; j<field.length; j++) {
				if(start != null && end != null)
					return;
				
				if(field[i][j].getType() == NodeType.START)
					start = field[i][j];
				
				if(field[i][j].getType() == NodeType.END)
					end = field[i][j];
			}
		}
	}
	
	/**
	 * abstract method for starting an algorithm
	 */
	public abstract void run();
}
