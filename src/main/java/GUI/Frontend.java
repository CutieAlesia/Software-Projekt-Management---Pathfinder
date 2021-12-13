package GUI;

import API.APIManager;
import API.Interfaces.IFrontend;
import API.Models.Node;
import GUI.Generator.DepthFirst;
import GUI.Generator.LabyrinthGenerator;

public class Frontend implements IFrontend {

    public static final String ANSI_RESET = "\u001B[0m";
    // Declaring the background color
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";

    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    private APIManager api;
    private Node[][] matrix;

    public Node[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Node[][] matrix) {
        this.matrix = matrix;
    }

    public Frontend(APIManager api) {
        this.api = api;
    }

    public void testStart() {
        generateLabyrinth(new DepthFirst(), 60, 30);
        api.initMatrix(matrix);
    }

    @Override
    public void update(Node node) {

        int x = node.getHorIndex();
        int y = node.getVertIndex();

        matrix[y][x] = node;

        System.out.println("[API - Frontend] Node update received");
        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; i < matrix[0].length; i++) {

                switch (matrix[i][j].getType()) {
                case BLOCKED:
                    System.out.print(ANSI_BLACK_BACKGROUND + "  " + ANSI_RESET);
                    break;
                case END:
                    System.out.print(ANSI_RED_BACKGROUND + "  " + ANSI_RESET);
                    break;
                case NORMAL:
                    System.out.print("  ");
                    break;
                case PATH:
                    System.out.print(ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET);
                    break;
                case START:
                    System.out.print(ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET);
                    break;
                case VISITED:
                    System.out.print(ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET);
                    break;
                default:
                    break;
                }
            }
            System.out.println();
        }
    }

    public void generateLabyrinth(LabyrinthGenerator labyrinthGenerator, int x, int y) {
        matrix = labyrinthGenerator.generateLabyrinth(y, x);
    }
}
