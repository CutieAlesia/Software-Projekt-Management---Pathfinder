package API;

import API.Interfaces.IBackend;
import API.Interfaces.IFrontend;
import API.Models.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dubsky
 * @version 1.3
 */
public class APIManager {

    private Node node;
    private List<IFrontend> frontends = new ArrayList<>();
    private List<IBackend> backends = new ArrayList<>();

    public APIManager() {}

    /** Sends a node update to all attached frontends
     *
     * @param node The updated node
     */
    public void sendToFrontend(Node node) {
        this.node = node;
        System.out.println("[API] Transmitting data to the frontend");
        for (IFrontend endpoint : frontends) {
            endpoint.update(this.node);
        }
    }

    /** Sends a matrix structure to all backends
     *
     * @param matrix A two-dimensional node array to simulate a grid structure
     */
    public void sendToBackend(Node[][] matrix) {
        System.out.println("[API] Transmitting data to the backend");
        for (IBackend endpoint : backends) {
            endpoint.receive(matrix);
        }
    }

    /** Service layer between getting a new matrix and sending it to the backend
     *
     * @param matrix A two-dimensional node array to simulate a grid structure
     */
    public void initMatrix(Node[][] matrix) {
        System.out.println("[API] Initializing a new Matrix");
        sendToBackend(matrix);
    }

    /** Attaches a frontend to the managed list
     *
     * @param frontend Class with the IFrontend interface
     */
    public void attachFrontend(IFrontend frontend) {
        frontends.add(frontend);
        System.out.println("[API] Frontend added");
    }

    /** Attaches a backend to the managed list
     *
     * @param backend Class with the IBackend interface
     */
    public void attachBackend(IBackend backend) {
        backends.add(backend);
        System.out.println("[API] Backend added");
    }
}
