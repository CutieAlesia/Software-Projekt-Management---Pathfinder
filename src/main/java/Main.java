import API.APIManager;
import API.Models.Node;
import GUI.Frontend;
import backend.AStar;
import backend.SearchAlgorithm;
import util.Util;

public class Main {

    public static void main(String[] args) {
        APIManager manager = new APIManager();
        Frontend frontend = new Frontend(manager);
        SearchAlgorithm backend = new AStar(manager);
        manager.attachFrontend(frontend);
        manager.attachBackend(backend);
        Node[][] field = Util.generateField(5);
        frontend.setMatrix(field);
        manager.initMatrix(field);
        backend.run();

        // daten im apimanager nur an ein backend bzw. frontend schicken
        // backend starten
    }
}
