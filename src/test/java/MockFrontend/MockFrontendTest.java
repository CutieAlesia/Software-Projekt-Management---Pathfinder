package MockFrontend;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import API.APIManager;
import API.Models.Node;

public class MockFrontendTest {

    private APIManager api;
    private Node[][] matrix = new Node[10][10];

    @Before
    public void setUp() {
        api = new APIManager();
        matrix[0][0] = new Node(0, 0, new int[] {1, 3});
    }

    @Test
    public void updateTest() {
        Node obj = new Node(0, 0, new int[] {1, 3});
        Assert.assertEquals(matrix[0][0].toString(), obj.toString());
    }

    @Test
    public void randomTest() {
        Node obj = new Node(1, 1, new int[] {1, 1}, true);
        Assert.assertTrue(obj.isBlocked());
    }
}
