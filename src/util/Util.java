package Util;

/**
 * Utility Class that contains methods for generating and handling Node[][] (fields)
 * 
 * @author Finn
 */
public class Util {

	/**
	 * Static method that takes a Node[][] and creates a colorful console output based on the nodes state (NodeType)
	 * 
	 * @param field Node[][] that is supposed to be printed
	 */
	public static void printField(Node[][] field) {
		for(int i=0; i<field.length; i++) {
			for(int j=0; j<field[0].length; j++) {
				Node node = field[i][j];
				String ob = Color.ANSI_BLACK + "[";
				String cb = Color.ANSI_BLACK + "]";
				String cc = node.getType() == NodeType.NORMAL ? "" : node.getType() == NodeType.START ? Color.ANSI_GREEN : Color.ANSI_RED;
				String formCosts = String.format("%03d", node.getCosts());
				String formEstCosts = String.format("%03d", node.getEstimatedCosts());
				String values = node.getCosts() == -1 ? "###, ###" : formCosts + ", " + formEstCosts;
				String content = ob + cc + values + cb;
				String prefix = Color.ANSI_WHITE_BACKGROUND;
					
				if(field[i][j].getType() == NodeType.PATH)
					prefix = Color.ANSI_BLUE_BACKGROUND;
				else if(field[i][j].getType() == NodeType.BLOCKED)
					prefix = Color.ANSI_YELLOW_BACKGROUND;
				else if(field[i][j].getType() == NodeType.VISITED)
					prefix = Color.ANSI_CYAN_BACKGROUND;
				
				System.out.print(prefix + content);
			}
			System.out.println("");
		}
		System.out.println();
	}
	
	/**
	 * Static method that creates a random Node[][] (field) based on a given width and height 
	 * 
	 * @param width Amount of columns in the field
	 * @param height Amount of rows in the field
	 * @return Node[][] Randomly generated field
	 */
	public static Node[][] generateField(int width, int height) {
		Node[][] field = new Node[height][width];
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				Node node = new Node(i, j);
				
				if(Math.random() < 0.3)
					node.setType(NodeType.BLOCKED);
					
				field[i][j] = node;
			}
		}
		
		placeStartAndEndNode(field);
		
		return field;
	}
	
	/**
	 * Static method that creates a random Node[][] (field) based on a given dimension, which means that width = height
	 * 
	 * @param dimension Amount of rows and columns in the field
	 * @return Node[][] Randomly generated field
	 */
	public static Node[][] generateField(int dimension) {
		return generateField(dimension, dimension);
	}
	
	/**
	 * Static method for placing a start and an end node into a given field
	 * 
	 * @param field Node[][] in which the start and end Nodes are supposed to be placed in
	 */
	public static void placeStartAndEndNode(Node[][] field) {
		field[0][0].setType(NodeType.START);
		field[field.length - 1][field[0].length - 1].setType(NodeType.END);
	}

	/**
	 * Returns a random Node from a given Node[][]
	 * 
	 * @param field Node[][] containing all nodes
	 * @return Node Random Node from the given Node[][]
	 */
	public static Node getRandomNode(Node[][] field) {
		return field[(int) (Math.random() * field.length)][(int) (Math.random() * field[0].length)];
	}
}
