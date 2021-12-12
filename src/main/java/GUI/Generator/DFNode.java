package GUI.Generator;


public class DFNode {

    private int x;
    private int y;
    private int[] data;
    private boolean visited = false;
    private boolean isBlocked = false;

    public DFNode(int x, int y, int[] data, boolean isBlocked) {
        this.x = x;
        this.y = y;
        this.data = data;
        this.isBlocked = isBlocked;
    }

    public DFNode(int x, int y, int[] data) {
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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean b) {
        visited = b;
    }

    @Override
    public String toString() {
        return "Node{" + "x=" + x + ", y=" + y + ", data=" + ", isBlocked=" + isBlocked + '}';
    }
}
