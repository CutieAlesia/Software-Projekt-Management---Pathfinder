package Models;

import API.Models.Node;
import org.junit.Assert;
import org.junit.Before;

import java.util.Arrays;

public class NodeTest {

    private Node node = new Node(5, 5, new int[]{1, 4, 5}, false);

    @org.junit.Test
    public void getXTest() {
        Assert.assertEquals(node.getX(), 5);
    }

    @org.junit.Test
    public void getYTest() {
        Assert.assertEquals(node.getY(), 5);
    }

    @org.junit.Test
    public void getDataTest() {
        Assert.assertEquals(Arrays.toString(node.getData()), Arrays.toString(new int[]{1, 4, 5}));
    }

    @org.junit.Test
    public void getBockedNodeTest() {
        Assert.assertFalse(node.isBlocked());
    }

    @org.junit.Test
    public void setBockedNodeTest() {
        Assert.assertFalse(node.isBlocked());
        node.setBlocked(true);
        Assert.assertTrue(node.isBlocked());
    }

    @org.junit.Test
    public void setData() {
        Assert.assertEquals(Arrays.toString(node.getData()), Arrays.toString(new int[]{1, 4, 5}));
        node.setData(new int[]{1,5,6,7});
        Assert.assertNotEquals(Arrays.toString(node.getData()), Arrays.toString(new int[]{1, 4, 5}));
        Assert.assertEquals(Arrays.toString(node.getData()), Arrays.toString(new int[]{1,5,6,7}));
    }

}