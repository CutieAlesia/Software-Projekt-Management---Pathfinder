import API.APIManager;
import API.Interfaces.IFrontend;
import MockBackend.MockBackend;
import MockFrontend.MockFrontend;

import java.util.Scanner;

public class MockMain {

    private static APIManager api;

    public static void main(String[] args) {
        // Init
        api = new APIManager();
        MockFrontend mockFrontend = new MockFrontend(api);
        MockBackend mockBackend = new MockBackend(api);

        // adding Interface listeners
        api.attachFrontend(mockFrontend);
        api.attachBackend(mockBackend);

        // Example Matrix | Can be called from the Frontend
        api.initMatrix(10, 10);

        // Backend -> Frontend | Single changes in loop
        mockBackend.test();
    }

}
