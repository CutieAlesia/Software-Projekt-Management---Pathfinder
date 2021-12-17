package MockFrontend;

import API.APIManager;
import API.Interfaces.IFrontend;
import API.Models.Node;

/**
 * @author Dubsky
 * @version 1.4
 */
public class MockFrontend implements IFrontend {

    private APIManager api;
    private Node[][] matrix;

    public MockFrontend(APIManager api) {
        this.api = api;
    }

    public void testStart() {
        this.matrix = new Node[15][15];
        api.initMatrix(matrix);
    }

    @Override
    public void update(Node node) {
        System.out.println("[API - Frontend] Node update received");
        this.matrix[node.getHorIndex()][node.getVertIndex()] = node;
        System.out.println(node);
    }
}
