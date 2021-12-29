package GUI.Generator;

import API.Models.Node;
import API.Models.NodeType;

public class RecursiveDivision implements LabyrinthGenerator{

    private Node[][] matrix;

    @Override
    public Node[][] generateLabyrinth(int x, int y) {
        // return null if the dimensions are too small
        if(x == 0 || y == 0) return null;

        initMatrix(x, y);
        generateWalls(x, y);

        return matrix;
    }

    /** Initializes the matrix by placing normal nodes as well as the start and end node */
    private void initMatrix(int x, int y) {
        // create the matrix
        this.matrix = new Node[x][y];

        // place normal nodes at every matrix index
        for(int i = 0; i < y; i++) {
            for(int j = 0; j < x; j++) {
                Node node = new Node(j, i);
                node.setType(NodeType.NORMAL);
                matrix[j][i] = node;
            }
        }

        // set start and end node
        matrix[0][0].setType(NodeType.START);
        matrix[x - 1][y - 1].setType(NodeType.END);
    }

    private void generateWalls(int x, int y) {
        int startY = randomNumber(1, y - 2);
        drawWallToRight(-1, startY);
        System.out.println("=================");
    }

    private void drawWallToTop(int x, int y) {
        int spaces = 0;

        for(int i = y - 1; i >= 0; i--) {
            Node node = matrix[x][i];
            NodeType type = node.getType();

            if(type == NodeType.BLOCKED || type == NodeType.END || type == NodeType.START)
                break;

            spaces++;
        }

        System.out.println("Top Spaces: " + spaces);

        if(spaces >= 3) {
            int idx = y - spaces;

            if(spaces == 2)
                idx = y - 1;

            if(spaces >= 5)
                randomNumber(y - 1, y - spaces);

            for(int i = y - 1; i >= y - spaces; i--) {
                Node node = matrix[x][i];

                if(i != idx) {
                    node.setType(NodeType.BLOCKED);
                }
            }

            int randomLeftIdx = randomIdx(y - 2, y - spaces, idx);
            int randomRightIdx = randomIdx(y - 2, y - spaces, idx);

            drawWallToRight(x, randomRightIdx);
            drawWallToLeft(x, randomLeftIdx);
        }
    }

    private void drawWallToBottom(int x, int y) {
        int spaces = 0;

        for(int i = y + 1; i<matrix[0].length; i++) {
            Node node = matrix[x][i];
            NodeType type = node.getType();

            if(type == NodeType.BLOCKED || type == NodeType.END || type == NodeType.START) {

                break;
            }

            spaces++;
        }

        System.out.println("Bottom Spaces: " + spaces);

        if(spaces >= 3) {
            int idx = y + spaces;

            if(spaces == 2)
                idx = y + 1;

            if(spaces >= 5)
                randomNumber(y + 1, y + spaces);

            for(int i = y + 1; i <= y + spaces; i++) {
                Node node = matrix[x][i];

                if(i != idx) {
                    node.setType(NodeType.BLOCKED);
                }
            }

            int randomLeftIdx = randomIdx(y + 2, y + spaces, idx);
            int randomRightIdx = randomIdx(y + 2, y + spaces, idx);

            drawWallToRight(x, randomRightIdx);
            drawWallToLeft(x, randomLeftIdx);
        }
    }

    private void drawWallToLeft(int x, int y) {
        int spaces = 0;

        for(int i = x - 1; i >= 0; i--) {
            Node node = matrix[i][y];
            NodeType type = node.getType();

            if(type == NodeType.BLOCKED || type == NodeType.END || type == NodeType.START)
                break;

            spaces++;
        }

        System.out.println("Left Spaces: " + spaces);

        if(spaces >= 3) {
            int idx = x - spaces;

            if(spaces == 2)
                idx = x - 1;

            if(spaces >= 5)
                randomNumber(x - 1, x - spaces);

            for(int i = x - 1; i >= x - spaces; i--) {
                Node node = matrix[i][y];

                if(i != idx) {
                    node.setType(NodeType.BLOCKED);
                }
            }

            int randomBottomIdx = randomIdx(x - 2, x - spaces, idx);
            int randomTopIdx = randomIdx(x - 2, x - spaces, idx);

            drawWallToBottom(randomBottomIdx, y);
            drawWallToTop(randomTopIdx, y);
        }
    }

    private void drawWallToRight(int x, int y) {
        int spaces = 0;

        for(int i = x + 1; i<matrix.length; i++) {
            Node node = matrix[i][y];
            NodeType type = node.getType();

            if(type == NodeType.BLOCKED || type == NodeType.END || type == NodeType.START)
                break;

            spaces++;
        }

        System.out.println("Right Spaces: " + spaces);

        if(spaces >= 3) {
            int idx = x + spaces;

            if(spaces == 2)
                idx = x + 1;

            if(idx >= 5)
                randomNumber(x + 1, x + spaces);

            for(int i = x + 1; i <= x + spaces; i++) {
                Node node = matrix[i][y];

                if(i != idx) {
                    node.setType(NodeType.BLOCKED);
                }
            }

            int randomBottomIdx = randomIdx(x + 2, x + spaces, idx);
            int randomTopIdx = randomIdx(x + 2, x + spaces, idx);

            drawWallToBottom(randomBottomIdx, y);
            drawWallToTop(randomTopIdx, y);
        }
    }

    private int randomIdx(int min, int max, int forbidden) {
        int idx = randomNumber(min, max);

        while(idx == forbidden) {
            idx = randomNumber(min, max);
        }

        return idx;
    }

    private int randomNumber(int num1, int num2) {
        int min = Math.min(num1, num2);
        int max = Math.max(num1, num2);
        return min + (int) (Math.random() * (max - min + 1));
    }
}
