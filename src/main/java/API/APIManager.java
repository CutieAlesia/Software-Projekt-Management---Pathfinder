package API;

import API.Interfaces.IBackend;
import API.Interfaces.IFrontend;
import API.Models.Node;

import java.util.ArrayList;
import java.util.List;

public class APIManager {

    private Node node;
    private List<IFrontend> frontends = new ArrayList<>();
    private List<IBackend> backends = new ArrayList<>();

    public APIManager() {}

    public void sendToFrontend(int row, int column, int[] val) {
        this.node = new Node(row, column, val);
        System.out.println("[API] Transmitting data to the frontend");
        for (IFrontend endpoint : frontends) {
            endpoint.update(this.node);
        }
    }

    public void sendToBackend(Node[][] matrix) {
        System.out.println("[API] Transmitting data to the backend");
        for (IBackend endpoint : backends) {
            endpoint.receive(matrix);
        }
    }

    public void initMatrix(Node[][] matrix) {
        System.out.println("[API] Initializing a new Matrix");
        sendToBackend(matrix);
    }

    public int[] getData() {
        return node.getData();
    }

    public void attachFrontend(IFrontend frontend) {
        frontends.add(frontend);
        System.out.println("[API] Frontend added");
    }

    public void attachBackend(IBackend backend) {
        backends.add(backend);
        System.out.println("[API] Backend added");
    }
}
