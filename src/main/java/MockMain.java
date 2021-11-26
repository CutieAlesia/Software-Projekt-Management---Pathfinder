import API.APIManager;
import MockBackend.MockBackend;
import MockFrontend.MockFrontend;

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

        mockFrontend.testStart();
    }
    
}
