package com.mygdx.map;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import API.Models.NodeType;

public class TileMapInputProcessor implements InputProcessor{
	
	private TileMap map;
	private Stage stage;
	
	public TileMapInputProcessor(TileMap map, Stage stage) {
		this.map = map;
		this.stage = stage;
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

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector2 stageCoordinates = stage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
		if(button == Buttons.LEFT) {
			System.out.println("clicked: "+ stageCoordinates.x + "  "+ stageCoordinates.y);
			Vector2 foundTilesCoordinates = determinePressedTile((int)stageCoordinates.x, (int)stageCoordinates.y);
			if(foundTilesCoordinates != null) {
				if(map.getNodes()[(int)foundTilesCoordinates.x][(int)foundTilesCoordinates.y].getType() == NodeType.BLOCKED) {
					System.out.println(foundTilesCoordinates.x + "  "+  foundTilesCoordinates.y);
					map.changeNode(NodeType.NORMAL, foundTilesCoordinates);
				}else if(map.getNodes()[(int)foundTilesCoordinates.x][(int)foundTilesCoordinates.y].getType() == NodeType.NORMAL) {
					System.out.println(foundTilesCoordinates.x + "  "+  foundTilesCoordinates.y);
					map.changeNode(NodeType.BLOCKED, foundTilesCoordinates);
				}
					
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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
	
	private Vector2 determinePressedTile(int stageX, int stageY) {
		
		for(int i = map.getTiles().length -1; i >= 0; i--) {
			for(int j = map.getTiles()[0].length -1; j >= 0; j--) {
				Vector2 leftEdge = new Vector2(map.getTiles()[j][i].getWorldPos().x, map.getTiles()[j][i].getWorldPos().y+ map.getTileDimensionsSurfaceY());
//				System.out.println( "links   " + A.x + "  "+ A.y);
				Vector2 upperEdge = new Vector2(map.getTiles()[j][i].getWorldPos().x + map.getTileDimensionsSurfaceX()/2, map.getTiles()[j][i].getWorldPos().y + (int)(map.getTileDimensionsSurfaceY()/2) + map.getTileDimensionsSurfaceY());
//				System.out.println("oben   " + C.x + "  "+ C.y);
				Vector2 lowerEdge = new Vector2(map.getTiles()[j][i].getWorldPos().x + map.getTileDimensionsSurfaceX()/2, map.getTiles()[j][i].getWorldPos().y -(int)(map.getTileDimensionsSurfaceY()/2) + map.getTileDimensionsSurfaceY());
//				System.out.println("unten   " + B.x + "  "+ B.y);
				Vector2 rightEdge = new Vector2(map.getTiles()[j][i].getWorldPos().x + map.getTileDimensionsSurfaceX(), map.getTiles()[j][i].getWorldPos().y+ map.getTileDimensionsSurfaceY());
				// Compute vectors        
				Vector2 v0 = new Vector2(upperEdge.x-leftEdge.x, upperEdge.y-leftEdge.y);
				Vector2 v1 = new Vector2(lowerEdge.x-leftEdge.x, lowerEdge.y-leftEdge.y);
				Vector2 v2 = new Vector2(stageX-leftEdge.x, stageY-leftEdge.y);

				// Compute dot products
				double dot00 = dotProduct(v0, v0);
			    double dot01 = dotProduct(v0, v1);
			    double dot02 = dotProduct(v0, v2);
			    double dot11 = dotProduct(v1, v1);
				double dot12 = dotProduct(v1, v2);

				// Compute barycentric coordinates
				double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
				double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
				double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

				// Check if point is in triangle
				if((u >= 0) && (v >= 0) && (u + v < 1)) {
					return new Vector2(j,i);
				}
				
//				Vector2 A = new Vector2(map.getTiles()[j][i].getWorldPos().x, map.getTiles()[j][i].getWorldPos().y - map.getMarginBottom()/2);
//				Vector2 C = new Vector2(map.getTiles()[j][i].getWorldPos().x + map.getTileDimensions(), map.getTiles()[j][i].getWorldPos().y);
//				Vector2 B = new Vector2(map.getTiles()[j][i].getWorldPos().x, map.getTiles()[j][i].getWorldPos().y);
//				// Compute vectors        
//				Vector2 v0 = new Vector2(C.x-A.x, C.y-A.y);
//				Vector2 v1 = new Vector2(B.x-A.x, B.y-A.y);
//				Vector2 v2 = new Vector2(screenX-A.x, screenY-A.y);
//
//				// Compute dot products
//				double dot00 = dotProduct(v0, v0);
//			    double dot01 = dotProduct(v0, v1);
//			    double dot02 = dotProduct(v0, v2);
//			    double dot11 = dotProduct(v1, v1);
//				double dot12 = dotProduct(v1, v2);
//
//				// Compute barycentric coordinates
//				double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
//				double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
//				double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
//
//				// Check if point is in triangle
//				if((u >= 0) && (v >= 0) && (u + v < 1)) {
//					return new Vector2(j,i);
//				}
			}
		}

		
		return null;
	}
	
	  public static double dotProduct(Vector2 a, Vector2 b) {

		    return a.x * b.x + a.y * b.y;
		  }

}
