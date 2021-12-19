import API.APIManager;
import API.Models.Node;
import GUI.Frontend;
import GUI.Generator.DepthFirst;
import backend.AStar;
import backend.SearchAlgorithm;

public class MainOld {

    public static void main(String[] args) {
        APIManager manager = new APIManager();
        Frontend frontend = new Frontend(manager);
        SearchAlgorithm backend = new AStar(manager);
        manager.attachFrontend(frontend);
        manager.attachBackend(backend);
<<<<<<< HEAD:core/src/MainOld.java
        int x = 10;
        int y = 10;
=======
        int x = 5;
        int y = 7;
>>>>>>> dev:src/main/java/Main.java
        DepthFirst a = new DepthFirst();
        Node[][] field = a.generateLabyrinth(x, y);
        frontend.setMatrix(field);
        manager.initMatrix(field);
        backend.run();

        // daten im apimanager nur an ein backend bzw. frontend schicken
        // backend starten
    }
}
