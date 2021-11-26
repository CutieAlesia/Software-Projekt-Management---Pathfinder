package MockBackend;

import API.APIManager;
import API.Interfaces.IBackend;

import java.util.Scanner;

public class MockBackend implements IBackend {

    private APIManager api;

    public MockBackend(APIManager api) {
        this.api = api;
    }

    public void test() {
        //--- TEST ---//
        Scanner scanner = new Scanner(System.in);
        int x, y, z;
        while(true) {
            System.out.println("Row >> ");
            x = scanner.nextInt();
            System.out.println("Column >> ");
            y = scanner.nextInt();
            System.out.println("Value >> ");
            z = scanner.nextInt();
            sendToFrontEnd(x, y, z);
        }
    }

    public void sendToFrontEnd(int row, int column, int val) {
        api.sendToFrontend(row, column, val);
    }

    @Override
    public void receive(int[][] matrix) {
        System.out.println("[API - Backend] Matrix angekommen");
        //Hier kommt eine komplett neue Matrix
    }
}
