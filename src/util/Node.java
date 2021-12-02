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
}
