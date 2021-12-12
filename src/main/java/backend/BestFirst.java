package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import API.APIManager;
import API.Models.Node;
import API.Models.NodeType;
import util.Util;

/**
 * Best First Algorithm class
 *
 * @author Finn
 */
public class BestFirst extends SearchAlgorithm {

    private ArrayList<Node> relevantNodes;

    /**
     * Initializes all fields
     *
     * @param field
     */
    public BestFirst(APIManager manager) {
        super(manager);
        this.relevantNodes = new ArrayList<Node>();
    }

    /** Starts the recursive algorithm and runs until a path is found or there is no valid path */
    @Override
    public void run() {
        findLocations();
        boolean found = updateNeighbours(this.start);
        if (found) {
            Node node = end;

            while (node != null) {
                if (node.getType() != NodeType.END && node.getType() != NodeType.START) {
                    node.setType(NodeType.PATH);

                    // update frontend
                    manager.sendToFrontend(node);
                }

                node = node.getPrev();
            }
        } else {
            System.out.println("Es wurde leider kein Weg gefunden!");
        }
    }

    /**
     * Takes a node, updates all direct neighbours for that node and recursively calls itself if
     * there are unvisited nodes in the relevants node list left
     *
     * @param node Node which neighbours are supposed to be updated
     * @return boolean that describes whether a valid path was found or not
     */
    private boolean updateNeighbours(Node node) {
        boolean found = false;

        // Coordinates of the neighbours that are supposed to be updated
        int[][] coords = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

        // Check if the given node is the start or end node
        if (node.getType() != NodeType.START && node.getType() != NodeType.END) {
            // mark the given node as visited
            node.setType(NodeType.VISITED);

            // update frontend
            manager.sendToFrontend(node);
        }

        // remove the given node from the relevants node list so the algorithm
        // doesn't get stuck in an infinite loop
        if (this.relevantNodes.contains(node)) {
            this.relevantNodes.remove(node);
        }

        // Iterate over all neighbour coordinates
        for (int[] coord : coords) {
            int i = coord[0];
            int j = coord[1];

            boolean leftInBound = node.getHorIndex() + j >= 0;
            boolean rightInBound = node.getHorIndex() + j < field[0].length;
            boolean topInBound = node.getVertIndex() + i >= 0;
            boolean bottomInBound = node.getVertIndex() + i < field.length;

            // Check if the neighbour coordinates are still in bound
            if (leftInBound && rightInBound && topInBound && bottomInBound) {

                Node neighbour = field[node.getVertIndex() + i][node.getHorIndex() + j];
                int estimatedCosts = Util.getDistance(neighbour, end);

                // skip the current neighbour if the node is blocked, already visited or the
                // start node
                if (neighbour.getType() == NodeType.START
                        || neighbour.getType() == NodeType.BLOCKED
                        || neighbour.getType() == NodeType.VISITED) {
                    continue;
                }

                // update the neighbours estimated costs if the neighbour hasn't been calculated
                // yet or the new estimated costs are cheaper
                if (estimatedCosts < neighbour.getEstimatedCosts()
                        || neighbour.getEstimatedCosts() == 0) {
                    neighbour.setEstimatedCosts(estimatedCosts);
                    neighbour.setPrev(node);

                    // indicate that the end node was found
                    if (neighbour.getType() == NodeType.END) {
                        found = true;
                        System.out.println("Ziel gefunden!");
                    }
                }

                // if not already done, add neighbour to the relevant nodes list
                if (!relevantNodes.contains(neighbour)) {
                    relevantNodes.add(neighbour);
                }
            }
        }

        Util.printField(field);
        Node nextToUpdate = getNextNode();
        // returns true if the end node was found, false if there is no valid path or
        // recursively calls itself if there are still nodes to check
        return found ? true : nextToUpdate == null ? false : updateNeighbours(nextToUpdate);
    }

    /**
     * returns the cheapest relevant node, based on the estimated costs
     *
     * @return Node Currently cheapest relevant node
     */
    private Node getNextNode() {
        if (this.relevantNodes.size() == 0) {
            return null;
        }

        sortRelevantNodes();
        return this.relevantNodes.get(0);
    }

    /** sort all relevant nodes by their estimated costs */
    private void sortRelevantNodes() {
        Collections.sort(
                relevantNodes,
                new Comparator<Node>() {
                    @Override
                    public int compare(Node node1, Node node2) {
                        return ((Integer) node1.getEstimatedCosts())
                                .compareTo(node2.getEstimatedCosts());
                    }
                });
    }
}
