package MockFrontend;

import API.APIManager;
import API.Interfaces.IFrontend;

public class MockFrontend implements IFrontend {

    private APIManager api;
    private int[][] matrix = new int[10][10];

    public MockFrontend(APIManager api) {
        this.api = api;
        printm(matrix);
    }

    public static void printm(int[][] matrix) {
        System.out.println("Frontend Matrix");
        System.out.println("<><><><><><><><><><><>");
        for(int i = 0; i < matrix[0].length; i++) {
            for(int x = 0; x < 10; x++) {
                System.out.print(" " + Integer.toString(matrix[i][x]));
            }
            System.out.println("");
        }
    }

    @Override
    public void update(int[][] data) {
        this.matrix = data;
        printm(this.matrix);
    }
}
