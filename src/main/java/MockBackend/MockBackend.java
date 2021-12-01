package MockBackend;

import API.APIManager;
import API.Interfaces.IBackend;
import API.Models.Node;
import API.Models.NodeType;

/**
 * @author Dubsky
 * @version 1.2
 */
public class MockBackend implements IBackend {

    private APIManager api;
    private Node[][] matrix;

    public MockBackend(APIManager api) {
        this.api = api;
    }

    public void testStart() {
        Node testNode = new Node(1, 1);
        testNode.setCosts(5);
        testNode.setType(NodeType.PATH);
        sendToFrontEnd(testNode);
    }

    public void sendToFrontEnd(Node node) {
        api.sendToFrontend(node);
    }

    @Override
    public void receive(Node[][] matrix) {
        System.out.format("[API - Backend] Neue Matrix mit den Parametern");
        this.matrix = matrix;
        testStart();
    }
}
