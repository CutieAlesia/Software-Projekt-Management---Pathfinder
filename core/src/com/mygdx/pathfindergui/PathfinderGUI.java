package com.mygdx.pathfindergui;

import GUI.Generator.RecursiveDivision;
import API.Models.NodeType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.SupportedAlgorithms;
import com.mygdx.labels.ExplanationLabel;
import com.mygdx.map.TileMap;
import com.mygdx.map.TileMapInputProcessor;

import API.APIManager;
import API.Interfaces.IFrontend;
import API.Models.Node;
import backend.AStar;
import backend.BestFirst;
import backend.BranchAndBound;
import backend.SearchAlgorithm;

import java.util.ArrayList;

/**
 * PathfinderGUI. Manages all of the GUI's components. Prepares GUI objects and places them onto the
 * stage.
 *
 * @author frontend
 */
public class PathfinderGUI extends ApplicationAdapter implements IFrontend {
    private Stage stage;
    private TileMap map;
    private final int MAP_X = 45;
    private final int MAP_Y = 45;
    private Table mapTable;
    private Table buttonTable;
    private Table counterTable;
    private Skin skin;
    private InputMultiplexer inputMultiplexer;

    private TileMapInputProcessor tileMapInputProcessor;

    private APIManager manager;
    private SearchAlgorithm backend;
    private Node[][] field;

    private PFTimer pfTimer;

    private ArrayList<Integer> algoTimes = new ArrayList<>();
    private ArrayList<Integer> algoSteps = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();
    private ArrayList<Node> receivedNodes = new ArrayList<>();

    //  Toggles autoplay mode
    private boolean autoStepEnabled = false;

    /**
     * Sets up the stage. WIP!
     *
     * @author frontend
     */
    @Override
    public void create() {
        ScreenViewport viewport = new ScreenViewport();
        stage = new Stage(viewport);
        setupEmptyField(MAP_X, MAP_Y);
        map.setMapFillable(true);
        mapTable = new Table();
        mapTable.setFillParent(true);
        mapTable.align(Align.bottom);

        buttonTable = new Table();
        buttonTable.setFillParent(true);
        // buttonTable.setBounds(20, -20, 20, 20);
        buttonTable.align(Align.topLeft);
        buttonTable.pad(30, 30, 30, 0);

        // Table for labels
        counterTable = new Table();
        counterTable.setFillParent(true);
        counterTable.align(Align.topLeft);
        LabelStyleGenerator labelStyleGenerator = new LabelStyleGenerator();
        Label counterHeader = new Label("Zeit und Schritte des Algorithmus\n", labelStyleGenerator.generateLabelStyle( "font/RobotoMono-VariableFont_wght.ttf",Color.valueOf("#FFDCA4"),18));
        counterTable.add(counterHeader);
        counterTable.row();
        counterTable.pad(60, 30, 30, 0);


        //  Buttons

        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        setupPermanentButtons(buttonTable, skin);

        // Table order
        // Add table containing the buttons before table containing the field to avoid dropdown
        // transparency issue

        stage.addActor(counterTable);
        stage.addActor(buttonTable);
        stage.addActor(mapTable);

        mapTable.add(map);

        setupAStarText();

        // A P I

        manager = new APIManager();
        manager.attachFrontend(this);

        //        backend = new AStar(manager);
        //        manager.attachBackend(backend);

        // attachNewAlgorithm(new AStar(manager));

        pfTimer = PFTimer.getInstance();

        tileMapInputProcessor = new TileMapInputProcessor(map, stage);
        inputMultiplexer = new InputMultiplexer();

        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(tileMapInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     * Instantiates buttons that have a permanent place on the stage and adds them to passed table.
     *
     * @param table Table to add the buttons to.
     * @param skin The skin used for the buttons.
     */
    private void setupPermanentButtons(Table table, final Skin skin) {

        final SelectBox<String> sbSearchAlgorithms = new SelectBox<>(skin);
        final String[] searchAlgorithms = {
            "AStar", "BestFirst", "BranchAndBound", "BreadthFirst", "DepthFirst", "Dijkstra"
        };
        sbSearchAlgorithms.setItems(searchAlgorithms);

        sbSearchAlgorithms.setWidth(70f);

        final TextButton bStartAlgorithm = new TextButton("Start", skin);

        bStartAlgorithm.addListener(
                new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        tileMapInputProcessor.setInputAllowed(false);

                        switch (sbSearchAlgorithms.getSelectedIndex()) {
                            case 0:
                                attachNewAlgorithm(new AStar(manager));
                                break;
                            case 1:
                                attachNewAlgorithm(new BestFirst(manager));
                                break;
                            case 2:
                                attachNewAlgorithm(new BranchAndBound(manager));
                                break;
                            case 3:
                                attachNewAlgorithm(new backend.BreadthFirst(manager));
                                break;
                            case 4:
                                attachNewAlgorithm(new backend.DepthFirst(manager));
                                break;
                            case 5:
                                attachNewAlgorithm(new backend.Dijkstra(manager));
                                break;
                        }
                        receivedNodes.clear();
                        launchBackend();
                        algoSteps.add(receivedNodes.size());
                        System.out.println("Clicked! Is checked: " + bStartAlgorithm.isChecked());
                        bStartAlgorithm.setDisabled(true);
                        createLabel(searchAlgorithms[sbSearchAlgorithms.getSelectedIndex()]);
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
                        clearCounterLabels();
                        tileMapInputProcessor.setInputAllowed(true);
                    }
                });

        final TextButton bAutoStepAlgorithm = new TextButton("Autoplay", skin);

        bAutoStepAlgorithm.addListener(
                new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        autoStepEnabled = !autoStepEnabled;
                        System.out.println(
                                "Clicked! Is checked: " + bAutoStepAlgorithm.isChecked());
                    }
                });

        final TextButton bResetLabyrinth = new TextButton("Reset Labyrinth", skin);

        bResetLabyrinth.addListener(
                new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        map.resetLabyrinth();
                        bStartAlgorithm.setDisabled(false);
                        System.out.println("Clicked! Is checked: " + bResetLabyrinth.isChecked());
                        tileMapInputProcessor.setInputAllowed(true);
                    }
                });

        final TextButton bClearField = new TextButton("Clear", skin);

        bClearField.addListener(
                new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        map.clearField();
                        bStartAlgorithm.setDisabled(false);
                        clearCounterLabels();
                        tileMapInputProcessor.setInputAllowed(true);
                    }
                });

        table.add(bNewRandomLabyrinth);
        table.add(sbSearchAlgorithms);
        table.add(bStartAlgorithm);
        table.add(bNextStep);
        table.add(bAutoStepAlgorithm);
        table.add(bResetLabyrinth);
        table.add(bClearField);
    }


    private void setupAStarText(){
        Table container = new Table();
        stage.addActor(container);
        container.align(Align.topRight);
        container.setFillParent(true);
        container.pad(0,1050, 670, 20 );
        Table table = new Table();

        final ScrollPane scroll = new ScrollPane(table, skin);
        scroll.setScrollbarsVisible(true);
        LabelStyleGenerator labelStyleGenerator = new LabelStyleGenerator();
        ExplanationLabel explanationLabel = new ExplanationLabel(SupportedAlgorithms.ASTAR, labelStyleGenerator.generateLabelStyle(
            "font/RobotoMono-VariableFont_wght.ttf",
            Color.valueOf("#FFDCA4"),
            12));

        table.add(explanationLabel);
        container.add(scroll);
        scroll.validate();
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
        // DepthFirst a = new DepthFirst();
        RecursiveDivision a = new RecursiveDivision();
        field = a.generateLabyrinth(x, y);
        map.changeProperties(field);
    }

    /**
     * Sets up a new empty field with the passed dimensions.
     *
     * @param x
     * @param y
     */
    private void setupEmptyField(int x, int y) {
        map = new TileMap(x, y);
        field = map.getNodes();
    }

    /**
     * Renders a GUI frame and increments program runtime. Automatically visualises
     * backend-processed nodes if autoStep mode is enabled.
     *
     * @author frontend
     */
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0.1f, 0.2f, 0.3f, 1);

        manageLabelStatus();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        pfTimer.increasePfRuntime();

        if (autoStepEnabled) {
            if (!map.autoVisualiseNode()) {
                autoStepEnabled = false;
            }
        }

        if (map.isMapEdited()) {
            clearCounterLabels();
            map.setMapEdited(false);
        }
    }

    private void manageLabelStatus() {
        if (labels.size() >= 2) {
            for (int i = 0; i < labels.size() - 1; i++) {
                labels.get(i).setVisible(true);
            }
            labels.get(labels.size() - 1).setVisible(map.getProcessedNodes().isEmpty());
        }
        if (labels.size() == 1) {
            labels.get(0).setVisible(map.getProcessedNodes().isEmpty());
        }
    }

    private void clearCounterLabels() {
        algoSteps.clear();
        algoTimes.clear();
        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).remove();
        }
    }

    /**
     * Launches the backend. WIP!
     *
     * @author frontend
     */
    public void launchBackend() {
        manager.initMatrix(field);
        map.changeProperties(field);
        long startTime = System.currentTimeMillis();
        backend.run();
        long endTime = System.currentTimeMillis();
        long algoTime = (endTime - startTime);
        algoTimes.add((int) algoTime);
    }

    @Override
    public void update(Node node) {
        map.receiveNode(node);
        //        map.updateMap(node);
        addToStepCounter(node);
        render();
    }

    private void addToStepCounter(Node node) {
        if (node.getType() == NodeType.VISITED) {
            receivedNodes.add(node);
        }
    }

    /**
     * creates Label; contains name, step counter and duration of the algorithm
     *
     * @param algorithmName
     */
    private void createLabel(String algorithmName) {

        LabelStyleGenerator labelStyleGenerator = new LabelStyleGenerator();
        Label.LabelStyle labelStyle= labelStyleGenerator.generateLabelStyle("font/RobotoMono-VariableFont_wght.ttf", Color.valueOf("#FFDCA4"), 16);
        Label label = new Label(algorithmName + "\nZeit: " + algoTimes.get(algoTimes.size()-1) + "ms" + " Schritte: " + algoSteps.get(algoSteps.size()-1) + "\n", labelStyle);
        if(labels.size() >= 5) {
            counterTable.removeActor(labels.remove(0));
        }
        counterTable.add(label);
        counterTable.row();
        labels.add(label);

        label.setVisible(false);
    }
}
