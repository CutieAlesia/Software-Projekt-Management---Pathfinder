import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Util.*;

/**
 * A* Algorithm class
 * 
 * @author Finn
 */
public class AStar extends SearchAlgorithm {
	
	private ArrayList<Node> relevantNodes;
	
	/**
	 * Initializes all fields
	 * @param field
	 */
	public AStar(Node[][] field) {
		super(field);
		this.relevantNodes = new ArrayList<Node>();
	}
	
	/**
	 * Starts the recursive algorithm and runs until a path is found or there is no valid path
	 */
	public void start() {
		boolean found = updateNeighbours(this.start);
		if(found) {
			Node node = end;
			
			// update the state for all nodes that are part of the found path
			while(node != null) {
				node.setType(NodeType.PATH);
				node = node.getPrev();
			}
		} else {
			System.out.println("Es wurde leider kein Weg gefunden!");
		}
	}
	
	/**
	 * Takes a node, updates all direct neighbours for that node and recursively calls itself if there are unvisited 
	 * nodes in the relevants node list left
	 * 
	 * @param node Node which neighbours are supposed to be updated
	 * @return boolean that describes whether a valid path was found or not
	 */
	private boolean updateNeighbours(Node node) {
		boolean found = false;
		
		// Coordinates of the neighbours that are supposed to be updated
		int[][] coords = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
		
		// Check if the given node is the start or end node
		if(node.getType() != NodeType.START && node.getType() != NodeType.END) {
			// mark the given node as visited
			node.setType(NodeType.VISITED);
		}
		
		// remove the given node from the relevants node list so the algorithm
		// doesn't get stuck in an infinite loop
		if(this.relevantNodes.contains(node)) {
			this.relevantNodes.remove(node);
		}
		
		// Iterate over all neighbour coordinates
		for(int[] coord : coords) {
			int i = coord[0];
			int j = coord[1];
			
			boolean leftInBound = node.getHorIndex() + j >= 0;
			boolean rightInBound = node.getHorIndex() + j < field[0].length;
			boolean topInBound = node.getVertIndex() + i >= 0;
			boolean bottomInBound = node.getVertIndex() + i < field.length;
			
			// Check if the neighbour coordinates are still in bound
			if(leftInBound && rightInBound && topInBound && bottomInBound) {
				Node neighbour = field[node.getVertIndex() + i][node.getHorIndex() + j];
				int costs = getDistance(node, neighbour) + (node.getCosts() != -1 ? node.getCosts() : 0);
				int estimatedCosts = getDistance(neighbour, end);
				
				// skip the current neighbour if the node is blocked, already visited or the start node
				if(neighbour.getType() == NodeType.START || neighbour.getType() == NodeType.BLOCKED || neighbour.getType() == NodeType.VISITED)
					continue;
				
				// update the neighbours costs if the neighbour hasn't been calculated yet or the new costs are cheaper
				if(costs + estimatedCosts < neighbour.getTotalCosts() || neighbour.getCosts() == -1) {
					neighbour.setCosts(costs);
					neighbour.setEstimatedCosts(estimatedCosts);
					neighbour.setPrev(node);
					
					// indicate that the end node was found
					if(neighbour.getType() == NodeType.END)
						found = true;
				}
				
				// if not already done, add neighbour to the relevant nodes list
				if(!relevantNodes.contains(neighbour)) {
					relevantNodes.add(neighbour);
				}
			}
		}
		
		Util.printField(field);
		Node nextToUpdate = getNextNode();
		// returns true if the end node was found, false if there is no valid path or recursively calls itself if there are still nodes to check
		return found ? true : nextToUpdate == null ? false : updateNeighbours(nextToUpdate);
	}
	
	/**
	 * returns the cheapest relevant node, based on the real path costs aswell as the heuristics
	 * 
	 * @return Node Currently cheapest relevant node
	 */
	private Node getNextNode() {
		if(this.relevantNodes.size() == 0)
			return null;
		
		sortRelevantNodes();
		return this.relevantNodes.get(0);
	}
	
	/**
	 * sort all relevant nodes by their total costs
	 */
	private void sortRelevantNodes() {
		Collections.sort(relevantNodes, new Comparator<Node>() {
		    @Override
		    public int compare(Node node1, Node node2) {
		        return ((Integer) node1.getTotalCosts()).compareTo(node2.getTotalCosts());
		    }
		});
	}
	
	/**
	 * Calculate the distance between two given nodes
	 * 
	 * @param node1 Start Node
	 * @param node2 End Node
	 * @return int Distance between the two given nodes
	 */
	private int getDistance(Node node1, Node node2) {
		int vertDiff = Math.abs(node1.getVertIndex() - node2.getVertIndex());
		int horDiff = Math.abs(node1.getHorIndex() - node2.getHorIndex());
		int diagonales = Math.min(vertDiff, horDiff);
		int straightLines = Math.max(vertDiff, horDiff) - diagonales;
		
		return diagonales * 14 + straightLines * 10;
	}
}
