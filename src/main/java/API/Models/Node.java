package API.Models;

/**
 * @author Dubsky
 * @version 1.2
 */
public class Node {

    private int x;
    private int y;
    private int costs;
    private int estimatedCosts;
    private NodeType type;
    private Node prev;


    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCosts() {
        return costs;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }

    public int getEstimatedCosts() {
        return estimatedCosts;
    }

    public void setEstimatedCosts(int estimatedCosts) {
        this.estimatedCosts = estimatedCosts;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", costs=" + costs +
                ", estimatedCosts=" + estimatedCosts +
                ", type=" + type +
                ", prev=" + prev +
                '}';
    }
}
