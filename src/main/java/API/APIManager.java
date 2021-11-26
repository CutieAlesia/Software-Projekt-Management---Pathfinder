package API;

import API.Interfaces.IBackend;
import API.Interfaces.IFrontend;
import API.Models.Matrix;

import java.util.ArrayList;
import java.util.List;

public class APIManager {

    private Matrix matrix;
    private List<IFrontend> frontends = new ArrayList<>() {
    };
    private List<IBackend> backends = new ArrayList<>() {
    };

    public APIManager() {
        this.matrix = new Matrix(0, 0);
    }

    public void sendToFrontend(int row, int column, int val) {
        System.out.println("[API] Mirroring matrix");
        this.matrix.setChanges(row, column, val);
        System.out.println("[API] Transmitting data to the frontend");
        for (IFrontend endpoint : frontends) {
            endpoint.update(this.matrix.getData());
        }
    }

    public void sendToBackend() {
        System.out.println("[API] Transmitting data to the backend");
        for (IBackend endpoint : backends) {
            endpoint.receive(this.matrix.getData());
        }
    }

    public void initMatrix(int x, int y) {
        System.out.println("[API] Initializing a new Matrix");
        matrix.setData(x, y);
    }

    public int[][] getMatrix() {
        return matrix.getData();
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