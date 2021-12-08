package GUI.Generator;


/**
 * Generates a Labyrinth via DepthFirst-algorithm.
 * 
 * @author 
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import API.Interfaces.IBackend;
import API.Interfaces.IFrontend;
import API.APIManager;
import API.Models.Node;
import API.Models.NodeType;

public class DepthFirst implements LabyrinthGenerator{
	
	DFNode [][]matrix;
	int matrixX;
	int matrixY;
	
	Node [][]finalMatrix;
//	ArrayList<Node> unblockedNodes = Array
	

	@Override
	public Node[][] generateLabyrinth(int x, int y) {
		matrixX = x;
		matrixY = y;
		finalMatrix = new Node[x][y];
		matrix = buildDFMatrix(x, y);
		startAlgorithm();
		transcribeInAPINode(finalMatrix, x, y);
		setStart(finalMatrix, x, y);
		setEnd(finalMatrix, x, y);
		

		return finalMatrix;
	}
	

	private void transcribeInAPINode(Node[][] finalMatrix, int x, int y) {
		for(int i=0; i<y;i++) {
			for(int j=0; j<x; j++) {
				if(matrix[j][i].isBlocked()) {
					finalMatrix[j][i] = new Node(j,i);
					finalMatrix[j][i].setType(NodeType.BLOCKED);
				}else {
					finalMatrix[j][i] = new Node(j,i);
					finalMatrix[j][i].setType(NodeType.NORMAL);
				}
			}
		}
	}
	
	
	private void setStart(Node[][] finalMatrix, int x, int y) {
		boolean startIsSet = false;
		for(int i=0; i<y;i++) {
			for(int j=0; j<x; j++) {
				if(finalMatrix[j][i].getType() == NodeType.NORMAL) {
					finalMatrix[j][i].setType(NodeType.START);
					startIsSet = true;
				}
				if(startIsSet) {
					break;
				}
			}
			if(startIsSet) {
				break;
			}
		}
	}
	

	private void setEnd(Node[][] finalMatrix, int x, int y) {
		boolean endIsSet = false;
		for(int i=y-1; i>=0;i--) {
			for(int j=x-1; j>=0; j--) {
				if(finalMatrix[j][i].getType() == NodeType.NORMAL) {
					finalMatrix[j][i].setType(NodeType.END);
					endIsSet = true;
				}
				if(endIsSet) {
					break;
				}
			}
			if(endIsSet) {
				break;
			}
		}
	}
	
	
	
	private DFNode[][] buildDFMatrix(int sizeX, int sizeY) {
		
		DFNode [][]field = new DFNode[sizeX][sizeY];
		
		for(int i=0; i<sizeY;i++) {
			for(int j=0; j<sizeX; j++) {
				field[j][i] = new DFNode(j,i, null, true);
			}
		}
		return field;
	}
	
	private void startAlgorithm() {
		depthFirst(0, 0);
		
		
	}
	
	/**
	 * Traverses unvisited nodes recursively and unblocks them. 
	 * @param x
	 * @param y
	 */
	private void depthFirst(int x, int y) {
		Integer []array = {1,2,3,4};
        List<Integer> list =Arrays.asList(array);
        Collections.shuffle(list);
        list.toArray(array);
		int index = 0;
		
		matrix[x][y].setVisited(true);
		matrix[x][y].setBlocked(false);
		
		while(index < array.length) {
			switch(array[index]) {
				case 1:
					if (checkIfNodeIsVisitable( x,  y-2)) {
						matrix[x][y-1].setBlocked(false);
						depthFirst(x, y-2);
					}
					index++;
					break;
				case 2:
					if (checkIfNodeIsVisitable( x,  y+2)) {
						matrix[x][y+1].setBlocked(false);
						depthFirst( x,  y+2);
					}
					index++;
					break;
				case 3:
					if (checkIfNodeIsVisitable( x-2,  y)) {
						matrix[x-1][y].setBlocked(false);
						depthFirst (x-2,  y);
					}
					index++;
					break;
				case 4:
					if (checkIfNodeIsVisitable( x+2,  y)) {
						matrix[x+1][y].setBlocked(false);
						depthFirst( x+2,  y);
					}
					index++;
					break;
				default:
					System.out.println("Fehler in funktion");
					System.exit(1);
			}
		}
	}
	
	private boolean checkIfNodeIsVisitable(int x, int y) {
		if (x >= 0 && x<matrixX && y >= 0 && y<matrixY) {
			if(!(matrix[x][y].isVisited())) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public DFNode[][] getMatrix() {
		return matrix;
	}






	
	public static void  main(String[]args) {
		DepthFirst a = new DepthFirst();
		int x = 80;
		int y = 40;
		Node[][] b = a.generateLabyrinth(x, y);
		for(int i=0; i<y;i++) {
			for(int j=0; j<x; j++) {
				if(b[j][i].getType() == NodeType.BLOCKED) {
					System.out.print("II");
				}else if (b[j][i].getType() == NodeType.START){
					System.out.print("oo");
				}else if (b[j][i].getType() == NodeType.END){
					System.out.print("~~");
				}else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}
}


























