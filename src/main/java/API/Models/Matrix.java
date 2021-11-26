package API.Models;

public class Matrix {

    private int[][] data;

    public Matrix(int x, int y) {
        this.data = new int[x][y];
    }

    public int[][] getData() {
        return data;
    }

    public void setData(int x, int y) {
        this.data = new int[x][y];
    }

    public void setChanges(int x, int y, int val) {
        this.data[x][y] = val;
    }
}
