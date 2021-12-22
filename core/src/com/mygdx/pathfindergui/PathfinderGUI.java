package com.mygdx.pathfindergui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.map.TileMap;

import API.APIManager;
import API.Interfaces.IFrontend;
import API.Models.Node;
import GUI.Generator.DepthFirst;
import backend.AStar;
import backend.SearchAlgorithm;

/**
 * PathfinderGUI. Manages all of the GUI's components. Prepares GUI objects and places them onto the
 * stage.
 *
 * @author frontend
 */
public class PathfinderGUI extends ApplicationAdapter implements IFrontend {
    Stage stage;
    private TileMap map;
    private final int MAP_X= 43;
    private final int MAP_Y= 43;
    private Table table;


    APIManager manager;
    SearchAlgorithm backend;
    Node[][] field;

    PFTimer pfTimer;

//  Toggles autoplay mode
    boolean autoStepEnabled = false;

    /**
     * Sets up the stage. WIP!
     *
     * @author frontend
     */
    @Override
    public void create() {
        ScreenViewport viewport = new ScreenViewport();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        DepthFirst df = new DepthFirst();
        Node[][] labyrinth = df.generateLabyrinth(10, 10);
        map = new TileMap(labyrinth);
        map.setMapFillable(true);
        table = new Table();
        table.setFillParent(true);
        table.center();

        stage.addActor(table);
        table.add(map);
        Skin skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

//      B U T T O N S

        setupPermanentButtons(table, skin);

//      A P I

        manager = new APIManager();
        manager.attachFrontend(this);

//        backend = new AStar(manager);
//        manager.attachBackend(backend);

        attachNewAlgorithm(new AStar(manager));

        setupNewLabyrinth(MAP_X, MAP_Y);

        pfTimer = PFTimer.getInstance();

    }


    /**
     * Instantiates buttons that have a permanent place on the stage and adds them to passed table.
     *
     * @param table Table to add the buttons to.
     * @param skin The skin used for the buttons.
     */
    private void setupPermanentButtons(Table table, final Skin skin) {
        final TextButton bStartAlgorithm = new TextButton("Select AStar", skin);

        bStartAlgorithm.addListener(
            new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    launchBackend();
                    System.out.println("Clicked! Is checked: " + bStartAlgorithm.isChecked());
                    bStartAlgorithm.setDisabled(true);
                }
            });


        final TextButton bNextStep = new TextButton("Next Step", skin);

        bNextStep.addListener(
            new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    map.visualiseNode();
                    System.out.println("Clicked! Is checked: " + bNextStep.isChecked());
                }
            });


        final TextButton bNewRandomLabyrinth = new TextButton("Generate new Labyrinth", skin);

        bNewRandomLabyrinth.addListener(
            new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    setupNewLabyrinth(MAP_X, MAP_Y);
                    bStartAlgorithm.setDisabled(false);
                    System.out.println("Clicked! Is checked: " + bNextStep.isChecked());
                }
            });

        final TextButton bAutoStepAlgorithm = new TextButton("Autoplay", skin);

        bAutoStepAlgorithm.addListener(
            new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    autoStepEnabled = !autoStepEnabled;
                    System.out.println("Clicked! Is checked: " + bAutoStepAlgorithm.isChecked());
                }
            });

        table.add(bNewRandomLabyrinth);
        table.add(bStartAlgorithm);
        table.add(bNextStep);
        table.add(bAutoStepAlgorithm);



    }


    /**
     * Attaches passed algorithm to the currently loaded labyrinth.
     *
     * @param searchAlgorithm The algorithm to be set up.
     */
    private void attachNewAlgorithm(SearchAlgorithm searchAlgorithm) {
        backend = searchAlgorithm;
        manager.attachBackend(searchAlgorithm);
    }


    /**
     * Sets up a new randomly generated labyrinth with the passed dimensions.
     *
     * @param x
     * @param y
     */
    private void setupNewLabyrinth(int x, int y) {
        DepthFirst a = new DepthFirst();
        field = a.generateLabyrinth(x, y);
        map.changeProperties(field);
        manager.initMatrix(field);
    }

    /**
     * Renders a GUI frame and increments program runtime.
     * Automatically visualises backend-processed nodes if autoStep mode is enabled.
     *
     * @author frontend
     */
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0.1f, 0.2f, 0.3f, 1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        pfTimer.increasePfRuntime();

        if (autoStepEnabled) {
            if (!map.autoVisualiseNode()) { autoStepEnabled = false; }
        }
    }

    /**
     * Launches the backend. WIP!
     *
     * @author frontend
     */
    public void launchBackend() {
        attachNewAlgorithm(new backend.DepthFirst(manager));
        manager.initMatrix(field);
        map.changeProperties(field);
        backend.run();
    }

    @Override
    public void update(Node node) {
        map.receiveNode(node);
//        map.updateMap(node);
        render();
    }
}
