package API.Models;

import java.util.Arrays;

public class Node {

    private int x;
    private int y;
    private int[] data;
    private boolean isBlocked = false;


    public Node(int x, int y, int[] data, boolean isBlocked) {
        this.x = x;
        this.y = y;
        this.data = data;
        this.isBlocked = isBlocked;
    }

    public Node(int x, int y, int[] data) {
        this.x = x;
        this.y = y;
        this.data = data;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", data=" + Arrays.toString(data) +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
