package com.mygdx.map;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import API.Models.Node;
import API.Models.NodeType;
import com.mygdx.pathfindergui.PFTimer;

/**
 * TileMap. Holds the data of the nodes and tiles that make up the labyrinth. Connects the nodes and
 * tiles logically. Responsible for drawing the Tiles.
 *
 * @author frontend
 */
public class TileMap extends Actor {

    // Textures for the tiles
    private Texture normal;
    private Texture start;
    private Texture end;
    private Texture path;
    private Texture visited;
    private Texture blocked;
    private int sizeX;
    private int sizeY;

    // margin around the Actor TileMap when placed into the stage
    private float margin = 50f;

    private float marginBottom = 10f;

    // dimensions of the tile .png (.png must be square)
    private int tileDimensions = 32;

    private int tileDimensionsSurfaceX = 32;
    private int tileDimensionsSurfaceY = 15;

    //
    private Node[][] nodes;

    //
    private Tile[][] tiles;

    // map will be filled with new tiles if true
    private boolean mapFillable;

    private int autoPlaySpeed = 2;

    //
    private LinkedList<Node> processedNodes;

    private PFTimer pfTimer;

    // true if the map has been edited. Necessary for resetting the counterLabels
    private boolean mapEdited;

    /**
     * Constructor. Generates a new Node matrix and sets the bounds of the map depending on the size
     * of the labyrinth.
     *
     * @param sizeX
     * @param sizeY
     */
    public TileMap(int sizeX, int sizeY) {

        path = new Texture("tiles_smooth_32x32/green.png");
        start = new Texture("tiles_smooth_32x32/red_h.png");
        end = new Texture("tiles_smooth_32x32/lightgreen_h.png");
        visited = new Texture("tiles_smooth_32x32/pink.png");
        normal = new Texture("tiles_smooth_32x32/yellow.png");
        blocked = new Texture("tiles_smooth_32x32/wall.png");

        this.sizeX = sizeX;
        this.sizeY = sizeY;

        nodes = new Node[sizeX][sizeY];
        for (int col = sizeY - 1; col >= 0; col--) {
            for (int row = sizeX - 1; row >= 0; row--) {

                nodes[row][col] = new Node(row, col);
            }
        }
        tiles = new Tile[sizeX][sizeY];

        nodes[0][0].setType(NodeType.START);
        nodes[nodes[0].length - 1][nodes.length - 1].setType(NodeType.END);

        processedNodes = new LinkedList<>();

        setBounds(
                getX(),
                getY(),
                tileDimensions * sizeY + margin,
                tileDimensions / 2 * sizeX + margin + marginBottom);

        this.mapFillable = true;

        this.pfTimer = PFTimer.getInstance();
    }

    /**
     * Constructor. Builds TileMap with given matrix and sets bounds depending on the size of the
     * labyrinth.
     *
     * @param matrix
     */
    public TileMap(Node[][] matrix) {

        path = new Texture("tiles_smooth_32x32/green.png");
        start = new Texture("tiles_smooth_32x32/red_h.png");
        end = new Texture("tiles_smooth_32x32/lightgreen_h.png");
        visited = new Texture("tiles_smooth_32x32/pink.png");
        normal = new Texture("tiles_smooth_32x32/yellow.png");
        blocked = new Texture("tiles_smooth_32x32/wall.png");

        this.sizeX = matrix.length;
        this.sizeY = matrix[0].length;

        nodes = matrix;

        tiles = new Tile[sizeX][sizeY];

        processedNodes = new LinkedList<>();

        setBounds(
                getX(),
                getY(),
                tileDimensions * sizeY + margin,
                tileDimensions / 2 * sizeX + margin + marginBottom);

        this.mapFillable = true;

        this.pfTimer = PFTimer.getInstance();
    }

    /**
     * Changes the properties of the TileMap so that it matches the given matrix.
     *
     * @param matrix
     */
    public void changeProperties(Node[][] matrix) {
        path = new Texture("tiles_smooth_32x32/green.png");
        start = new Texture("tiles_smooth_32x32/red_h.png");
        end = new Texture("tiles_smooth_32x32/lightgreen_h.png");
        visited = new Texture("tiles_smooth_32x32/pink.png");
        normal = new Texture("tiles_smooth_32x32/yellow.png");
        blocked = new Texture("tiles_smooth_32x32/wall.png");

        processedNodes.clear();

        this.sizeX = matrix.length;
        this.sizeY = matrix[0].length;

        nodes = matrix;
        tiles = new Tile[sizeX][sizeY];

        setBounds(
                getX(),
                getY(),
                tileDimensions * sizeY + margin,
                tileDimensions / 2 * sizeX + margin + marginBottom);

        this.mapFillable = true;
        fillMap();
    }

    /** Clears the processedNodes and reverts the changes of the Labyrinths. */
    public void resetLabyrinth() {

        for (int col = sizeY - 1; col >= 0; col--) {
            for (int row = sizeX - 1; row >= 0; row--) {
                Node tempNode = nodes[row][col];
                nodes[row][col] = new Node(row, col);
                switch (tempNode.getType()) {
                    case BLOCKED:
                        nodes[row][col].setType(NodeType.BLOCKED);
                        break;
                    case END:
                        nodes[row][col].setType(NodeType.END);
                        break;
                    case START:
                        nodes[row][col].setType(NodeType.START);
                        break;
                    default:
                        break;
                }
            }
        }
        processedNodes.clear();
        setMapFillable(true);
    }

    public void clearField() {

        processedNodes.clear();
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                Node n = new Node(j, i);
                updateNode(n);
            }
        }
        Node n = new Node(0, 0);
        n.setType(NodeType.START);
        updateNode(n);
        n = new Node(sizeX - 1, sizeY - 1);
        n.setType(NodeType.END);
        updateNode(n);
    }

    /**
     * Receives a node from the backend and stores it for later backend-independent visualisation.
     *
     * @param node
     */
    public void receiveNode(Node node) {
        //      Creating new node because backend may change their type later without frontend
        // knowing
        Node bufferedNode = new Node(node.getVertIndex(), node.getHorIndex());
        bufferedNode.setType(node.getType());
        processedNodes.addLast(bufferedNode);
    }

    /** Visualises the oldest stored node. */
    public void visualiseNode() {
        if (!processedNodes.isEmpty()) {
            Node poppedNode = processedNodes.removeFirst();
            updateNode(poppedNode);
        }
    }

    /**
     * Visualises a node if there are non-visualised backend-processed nodes remaining.
     *
     * @return
     */
    public boolean autoVisualiseNode() {
        if (processedNodes.isEmpty()) {
            return false;
        } else if (pfTimer.getPfRuntime() % this.autoPlaySpeed == 0) {
            visualiseNode();
        }
        return true;
    }

    /**
     * Changes the Node at the coordinates of given Node with given node. Updates the Tile based on
     * new Node.
     *
     * @param node
     */
    public void updateNode(Node node) {
        nodes[node.getVertIndex()][node.getHorIndex()] = node;
        Vector2 tilePosition = tiles[node.getVertIndex()][node.getHorIndex()].getWorldPos();
        tiles[node.getVertIndex()][node.getHorIndex()] = buildTile(node, tilePosition);
    }

    /**
     * Changes the Tile and Node at the matrixPosition to the given NodeType.
     *
     * @param type
     * @param matrixPosition
     */
    public void changeNode(NodeType type, Vector2 matrixPosition) {
        nodes[(int) matrixPosition.x][(int) matrixPosition.y] =
                new Node((int) matrixPosition.x, (int) matrixPosition.y);
        nodes[(int) matrixPosition.x][(int) matrixPosition.y].setType(type);
        Vector2 tileWorldPosition =
                tiles[nodes[(int) matrixPosition.x][(int) matrixPosition.y].getVertIndex()][
                        nodes[(int) matrixPosition.x][(int) matrixPosition.y].getHorIndex()]
                        .getWorldPos();
        tiles[(int) matrixPosition.x][(int) matrixPosition.y] =
                buildTile(nodes[(int) matrixPosition.x][(int) matrixPosition.y], tileWorldPosition);
        setMapEdited(true);
    }

    /**
     * Helper method. Builds Tile based on given parameters.
     *
     * @param node
     * @param tilePosition
     * @return
     */
    private Tile buildTile(Node node, Vector2 tilePosition) {
        if (node.getType() == NodeType.NORMAL) {
            return new Tile(
                    normal, new Vector2(node.getVertIndex(), node.getHorIndex()), tilePosition);
        } else if (node.getType() == NodeType.VISITED) {
            return new Tile(
                    visited, new Vector2(node.getVertIndex(), node.getHorIndex()), tilePosition);
        } else if (node.getType() == NodeType.BLOCKED) {
            return new Tile(
                    blocked, new Vector2(node.getVertIndex(), node.getHorIndex()), tilePosition);
        } else if (node.getType() == NodeType.PATH) {
            return new Tile(
                    path, new Vector2(node.getVertIndex(), node.getHorIndex()), tilePosition);
        } else if (node.getType() == NodeType.START) {
            return new Tile(
                    start, new Vector2(node.getVertIndex(), node.getHorIndex()), tilePosition);
        } else if (node.getType() == NodeType.END) {
            return new Tile(
                    end, new Vector2(node.getVertIndex(), node.getHorIndex()), tilePosition);
        } else {
            return null;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (mapFillable) {

            fillMap();
        }

        //      Check for Map related Inputs (use if else if we want to only process one input at a
        // time)
        checkInput();

        for (int col = sizeY - 1; col >= 0; col--) {
            for (int row = sizeX - 1; row >= 0; row--) {
                tiles[row][col].draw(batch, parentAlpha);
            }
        }
    }

    /** Lets tilemap react to gdx inputs. */
    private void checkInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            visualiseNode();
        }
    }

    @Override
    protected void positionChanged() {
        this.setPosition(getX(), getY());
        super.positionChanged();
    }

    /** Builds TileMap based on the state of the Node matrix */
    public void fillMap() {
        // spawn tiles
        for (int col = sizeY - 1; col >= 0; col--) {
            for (int row = sizeX - 1; row >= 0; row--) {

                // sizeX-1, so that the left bound of the TileMap is equal to the leftmost Tile.
                float x = getX() + margin / 2 + (sizeX - 1 + col - row) * tileDimensions / 2.0001f;
                float y = getY() + -marginBottom + margin / 2 + (row + col) * tileDimensions / 4f;

                if (nodes[row][col].getType() == NodeType.NORMAL) {
                    tiles[row][col] = new Tile(normal, new Vector2(row, col), new Vector2(x, y));
                } else if (nodes[row][col].getType() == NodeType.VISITED) {
                    tiles[row][col] = new Tile(visited, new Vector2(row, col), new Vector2(x, y));
                } else if (nodes[row][col].getType() == NodeType.BLOCKED) {
                    tiles[row][col] = new Tile(blocked, new Vector2(row, col), new Vector2(x, y));
                } else if (nodes[row][col].getType() == NodeType.PATH) {
                    tiles[row][col] = new Tile(path, new Vector2(row, col), new Vector2(x, y));
                } else if (nodes[row][col].getType() == NodeType.START) {
                    tiles[row][col] = new Tile(start, new Vector2(row, col), new Vector2(x, y));
                } else if (nodes[row][col].getType() == NodeType.END) {
                    tiles[row][col] = new Tile(end, new Vector2(row, col), new Vector2(x, y));
                }
            }
        }

        this.mapFillable = false;
    }

    /** Sets mapUpdatable to true so that the TileMap will be updated. */
    public void setMapFillable(boolean bool) {
        this.mapFillable = bool;
    }

    public int getAutoPlaySpeed() {
        return autoPlaySpeed;
    }

    public void setAutoPlaySpeed(int autoPlaySpeed) {
        this.autoPlaySpeed = autoPlaySpeed;
    }

    public LinkedList<Node> getProcessedNodes() {
        return processedNodes;
    }

    public float getMargin() {
        return margin;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public int getTileDimensionsSurfaceX() {
        return tileDimensionsSurfaceX;
    }

    public int getTileDimensionsSurfaceY() {
        return tileDimensionsSurfaceY;
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public boolean isMapEdited() {
        return mapEdited;
    }

    public void setMapEdited(boolean mapEdited) {
        this.mapEdited = mapEdited;
    }
}
