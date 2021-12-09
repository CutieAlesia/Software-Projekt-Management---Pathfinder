package main.java.main;

import main.java.api.APIManager;
import main.java.api.interfaces.IBackend;
import main.java.api.interfaces.IFrontend;
import main.java.api.models.Node;
import main.java.backend.AStar;
import main.java.frontend.Frontend;
import main.java.util.Util;

public class Main {

	public static void main(String[] args) {
//		Node[][] field = Util.generateField(10);
//		SearchAlgorithm alg = new DepthFirst(field);
//		alg.run();
//		Util.printField(alg.getField());
		
		APIManager manager = new APIManager();
		IFrontend frontend = new Frontend();
		IBackend backend = new AStar(manager); 
		Node[][] field = Util.generateField(10);
		
		manager.attachFrontend(frontend);
		manager.attachBackend(backend);
		manager.initMatrix(field);
		
		// daten im apimanager nur an ein backend bzw. frontend schicken
		// backend starten
	}
	
}
