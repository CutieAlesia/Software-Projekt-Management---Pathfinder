package com.mygdx.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import API.Models.NodeType;

/**
 * TileMapInputProcessor. Contains the logic for making tiles clickable.
 *
 * @author frontend
 */
public class TileMapInputProcessor implements InputProcessor {

    private TileMap map;
    private Stage stage;

    private float yAxisCorrection;

    private boolean inputAllowed;

    private Vector2 nodePositionStash;
    private NodeType nodeTypeStash;

    public TileMapInputProcessor(TileMap map, Stage stage) {
        this.map = map;
        this.stage = stage;
        this.yAxisCorrection = map.getTileDimensionsSurfaceY() + 2;
        this.inputAllowed = true;
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Left click builds blocked and right click builds normal tiles. When the start or end tile is
     * left clicked, it is stashed and can be repositioned with the next left click.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (inputAllowed) {
            Vector2 stageCoordinates =
                    stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));
            Vector2 foundTilesCoordinates;

            // if a tile was stashed
            if (nodeTypeStash != null && nodePositionStash != null) {
                if (button == Buttons.LEFT) {
                    foundTilesCoordinates =
                            determinePressedTile(
                                    (int) stageCoordinates.x, (int) stageCoordinates.y);
                    if (foundTilesCoordinates != null
                            && map
                                            .getNodes()[(int) foundTilesCoordinates.x][
                                            (int) foundTilesCoordinates.y]
                                            .getType()
                                    != NodeType.START
                            && map
                                            .getNodes()[(int) foundTilesCoordinates.x][
                                            (int) foundTilesCoordinates.y]
                                            .getType()
                                    != NodeType.END) {
                        map.changeNode(NodeType.NORMAL, nodePositionStash);
                        map.changeNode(nodeTypeStash, foundTilesCoordinates);
                        nodePositionStash = null;
                        nodeTypeStash = null;
                    }
                }
            }

            // left click if no tile was stashed
            else if (button == Buttons.LEFT) {
                foundTilesCoordinates =
                        determinePressedTile((int) stageCoordinates.x, (int) stageCoordinates.y);
                if (foundTilesCoordinates != null) {
                    if (map.getNodes()[(int) foundTilesCoordinates.x][(int) foundTilesCoordinates.y]
                                    .getType()
                            == NodeType.NORMAL) {
                        map.changeNode(NodeType.BLOCKED, foundTilesCoordinates);
                    } else if (map
                                            .getNodes()[(int) foundTilesCoordinates.x][
                                            (int) foundTilesCoordinates.y]
                                            .getType()
                                    == NodeType.START
                            || map
                                            .getNodes()[(int) foundTilesCoordinates.x][
                                            (int) foundTilesCoordinates.y]
                                            .getType()
                                    == NodeType.END) {
                        nodeTypeStash =
                                map
                                        .getNodes()[(int) foundTilesCoordinates.x][
                                        (int) foundTilesCoordinates.y]
                                        .getType();
                        nodePositionStash =
                                new Vector2(foundTilesCoordinates.x, foundTilesCoordinates.y);
                    }
                }
            }
            // right click if no tile was stashed
            else if (button == Buttons.RIGHT) {
                foundTilesCoordinates =
                        determinePressedTile((int) stageCoordinates.x, (int) stageCoordinates.y);
                if (foundTilesCoordinates != null) {

                    if (map.getNodes()[(int) foundTilesCoordinates.x][(int) foundTilesCoordinates.y]
                                    .getType()
                            == NodeType.BLOCKED) {
                        map.changeNode(NodeType.NORMAL, foundTilesCoordinates);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Dragging the cursor over the screen changes the NodeTypes. Left click builds blockled and
     * right click builds normal tiles.
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (inputAllowed) {
            Vector2 stageCoordinates =
                    stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));
            Vector2 foundTilesCoordinates;

            if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
                foundTilesCoordinates =
                        determinePressedTile((int) stageCoordinates.x, (int) stageCoordinates.y);
                if (foundTilesCoordinates != null) {
                    if (map.getNodes()[(int) foundTilesCoordinates.x][(int) foundTilesCoordinates.y]
                                    .getType()
                            == NodeType.NORMAL) {
                        map.changeNode(NodeType.BLOCKED, foundTilesCoordinates);
                    }
                }
            } else if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
                foundTilesCoordinates =
                        determinePressedTile((int) stageCoordinates.x, (int) stageCoordinates.y);
                if (foundTilesCoordinates != null) {
                    if (map.getNodes()[(int) foundTilesCoordinates.x][(int) foundTilesCoordinates.y]
                                    .getType()
                            == NodeType.BLOCKED) {
                        map.changeNode(NodeType.NORMAL, foundTilesCoordinates);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Using barycentric coordinates to determine whether a Tile was clicked on. Every Tile is
     * halfed into a left and right Rectangle for this matter.
     *
     * @param stageX
     * @param stageY
     * @return
     */
    private Vector2 determinePressedTile(int stageX, int stageY) {

        for (int i = map.getTiles().length - 1; i >= 0; i--) {
            for (int j = map.getTiles()[0].length - 1; j >= 0; j--) {
                Vector2 leftEdge =
                        new Vector2(
                                map.getTiles()[j][i].getWorldPos().x,
                                map.getTiles()[j][i].getWorldPos().y + yAxisCorrection);
                Vector2 upperEdge =
                        new Vector2(
                                map.getTiles()[j][i].getWorldPos().x
                                        + map.getTileDimensionsSurfaceX() / 2,
                                map.getTiles()[j][i].getWorldPos().y
                                        + (int) (map.getTileDimensionsSurfaceY() / 2)
                                        + yAxisCorrection);
                Vector2 lowerEdge =
                        new Vector2(
                                map.getTiles()[j][i].getWorldPos().x
                                        + map.getTileDimensionsSurfaceX() / 2,
                                map.getTiles()[j][i].getWorldPos().y
                                        - (int) (map.getTileDimensionsSurfaceY() / 2)
                                        + yAxisCorrection);
                Vector2 rightEdge =
                        new Vector2(
                                map.getTiles()[j][i].getWorldPos().x
                                        + map.getTileDimensionsSurfaceX(),
                                map.getTiles()[j][i].getWorldPos().y + yAxisCorrection);

                // Compute vectors
                Vector2 leftToUpperEdge =
                        new Vector2(upperEdge.x - leftEdge.x, upperEdge.y - leftEdge.y);
                Vector2 leftToLowerEdge =
                        new Vector2(lowerEdge.x - leftEdge.x, lowerEdge.y - leftEdge.y);
                Vector2 leftEdgeToClickedPoint =
                        new Vector2(stageX - leftEdge.x, stageY - leftEdge.y);

                // Compute dot products
                double dot00 = dotProduct(leftToUpperEdge, leftToUpperEdge);
                double dot01 = dotProduct(leftToUpperEdge, leftToLowerEdge);
                double dot02 = dotProduct(leftToUpperEdge, leftEdgeToClickedPoint);
                double dot11 = dotProduct(leftToLowerEdge, leftToLowerEdge);
                double dot12 = dotProduct(leftToLowerEdge, leftEdgeToClickedPoint);

                // Compute barycentric coordinates
                double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
                double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
                double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

                // Check if point is in left triangle
                if ((u >= 0) && (v >= 0) && (u + v < 1)) {
                    return new Vector2(j, i);
                }

                // Compute vectors
                Vector2 rightToUpperEdge =
                        new Vector2(upperEdge.x - rightEdge.x, upperEdge.y - rightEdge.y);
                Vector2 rightToLowerEdge =
                        new Vector2(lowerEdge.x - rightEdge.x, lowerEdge.y - rightEdge.y);
                Vector2 rightEdgeToClickedPoint =
                        new Vector2(stageX - rightEdge.x, stageY - rightEdge.y);

                // Compute dot products
                dot00 = dotProduct(rightToUpperEdge, rightToUpperEdge);
                dot01 = dotProduct(rightToUpperEdge, rightToLowerEdge);
                dot02 = dotProduct(rightToUpperEdge, rightEdgeToClickedPoint);
                dot11 = dotProduct(rightToLowerEdge, rightToLowerEdge);
                dot12 = dotProduct(rightToLowerEdge, rightEdgeToClickedPoint);

                // Compute barycentric coordinates
                invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
                u = (dot11 * dot02 - dot01 * dot12) * invDenom;
                v = (dot00 * dot12 - dot01 * dot02) * invDenom;

                // Check if point is in triangle
                if ((u >= 0) && (v >= 0) && (u + v < 1)) {
                    return new Vector2(j, i);
                }
            }
        }

        return null;
    }

    private double dotProduct(Vector2 a, Vector2 b) {

        return a.x * b.x + a.y * b.y;
    }

    public boolean isInputAllowed() {
        return inputAllowed;
    }

    public void setInputAllowed(boolean inputAllowed) {
        this.inputAllowed = inputAllowed;
    }
}
