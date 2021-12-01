package Models;

import API.Models.Node;
import API.Models.NodeType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class NodeTest {

    private final Node node = new Node(5, 5);

    @Before
    public void setup() {
        this.node.setCosts(5);
        this.node.setType(NodeType.PATH);
        this.node.setEstimatedCosts(20);
    }

    @After
    public void finish() {

    }


    @org.junit.Test
    public void getXTest() {
        Assert.assertEquals(node.getX(), 5);
    }

    @org.junit.Test
    public void getYTest() {
        Assert.assertEquals(node.getY(), 5);
    }

    @org.junit.Test
    public void getCostTest() {
        Assert.assertEquals(node.getCosts(), 5);
    }

    @org.junit.Test
    public void getEstimatedTest() {
        Assert.assertEquals(node.getEstimatedCosts(), 20);
    }

    @org.junit.Test
    public void getTypeTest() {
        Assert.assertEquals(node.getType(), NodeType.PATH);
    }

}