package util;

import API.Models.Node;
import API.Models.NodeType;

/**
 * Utility Class that contains methods for generating and handling Node[][]
 * (fields) and nodes
 *
 * @author Finn
 */
public class Util {

    /**
     * Iterates over all nodes of a given field and prints them
     *
     * @param field Node[][] that is supposed to be printed
     */
    public static void printField(Node[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                Node node = field[i][j];
                System.out.print(node);
            }
            System.out.println("");
        }
        System.out.println();
    }

    /**
     * Static method that creates a random Node[][] (field) based on a given width
     * and height
     *
     * @param width  Amount of columns in the field
     * @param height Amount of rows in the field
     * @return Node[][] Randomly generated field
     */
    public static Node[][] generateField(int width, int height) {
        Node[][] field = new Node[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Node node = new Node(i, j);

                if (Math.random() < 0.3) {
                    node.setType(NodeType.BLOCKED);
                }

                field[i][j] = node;
            }
        }

        placeStartAndEndNode(field);

        return field;
    }

    /**
     * Static method that creates a random Node[][] (field) based on a given
     * dimension, which means that width = height
     *
     * @param dimension Amount of rows and columns in the field
     * @return Node[][] Randomly generated field
     */
    public static Node[][] generateField(int dimension) {
        return generateField(dimension, dimension);
    }

    /**
     * Static method for placing a start and an end node into a given field
     *
     * @param field Node[][] in which the start and end Nodes are supposed to be
     *              placed in
     */
    public static void placeStartAndEndNode(Node[][] field) {
        field[0][0].setType(NodeType.START);
        field[field.length - 1][field[0].length - 1].setType(NodeType.END);
    }

    /**
     * Returns a random Node from a given Node[][]
     *
     * @param field Node[][] containing all nodes
     * @return Node Random Node from the given Node[][]
     */
    public static Node getRandomNode(Node[][] field) {
        return field[(int) (Math.random() * field.length)][(int) (Math.random() * field[0].length)];
    }

    /**
     * Calculate the distance between two given nodes
     *
     * @param node1 Start Node
     * @param node2 End Node
     * @return int Distance between the two given nodes
     */
    public static int getDistance(Node node1, Node node2) {
        int vertDiff = Math.abs(node1.getVertIndex() - node2.getVertIndex());
        int horDiff = Math.abs(node1.getHorIndex() - node2.getHorIndex());
        int diagonales = Math.min(vertDiff, horDiff);
        int straightLines = Math.max(vertDiff, horDiff) - diagonales;

        return diagonales * 14 + straightLines * 10;
    }
}
