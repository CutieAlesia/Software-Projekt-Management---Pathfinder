package MockFrontend;

import API.APIManager;
import API.Models.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MockFrontendTest {

    private APIManager api;
    private Node[][] matrix = new Node[10][10];

    @Before
    public void setUp() {
        api = new APIManager();
        matrix[0][0] = new Node(0,0, new int[]{1,3});
    }

    @Test
    public void updateTest() {
        Node obj = new Node(0,0, new int[]{1,3});
        Assert.assertEquals(matrix[0][0].toString(), obj.toString());
    }
}