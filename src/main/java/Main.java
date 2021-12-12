import API.APIManager;
import API.Interfaces.IBackend;
import API.Interfaces.IFrontend;
import API.Models.Node;
import GUI.Frontend;
import backend.AStar;
import util.Util;

public class Main {

    public static void main(String[] args) {
        // Node[][] field = Util.generateField(10);
        // SearchAlgorithm alg = new DepthFirst(field);
        // alg.run();
        // Util.printField(alg.getField());

        APIManager manager = new APIManager();
        IFrontend frontend = new Frontend(manager);
        IBackend backend = new AStar(manager);
        Node[][] field = Util.generateField(10);

        manager.attachFrontend(frontend);
        manager.attachBackend(backend);
        manager.initMatrix(field);

        // daten im apimanager nur an ein backend bzw. frontend schicken
        // backend starten
    }
}
