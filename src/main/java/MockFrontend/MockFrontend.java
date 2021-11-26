package MockFrontend;

import API.APIManager;
import API.Interfaces.IFrontend;

public class MockFrontend implements IFrontend {

    private APIManager api;
    private int[][] matrix;

    public MockFrontend(APIManager api) {
        this.api = api;
    }

    public void testStart() {
        api.initMatrix(15, 15);
        this.matrix = api.getMatrix();
        api.sendToBackend();
    }

    public static void printm(int[][] matrix) {
        System.out.println("<><><><><><><><><><><>");
        for(int i = 0; i < 15; i++) {
            for(int x = 0; x < 15; x++) {
                System.out.print(" " + matrix[i][x]);
            }
            System.out.println("");
        }
    }

    @Override
    public void update(int[][] data) {
        System.out.println("[API - Frontend] Update angekommen");
        this.matrix = data;
        printm(this.matrix);
    }
}
