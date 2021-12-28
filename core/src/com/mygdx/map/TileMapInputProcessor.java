package com.mygdx.map;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import API.Models.NodeType;

/**TileMapInputProcessor. Contains the logic for making tiles clickable. Very dependent on "TileMap" and especially the "fillMap()"-method in TileMap. 
 * 
 * 
 * @author frontend
 *
 */

public class TileMapInputProcessor implements InputProcessor{
	
	private TileMap map;
	private Stage stage;
	
	private float yAxisCorrection;
	
	private Vector2 lastChangedTileWhileDragging;
	private NodeType typeOfFirstDraggedNode;
	
	public TileMapInputProcessor(TileMap map, Stage stage) {
		this.map = map;
		this.stage = stage;
		this.yAxisCorrection =  map.getTileDimensionsSurfaceY() + 2;
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

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/**
	 * Releasing the left mouse button switches the state of the Tile between BLOCKED and NORMAL. Resets the Components needed for touchDragged.
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector2 stageCoordinates = stage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
		if(button == Buttons.LEFT) {
			Vector2 foundTilesCoordinates = determinePressedTile((int)stageCoordinates.x, (int)stageCoordinates.y);
			if(foundTilesCoordinates != null) {
				if(map.getNodes()[(int)foundTilesCoordinates.x][(int)foundTilesCoordinates.y].getType() == NodeType.BLOCKED) {
					map.changeNode(NodeType.NORMAL, foundTilesCoordinates);
				}else if(map.getNodes()[(int)foundTilesCoordinates.x][(int)foundTilesCoordinates.y].getType() == NodeType.NORMAL) {
					map.changeNode(NodeType.BLOCKED, foundTilesCoordinates);
				}
					
			}
		}
		lastChangedTileWhileDragging = null;
		typeOfFirstDraggedNode = null;
		return false;
	}

	/**
	 * Dragging the cursor over the screen changes the NodeTypes. 
	 * Depending on which NodeType the process started, only Blocked Tiles are changed to normal Tiles and vice versa.
	 */
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector2 stageCoordinates = stage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
		Vector2 foundTilesPosition = determinePressedTile((int)stageCoordinates.x, (int)stageCoordinates.y);
		if(foundTilesPosition != null) {
			setupTypeOfFirstDraggedNode(foundTilesPosition);
			if(typeOfFirstDraggedNode != null) {
				if(!foundTilesPosition.equals(lastChangedTileWhileDragging)) {
					if(map.getNodes()[(int)foundTilesPosition.x][(int)foundTilesPosition.y].getType() == NodeType.BLOCKED && typeOfFirstDraggedNode == NodeType.BLOCKED) {
						map.changeNode(NodeType.NORMAL, foundTilesPosition);
					}else if(map.getNodes()[(int)foundTilesPosition.x][(int)foundTilesPosition.y].getType() == NodeType.NORMAL && typeOfFirstDraggedNode == NodeType.NORMAL) {
						map.changeNode(NodeType.BLOCKED, foundTilesPosition);
					}
					
					lastChangedTileWhileDragging = foundTilesPosition;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Sets typeOfFirstDraggedNode after starting to drag the mouse if the Tile was Normal or Blocked.
	 */
	private void setupTypeOfFirstDraggedNode(Vector2 foundTilesPosition) {
		if(typeOfFirstDraggedNode == null) {
			NodeType foundTilesNodeType = map.getNodes()[(int)foundTilesPosition.x][(int)foundTilesPosition.y].getType();
			if(foundTilesNodeType == NodeType.BLOCKED || foundTilesNodeType == NodeType.NORMAL) {
				typeOfFirstDraggedNode = foundTilesNodeType;
				
			}
		}
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
	 * Using barycentric coordinates to determine whether a Tile was clicked on. Every Tile is halfed into a left and right Rectangle for this matter.
	 * @param stageX
	 * @param stageY
	 * @return
	 */
	private Vector2 determinePressedTile(int stageX, int stageY) {
		
		for(int i = map.getTiles().length -1; i >= 0; i--) {
			for(int j = map.getTiles()[0].length -1; j >= 0; j--) {
				Vector2 leftEdge = new Vector2(map.getTiles()[j][i].getWorldPos().x, map.getTiles()[j][i].getWorldPos().y+ yAxisCorrection);
				Vector2 upperEdge = new Vector2(map.getTiles()[j][i].getWorldPos().x + map.getTileDimensionsSurfaceX()/2, map.getTiles()[j][i].getWorldPos().y + (int)(map.getTileDimensionsSurfaceY()/2) + yAxisCorrection);
				Vector2 lowerEdge = new Vector2(map.getTiles()[j][i].getWorldPos().x + map.getTileDimensionsSurfaceX()/2, map.getTiles()[j][i].getWorldPos().y -(int)(map.getTileDimensionsSurfaceY()/2) + yAxisCorrection);
				Vector2 rightEdge = new Vector2(map.getTiles()[j][i].getWorldPos().x + map.getTileDimensionsSurfaceX(), map.getTiles()[j][i].getWorldPos().y+ yAxisCorrection);
				
				
				// Compute vectors        
				Vector2 leftToUpperEdge = new Vector2(upperEdge.x-leftEdge.x, upperEdge.y-leftEdge.y);
				Vector2 leftToLowerEdge = new Vector2(lowerEdge.x-leftEdge.x, lowerEdge.y-leftEdge.y);
				Vector2 leftEdgeToClickedPoint = new Vector2(stageX-leftEdge.x, stageY-leftEdge.y);

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
				if((u >= 0) && (v >= 0) && (u + v < 1)) {
					return new Vector2(j,i);
				}
				
				// Compute vectors        
				Vector2 rightToUpperEdge = new Vector2(upperEdge.x-rightEdge.x, upperEdge.y-rightEdge.y);
				Vector2 rightToLowerEdge = new Vector2(lowerEdge.x-rightEdge.x, lowerEdge.y-rightEdge.y);
				Vector2 rightEdgeToClickedPoint = new Vector2(stageX-rightEdge.x, stageY-rightEdge.y);
				


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
				if((u >= 0) && (v >= 0) && (u + v < 1)) {
					return new Vector2(j,i);
				}
			}
		}

		
		return null;
	}
	
	  private double dotProduct(Vector2 a, Vector2 b) {

		    return a.x * b.x + a.y * b.y;
		  }

}
