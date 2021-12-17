package GUI;

import API.APIManager;
import API.Interfaces.IBackend;
import API.Models.Node;
import API.Models.NodeType;

public class B implements IBackend{

	static int x = 10;
	static int y = 10;
	Node[][] matrix;
	
	private APIManager api;
	public B(APIManager api) {
		this.api = api;
	}
	
	@Override
	public void receive(Node[][] matrix) {
		
        System.out.format("[API - Backend] New Matrix received");
        this.matrix = matrix;
        c();
		
	}
	
	public void sendToFrontEnd(Node node) {
	    api.sendToFrontend(node);
	}

	private void c () {
		while(x < 15) {
			matrix[x][y].setType(NodeType.PATH);
			sendToFrontEnd(matrix[10][10]);
			x++;
			y++;
		}
	}
}
