package MockBackend;

import API.APIManager;
import API.Interfaces.IBackend;
import API.Models.Node;

public class MockBackend implements IBackend {

    private APIManager api;
    private Node[][] matrix;

    public MockBackend(APIManager api) {
        this.api = api;
    }

    public void testStart() {
        int[] data = {1,3,4};
        sendToFrontEnd(0,0, data);
    }

    public void sendToFrontEnd(int row, int column, int[] val) {
        api.sendToFrontend(row, column, val);
    }

    @Override
    public void receive(Node[][] matrix) {
        System.out.format("[API - Backend] Neue Matrix mit den Parametern");
        this.matrix = matrix;
        testStart();
    }
}
