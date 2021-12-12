package backend;

import API.APIManager;
import API.Interfaces.IBackend;
import API.Models.Node;
import API.Models.NodeType;

/**
 * Base Class for all search algorithms
 *
 * @author Finn
 */
public abstract class SearchAlgorithm implements IBackend {

    protected Node[][] field;
    protected Node start;
    protected Node end;
    protected APIManager manager;

    /**
     * Initializes the field and finds the start and end node
     *
     * @param field Node[][] that the algorithm is supposed to work with
     */
    public SearchAlgorithm(APIManager manager) {
        this.manager = manager;
    }

    public Node[][] getField() {
        return this.field;
    }

    /** Iterates over the field to find the start and end node */
    protected void findLocations() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (start != null && end != null) {
                    return;
                }

                if (field[i][j].getType() == NodeType.START) {
                    start = field[i][j];
                }

                if (field[i][j].getType() == NodeType.END) {
                    end = field[i][j];
                }
            }
        }
    }

    /** abstract method for starting an algorithm */
    public abstract void run();

    /**
     * receive a matrix from the API manager
     *
     * @param Node[][] matrix that represents the labyrinth
     */
    @Override
    public void receive(Node[][] matrix) {
        this.field = matrix;
    }
}
