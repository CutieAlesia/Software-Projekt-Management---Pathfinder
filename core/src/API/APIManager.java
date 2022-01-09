package API;

import java.util.ArrayList;
import java.util.List;

import API.Interfaces.IBackend;
import API.Interfaces.IFrontend;
import API.Models.Node;

/**
 * @author Dubsky
 * @version 1.3
 */
public class APIManager {

    private Node node;
    private String activeAlgorithm;
    private List<IFrontend> frontends = new ArrayList<>();
    private IBackend backend;

    public APIManager() {
        activeAlgorithm = null;
    }

    /**
     * Sends a node update to all attached frontends
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

    /**
     * Sends a matrix structure to all backends
     *
     * @param matrix A two-dimensional node array to simulate a grid structure
     */
    public void sendToBackend(Node[][] matrix) {
        System.out.println("[API] Transmitting data to the backend");
        backend.receive(matrix);
    }

    /**
     * Service layer between getting a new matrix and sending it to the backend
     *
     * @param matrix A two-dimensional node array to simulate a grid structure
     */
    public void initMatrix(Node[][] matrix) {
        System.out.println("[API] Initializing a new Matrix");
        sendToBackend(matrix);
    }

    /**
     * Attaches a frontend to the managed list
     *
     * @param frontend Class with the IFrontend interface
     */
    public void attachFrontend(IFrontend frontend) {
        frontends.add(frontend);
        System.out.println("[API] Frontend added");
    }

    /**
     * Attaches a backend to the managed list
     *
     * @param backend Class with the IBackend interface
     */
    public void attachBackend(IBackend backend) {
        this.backend = backend;
        System.out.println("[API] Backend added");
        System.out.format("Active Algorithm: %s\n", backend.getClass().getSimpleName());
    }

    /**
     * Detaches a algorithm backend from the manager
     *
     * @param backend Backend algorithm
     */
    public void detachBackend(IBackend backend) {
        this.backend = null;
        System.out.println("[API] Backend detached");
        System.out.format("Active Algorithm: %s\n", backend.getClass().getSimpleName());
    }

    public String getActiveAlgorithm() {
        return activeAlgorithm;
    }

    public void setActiveAlgorithm(String activeAlgorithm) {
        this.activeAlgorithm = activeAlgorithm;
    }
}
