package GUI.Generator;

import API.Models.Node;
import API.Models.NodeType;

/**
 * Recursive Division maze generation algorithm
 *
 * @author backend
 */
public class RecursiveDivision implements LabyrinthGenerator{

    // The maze itself
    private Node[][] matrix;
    // Value for calculating the buffer for new wall indices
    private static int DIVIDER = 4;
    // Boolean that determines whether shorter or longer paths shall be generated
    private static boolean SHORTER_PATHS = true;

    /**
     * entry point for generating a new maze
     *
     * @return generated maze, with all necessary node types
     * @param x width of the maze
     * @param y height of the maze
     *
     * */
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

    /**
     * Entry point for generating the maze walls
     *
     * @param x coordinate of the first wall
     * @param y coordinate of the first wall
     */
    private void generateWalls(int x, int y) {
        int startY = randomIdx(1, y - 2, -1);
        drawWallToRight(-1, startY);
        System.out.println("=================");
    }

    /**
     * Draws a new wall from a given index to the top
     *
     * @param x Start value of the new wall
     * @param y Start value of the new wall
     *
     * Runs until a wall, the end node, the start node or a passage is found.
     * Then a new wall is drawn with a randomly generated passage index.
     * After the work is done it recursively calls drawWallToLeft and drawWallToRight to generate
     * new walls with the previously generated wall as their origin
     */
    private void drawWallToTop(int x, int y) {
        int spaces = 0;
        boolean lastIsPassage = false;

        for(int i = y - 1; i >= 0; i--) {
            Node node = matrix[x][i];
            NodeType type = node.getType();

            boolean leftIsWall = x - 1 >= 0 && matrix[x - 1][i].getType() == NodeType.BLOCKED;
            boolean rightIsWall = x + 1 < matrix.length && matrix[x + 1][i].getType() == NodeType.BLOCKED;

            if(leftIsWall || rightIsWall) {
                lastIsPassage = true;
                break;
            }

            if(type == NodeType.BLOCKED || type == NodeType.END || type == NodeType.START || leftIsWall || rightIsWall)
                break;

            spaces++;
        }

        if(spaces >= 2) {
            int idx = y - spaces;

            if(spaces == 2)
                idx = y - 1;

            if(spaces >= 5)
                idx = randomNumber(y - 1, y - spaces);

            if(lastIsPassage)
                idx = y - spaces;

            for(int i = y - 1; i >= y - spaces; i--) {
                Node node = matrix[x][i];

                if(i != idx) {
                    node.setType(NodeType.BLOCKED);
                }
            }

            int randomLeftIdx = randomIdx(y - 2, y - spaces, idx);
            int randomRightIdx = randomIdx(y - 2, y - spaces, idx);

            if(spaces >= 9 && SHORTER_PATHS) {
                int center = (int) ((y - 1 + y - spaces) / 2);
                int puffer = (int) (spaces / DIVIDER);
                randomLeftIdx = randomIdx(center - puffer, center + puffer, idx);
                randomRightIdx = randomIdx(center - puffer, center + puffer, idx);
            }

            drawWallToRight(x, randomRightIdx);
            drawWallToLeft(x, randomLeftIdx);
        }
    }

    /**
     * Draws a new wall from a given index to the bottom
     *
     * @param x Start value of the new wall
     * @param y Start value of the new wall
     *
     * Runs until a wall, the end node, the start node or a passage is found.
     * Then a new wall is drawn with a randomly generated passage index.
     * After the work is done it recursively calls drawWallToLeft and drawWallToRight to generate
     * new walls with the previously generated wall as their origin
     */
    private void drawWallToBottom(int x, int y) {
        int spaces = 0;
        boolean lastIsPassage = false;

        for(int i = y + 1; i<matrix[0].length; i++) {
            Node node = matrix[x][i];
            NodeType type = node.getType();

            boolean leftIsWall = x - 1 >= 0 && matrix[x - 1][i].getType() == NodeType.BLOCKED;
            boolean rightIsWall = x + 1 < matrix.length && matrix[x + 1][i].getType() == NodeType.BLOCKED;

            if(leftIsWall || rightIsWall) {
                lastIsPassage = true;
                break;
            }

            if(type == NodeType.BLOCKED || type == NodeType.END || type == NodeType.START)
                break;

            spaces++;
        }

        if(spaces >= 2) {
            int idx = y + spaces;

            if(spaces == 2)
                idx = y + 1;

            if(spaces >= 5)
                idx = randomNumber(y + 1, y + spaces);

            if(lastIsPassage)
                idx = y + spaces;

            for(int i = y + 1; i <= y + spaces; i++) {
                Node node = matrix[x][i];

                if(i != idx) {
                    node.setType(NodeType.BLOCKED);
                }
            }

            int randomLeftIdx = randomIdx(y + 2, y + spaces, idx);
            int randomRightIdx = randomIdx(y + 2, y + spaces, idx);

            if(spaces >= 9 && SHORTER_PATHS) {
                int center = (int) ((y + 1 + y + spaces) / 2);
                int puffer = (int) (spaces / DIVIDER);
                randomLeftIdx = randomIdx(center - puffer, center + puffer, idx);
                randomRightIdx = randomIdx(center - puffer, center + puffer, idx);
            }

            drawWallToRight(x, randomRightIdx);
            drawWallToLeft(x, randomLeftIdx);
        }
    }

    /**
     * Draws a new wall from a given index to the left
     *
     * @param x Start value of the new wall
     * @param y Start value of the new wall
     *
     * Runs until a wall, the end node, the start node or a passage is found.
     * Then a new wall is drawn with a randomly generated passage index.
     * After the work is done it recursively calls drawWallToTop and drawWallToBottom to generate
     * new walls with the previously generated wall as their origin
     */
    private void drawWallToLeft(int x, int y) {
        int spaces = 0;
        boolean lastIsPassage = false;

        for(int i = x - 1; i >= 0; i--) {
            Node node = matrix[i][y];
            NodeType type = node.getType();

            boolean leftIsWall = y - 1 >= 0 && matrix[i][y - 1].getType() == NodeType.BLOCKED;
            boolean rightIsWall = y + 1 < matrix[0].length && matrix[i][y + 1].getType() == NodeType.BLOCKED;

            if(leftIsWall || rightIsWall) {
                lastIsPassage = true;
                break;
            }

            if(type == NodeType.BLOCKED || type == NodeType.END || type == NodeType.START)
                break;

            spaces++;
        }

        if(spaces >= 2) {
            int idx = x - spaces;

            if(spaces == 2)
                idx = x - 1;

            if(spaces >= 5)
                idx = randomNumber(x - 1, x - spaces);

            if(lastIsPassage)
                idx = x - spaces;

            for(int i = x - 1; i >= x - spaces; i--) {
                Node node = matrix[i][y];

                if(i != idx) {
                    node.setType(NodeType.BLOCKED);
                }
            }

            int randomBottomIdx = randomIdx(x - 2, x - spaces, idx);
            int randomTopIdx = randomIdx(x - 2, x - spaces, idx);

            if(spaces >= 9 && SHORTER_PATHS) {
                int center = (int) ((x - 1 + x - spaces) / 2);
                int puffer = (int) (spaces / DIVIDER);
                randomBottomIdx = randomIdx(center - puffer, center + puffer, idx);
                randomTopIdx = randomIdx(center - puffer, center + puffer, idx);
            }

            drawWallToBottom(randomBottomIdx, y);
            drawWallToTop(randomTopIdx, y);
        }
    }

    /**
     * Draws a new wall from a given index to the right
     *
     * @param x Start value of the new wall
     * @param y Start value of the new wall
     *
     * Runs until a wall, the end node, the start node or a passage is found.
     * Then a new wall is drawn with a randomly generated passage index.
     * After the work is done it recursively calls drawWallToTop and drawWallToBottom to generate
     * new walls with the previously generated wall as their origin
     */
    private void drawWallToRight(int x, int y) {
        int spaces = 0;
        boolean lastIsPassage = false;

        for(int i = x + 1; i<matrix.length; i++) {
            Node node = matrix[i][y];
            NodeType type = node.getType();

            boolean leftIsWall = y - 1 >= 0 && matrix[i][y - 1].getType() == NodeType.BLOCKED;
            boolean rightIsWall = y + 1 < matrix[0].length && matrix[i][y + 1].getType() == NodeType.BLOCKED;

            if(leftIsWall || rightIsWall) {
                lastIsPassage = true;
                break;
            }

            if(type == NodeType.BLOCKED || type == NodeType.END || type == NodeType.START)
                break;

            spaces++;
        }

        if(spaces >= 2) {
            int idx = x + spaces;

            if(spaces == 2)
                idx = x + 1;

            if(spaces >= 5)
                idx = randomNumber(x + 1, x + spaces);

            if(lastIsPassage)
                idx = x + spaces;

            for(int i = x + 1; i <= x + spaces; i++) {
                Node node = matrix[i][y];

                if(i != idx) {
                    node.setType(NodeType.BLOCKED);
                }
            }

            int randomBottomIdx = randomIdx(x + 2, x + spaces, idx);
            int randomTopIdx = randomIdx(x + 2, x + spaces, idx);

            if(spaces >= 9 && SHORTER_PATHS) {
                int center = (int) ((x + 1 + x + spaces) / 2);
                int puffer = (int) (spaces / DIVIDER);
                randomBottomIdx = randomIdx(center - puffer, center + puffer, idx);
                randomTopIdx = randomIdx(center - puffer, center + puffer, idx);
            }

            drawWallToBottom(randomBottomIdx, y);
            drawWallToTop(randomTopIdx, y);
        }
    }

    /**
     * Randomly generates a new wall index, based on a min and max value and the generated passage value
     *
     * @param min Min value for the new index
     * @param max Max value for the new index
     * @param forbidden Index of the generated passage
     * @return the randomly generated index
     */
    private int randomIdx(int min, int max, int forbidden) {
        int idx = randomNumber(min, max);

        boolean prettyMaze = matrix.length % 2 != 0 && matrix[0].length % 2 != 0;

        while(idx == forbidden || (prettyMaze && idx % 2 != 0)) {
            idx = randomNumber(min, max);
        }

        return idx;
    }

    /**
     * Generates a random number, based on a given min and max value
     *
     * @param num1 minValue
     * @param num2 maxValue
     * @return randomly generated number
     */
    private int randomNumber(int num1, int num2) {
        int min = Math.min(num1, num2);
        int max = Math.max(num1, num2);
        return min + (int) (Math.random() * (max - min + 1));
    }
}
