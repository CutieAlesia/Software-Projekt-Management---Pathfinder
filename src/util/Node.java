package Util;

/**
 * Basic Node Class that can be used for all search algorithms
 * 
 * @author Finn
 */
public class Node {

	// x and y coordinates
	private int vertIndex;
	private int horIndex;
	
	// path costs and heusristics
	private int costs;
	private int estimatedCosts;
	
	private NodeType type;
	private Node prev;
	
	public Node(int vertIndex, int horIndex) {
		this.vertIndex = vertIndex;
		this.horIndex = horIndex;
		this.costs = -1;
		this.estimatedCosts = 0;
		this.type = NodeType.NORMAL;
	}

	public int getCosts() {
		return this.costs;
	}
	
	public void setCosts(int costs) {
		this.costs = costs;
	}
	
	public int getEstimatedCosts() {
		return this.estimatedCosts;
	}
	
	public void setEstimatedCosts(int estimatedCosts) {
		this.estimatedCosts = estimatedCosts;
	}
	
	public NodeType getType() {
		return this.type;
	}
	
	public void setType(NodeType type) {
		this.type = type;
	}
	
	public int getTotalCosts() {
		return this.costs + this.estimatedCosts;
	}
	
	public int getVertIndex() {
		return this.vertIndex;
	}
	
	public int getHorIndex() {
		return this.horIndex;
	}
	
	public Node getPrev() {
		return this.prev;
	}
	
	public void setPrev(Node node) {
		this.prev = node;
	}
	
	/**
	 * Creates a colorful String that describes the node, based on the nodes type
	 */
	@Override
	public String toString() {
		String prefix = Color.ANSI_WHITE_BACKGROUND;
		String openingBracket = Color.ANSI_BLACK + "[";
		String closingBracket = Color.ANSI_BLACK + "]";
		String textColor = "";
		String formCosts = String.format("%03d", costs);
		String formEstCosts = String.format("%03d", estimatedCosts);
		String value1 = costs != -1 ? formCosts : "###";
		String value2 = estimatedCosts != 0 ? formEstCosts : "###";
		
		switch(type) {
			case START:
				textColor = Color.ANSI_GREEN;
				break;
			case END:
				textColor = Color.ANSI_RED;
				break;
			case VISITED:
				prefix = Color.ANSI_CYAN_BACKGROUND;
				break;
			case BLOCKED:
				prefix = Color.ANSI_YELLOW_BACKGROUND;
				break;
			case PATH:
				prefix = Color.ANSI_BLUE_BACKGROUND;
				break;
			default:
				break;
		}
		
		return prefix + openingBracket + textColor + value1 + ", " + value2 + closingBracket;
	}
}
