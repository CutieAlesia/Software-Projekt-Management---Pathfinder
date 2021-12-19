package com.mygdx.map;

import java.io.IOException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import API.Models.Node;
import API.Models.NodeType;

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

    //
    private Node[][] nodes;
    //
    private Tile[][] tiles;

    // map will be updated if true
    private boolean mapUpdatable;

    /**
     * Constructor. Generates a new Node matrix and sets the bounds of the map depending on the size
     * of the labyrinth.
     *
     * @param sizeX
     * @param sizeY
     */
    public TileMap(int sizeX, int sizeY) {

        path = new Texture("tiles_smooth/green.png");
        start = new Texture("tiles_smooth/ice.png");
        end = new Texture("tiles_smooth/ice.png");
        visited = new Texture("tiles_smooth/pink.png");
        normal = new Texture("tiles_smooth/yellow.png");
        blocked = new Texture("tiles_smooth/wall.png");

        this.sizeX = sizeX;
        this.sizeY = sizeY;

        nodes = new Node[sizeX][sizeY];
        for (int col = sizeY - 1; col >= 0; col--) {
            for (int row = sizeX - 1; row >= 0; row--) {

                nodes[row][col] = new Node(row, col);
            }
        }
        tiles = new Tile[sizeX][sizeY];

        setBounds(getX(), getY(), 64 * sizeY, 32 * sizeX);

        this.mapUpdatable = true;
    }

    /**
     * Constructor. Builds TileMap with given matrix and sets bounds depending on the size of the
     * labyrinth.
     *
     * @param matrix
     */
    public TileMap(Node[][] matrix) {

        path = new Texture("tiles_smooth/green.png");
        start = new Texture("tiles_smooth/ice.png");
        end = new Texture("tiles_smooth/ice.png");
        visited = new Texture("tiles_smooth/pink.png");
        normal = new Texture("tiles_smooth/yellow.png");
        blocked = new Texture("tiles_smooth/wall.png");

        this.sizeX = matrix.length;
        this.sizeY = matrix[0].length;

        nodes = matrix;

        tiles = new Tile[sizeX][sizeY];

        setBounds(getX(), getY(), 64 * sizeY, 32 * sizeX);

        this.mapUpdatable = true;
    }

    /**
     * Changes the properties of the TileMap so that it matches the given matrix.
     *
     * @param matrix
     */
    public void changeProperties(Node[][] matrix) {
        path = new Texture("tiles_smooth/green.png");
        start = new Texture("tiles_smooth/ice.png");
        visited = new Texture("tiles_smooth/pink.png");
        normal = new Texture("tiles_smooth/yellow.png");
        blocked = new Texture("tiles_smooth/wall.png");

        this.sizeX = matrix.length;
        this.sizeY = matrix[0].length;

        nodes = matrix;
        tiles = new Tile[sizeX][sizeY];

        fillMap();

        setBounds(getX(), getY(), 64 * sizeY, 32 * sizeX);

        this.mapUpdatable = true;
    }

    /**
     * Changes the Node at the coordinates of given Node with given node. Updates the Tile based on
     * new Node.
     *
     * @param node
     */
    public void updateMap(Node node) {
        nodes[node.getVertIndex()][node.getHorIndex()] = node;
        Vector2 tilePosition = tiles[node.getVertIndex()][node.getHorIndex()].getWorldPos();
        tiles[node.getVertIndex()][node.getHorIndex()] =
                buildTileBasedOnNodeType(node, tilePosition);
    }

    /**
     * Helper method. Builds Tile based on given parameters.
     *
     * @param node
     * @param tilePosition
     * @return
     */
    private Tile buildTileBasedOnNodeType(Node node, Vector2 tilePosition) {
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

    /** Sets mapUpdatable to true so that the TileMap will be updated. */
    public void setMapUpdatableTrue() {
        this.mapUpdatable = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (mapUpdatable) {

            fillMap();
        }

        for (int col = sizeY - 1; col >= 0; col--) {
            for (int row = sizeX - 1; row >= 0; row--) {
                tiles[row][col].draw(batch, parentAlpha);
            }
        }
    }

    @Override
    protected void positionChanged() {
        this.setPosition(getX(), getY());
        super.positionChanged();
    }

    /**
     * Builds TileMap based on the state of the Node matrix
     *
     * @throws IOException
     */
    public void fillMap() {
        //	  spawn tiles
        for (int col = sizeY - 1; col >= 0; col--) {
            for (int row = sizeX - 1; row >= 0; row--) {

                // sizeX-1, so that the left bound of the TileMap is equal to the leftmost Tile.
                float x = getX() + (sizeX - 1 + col - row) * 64 / 2.0001f;
                float y = getY() + (row + col) * 64 / 4f;

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

        this.mapUpdatable = false;
    }
}
