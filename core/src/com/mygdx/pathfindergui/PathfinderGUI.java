package com.mygdx.pathfindergui;

import GUI.Generator.RecursiveDivision;
import API.Models.NodeType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
    private Skin skin;
    private Table counterTable;

    private InputMultiplexer inputMultiplexer;

    private SpriteBatch batch;
    private Texture userGuide;

    private TileMapInputProcessor tileMapInputProcessor;

    private APIManager manager;
    private SearchAlgorithm backend;
    private Node[][] field;

    private PFTimer pfTimer;

    private ArrayList<Integer> algoTimes = new ArrayList<>();
    private ArrayList<Integer> algoSteps = new ArrayList<>();
    private ArrayList<Integer> pathSteps = new ArrayList<>();
    private ArrayList<Node> receivedNodes = new ArrayList<>();
    private ArrayList<Node> pathNodes = new ArrayList<>();

    private ArrayList<Label> labels = new ArrayList<>();
    private ExplanationLabel explanationLabel;
    private TextButton bStartAlgorithm;
    private SelectBox<String> sbSearchAlgorithms;
    private int lastSelected;


    //  Toggles autoplay mode
    private boolean autoStepEnabled = false;
    // Toggles user guide
    private boolean userGuideEnabled = false;

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
        Table mapTable = new Table();
        mapTable.setFillParent(true);
        mapTable.align(Align.bottom);

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        // buttonTable.setBounds(20, -20, 20, 20);
        buttonTable.align(Align.topLeft);
        buttonTable.pad(30, 30, 30, 0);

        Table saveLoadButtonTable = new Table();
        saveLoadButtonTable.setFillParent(true);
        saveLoadButtonTable.align(Align.bottomRight);
        saveLoadButtonTable.pad(30, 30, 30 ,30);

        Table userGuideButtonTable = new Table();
        userGuideButtonTable.setFillParent(true);
        userGuideButtonTable.align(Align.bottomLeft);
        userGuideButtonTable.pad(0, 30, 30, 0);



        // Table for labels
        counterTable = new Table();
        counterTable.setFillParent(true);
        counterTable.align(Align.topLeft);



        LabelStyleGenerator labelStyleGenerator = new LabelStyleGenerator();
        Label counterHeader =
            new Label(
                "Zeit und Schritte des Algorithmus\n",
                labelStyleGenerator.generateLabelStyle(
                    "font/RobotoMono-VariableFont_wght.ttf",
                    Color.valueOf("#FFDCA4"),
                    18));
        counterTable.add(counterHeader);
        counterTable.row().align(Align.left);
        counterTable.pad(60, 30, 30, 0);
        counterHeader.setAlignment(Align.topLeft);
        //  Buttons

        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        setupPermanentButtons(buttonTable, skin);
        setupSaveLoadButtons(saveLoadButtonTable, skin);
        setupUserGuideButton(userGuideButtonTable, skin);

        // Table order
        // Add table containing the buttons before table containing the field to avoid dropdown
        // transparency issue

        stage.addActor(counterTable);
        stage.addActor(buttonTable);
        stage.addActor(userGuideButtonTable);
        stage.addActor(mapTable);
        stage.addActor(saveLoadButtonTable);

        mapTable.add(map);

        // Scrollpane with algorithm explanations
        explanationLabel =
            new ExplanationLabel(
                SupportedAlgorithms.ASTAR,
                labelStyleGenerator.generateLabelStyle(
                    "font/RobotoMono-VariableFont_wght.ttf",
                    Color.valueOf("#FFDCA4"),
                    13));
        setupExplanationText(explanationLabel);

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

        userGuide = new Texture("userguide_v2.png");
        batch = new SpriteBatch();

    }

    /**
     * Instantiates buttons for saving and loading labyrinths and adds them to the passed table.
     *
     * @param table
     * @param skin
     */
    private void setupSaveLoadButtons(Table table, final Skin skin) {

        final TextButton bSaveLabyrinth = new TextButton("Speichern", skin);
        final TextButton.TextButtonStyle defaultTextButtonStyle = bSaveLabyrinth.getStyle();

        bSaveLabyrinth.addListener(
            new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    map.resetLabyrinth();

                    saveLabyrinth(map.getNodes());
                }
            });

        final TextButton bLoadLabyrinth = new TextButton("Laden", skin);

        bLoadLabyrinth.addListener(
            new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    map.resetLabyrinth();

                    field = loadLabyrinth();

                    // calls to un-disable buttons
                    map.resetLabyrinth();
                    clearCounterLabels();
                    bStartAlgorithm.setDisabled(false);
                    resetTextButtonStyle(bStartAlgorithm, defaultTextButtonStyle);
                    map.changeProperties(field);
                }
            });


        table.add(bLoadLabyrinth);
        table.add(bSaveLabyrinth);
    }

    /**
     * takes the maze and saves it into a text file
     *
     * @param labyrinth The current maze
     */
    private void saveLabyrinth(Node[][] labyrinth) {
        if(labyrinth == null || labyrinth.length == 0 || labyrinth[0].length == 0) {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int choice = chooser.showOpenDialog(null);

        if (choice != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();

        save(labyrinth, file);
    }

    /**
     * Takes a Node[][] and saves it into a given file
     *
     * @param field The maze
     * @param file The file that the maze is supposed to be written to
     */
    private void save(Node[][] field, File file) {
        FileWriter writer;

        try {
            writer = new FileWriter(file);
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }

        for(int i=0; i<field.length; i++) {
            for(int j=0; j<field[i].length; j++) {
                Node node = field[i][j];
                int x = node.getHorIndex();
                int y = node.getVertIndex();
                NodeType type = node.getType();
                String end = j == field[i].length - 1 ? "\n" : "\t";

                if(type == NodeType.VISITED || type == NodeType.PATH) {
                    type = NodeType.NORMAL;
                }

                try {
                    writer.write("[" + x + "," + y + "," + type + "]" + end);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a filebrowser and returns a saved maze, based on the file content
     *
     * @return maze that was saved in a selected file
     */
    private Node[][] loadLabyrinth() {
        JFileChooser chooser = new JFileChooser();
        File file;

        int selection = chooser.showOpenDialog(null);

        if(selection == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        } else {
            System.out.println("Could not open file");
            return field;
        }

        Node[][] matrix = load(file);
        return matrix != null ? matrix : field;
    }

    /**
     * Takes a file, reads it and returns a maze that is created from the files content
     *
     * @return loaded maze
     * @param file File where the maze is saved
     */
    private Node[][] load(File file) {
        if(file == null) return null;

        ArrayList<Node> nodes = new ArrayList<Node>();
        int width = 0;
        int height = 0;

        Scanner scanner;

        try {
            scanner = new Scanner(file);

            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] entries = line.split("\t");

                for(String entry : entries) {
                    entry = entry.substring(1, entry.length() - 1);
                    String[] values = entry.split(",");

                    int x = Integer.parseInt(values[0]);
                    int y = Integer.parseInt(values[1]);
                    NodeType type = NodeType.valueOf(values[2]);

                    Node node = new Node(y, x);
                    node.setType(type);
                    nodes.add(node);
                }

                width = entries.length;
                height++;
            }

            scanner.close();

            Node[][] field = new Node[height][width];

            for(int i=0; i<height; i++) {
                for(int j=0; j<width; j++) {
                    field[i][j] = nodes.remove(0);
                }
            }
            return field;

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Instantiates buttons that have a permanent place on the stage and adds them to passed table.
     *
     * @param table Table to add the buttons to.
     * @param skin The skin used for the buttons.
     */
    private void setupPermanentButtons(Table table, final Skin skin) {


        sbSearchAlgorithms = new SelectBox<>(skin);


        final String[] searchAlgorithms = {
            "AStar", "BestFirst", "BranchAndBound", "BreadthFirst", "DepthFirst", "Dijkstra"
        };
        sbSearchAlgorithms.setItems(searchAlgorithms);

        sbSearchAlgorithms.setWidth(70f);
        bStartAlgorithm = new TextButton("Ausfuehren", skin);

        final TextButton.TextButtonStyle defaultTextButtonStyle = bStartAlgorithm.getStyle();

        sbSearchAlgorithms.addListener(
                new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                    	if(checklastAndCurrentSelection()) {
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

	                        resetTextButtonStyle(bStartAlgorithm, defaultTextButtonStyle);

	                        bStartAlgorithm.setDisabled(false);
                    	}
                    	else {
	                            setTextButtonStylePressed(bStartAlgorithm);
		                        bStartAlgorithm.setDisabled(true);
                    	}
                    }
                });



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
                        map.resetLabyrinth();
                        receivedNodes.clear();
                        pathNodes.clear();

                        if(autoStepEnabled) {
                        	autoStepEnabled = false;
                        }
                        lastSelected = sbSearchAlgorithms.getSelectedIndex();
                        launchBackend();
                        algoSteps.add(receivedNodes.size());
                        pathSteps.add(pathNodes.size());
                        System.out.println("Clicked! Is checked: " + bStartAlgorithm.isChecked());
                        bStartAlgorithm.setDisabled(true);

                        setTextButtonStylePressed(bStartAlgorithm);

                        createLabel(searchAlgorithms[sbSearchAlgorithms.getSelectedIndex()]);
                    }
                });

        final TextButton bNextStep = new TextButton("Weiter", skin);

        bNextStep.addListener(
            new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    map.visualiseNode();
                    System.out.println("Clicked! Is checked: " + bNextStep.isChecked());
                }
            });

        final TextButton bNewRandomLabyrinth = new TextButton("Zufallslabyrinth", skin);


        bNewRandomLabyrinth.addListener(
                new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        if (pfTimer.getPfRuntime() % 2 == 0) {
                            setupNewLabyrinthDepthFirst(MAP_X, MAP_Y);
                        } else {
                            setupNewLabyrinthRecursiveDivision(MAP_X, MAP_Y);
                        }
                        bStartAlgorithm.setDisabled(false);
                        resetTextButtonStyle(bStartAlgorithm, defaultTextButtonStyle);
                        System.out.println("Clicked! Is checked: " + bNextStep.isChecked());
                        clearCounterLabels();
                        lastSelected = -1;
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

        final TextButton bResetLabyrinth = new TextButton("Editieren", skin);

        bResetLabyrinth.addListener(
                new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        map.resetLabyrinth();
                        bStartAlgorithm.setDisabled(false);
                        resetTextButtonStyle(bStartAlgorithm, defaultTextButtonStyle);
                        System.out.println("Clicked! Is checked: " + bResetLabyrinth.isChecked());
                        lastSelected = -1;
                        tileMapInputProcessor.setInputAllowed(true);
                    }
                });

        final TextButton bClearField = new TextButton("Leeres Labyrinth", skin);


        bClearField.addListener(
                new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        map.clearField();
                        bStartAlgorithm.setDisabled(false);
                        resetTextButtonStyle(bStartAlgorithm, defaultTextButtonStyle);
                        clearCounterLabels();
                        lastSelected = -1;
                        tileMapInputProcessor.setInputAllowed(true);
                    }
                });

        Table generateLabyrinthButtonTable = new Table();
        generateLabyrinthButtonTable.add(bNewRandomLabyrinth);
        table.add(bClearField);
        //        generateLabyrinthButtonTable.row();
        //        generateLabyrinthButtonTable.add(bNewRandomLabyrinthRecursiveDivision);
        table.add(generateLabyrinthButtonTable);
        table.add(sbSearchAlgorithms);
        table.add(bStartAlgorithm);
        table.add(bNextStep);
        table.add(bAutoStepAlgorithm);
        table.add(bResetLabyrinth);
    }


 /**
  * Checks if the selected index changed to a different index.
  * @return
  */

    private boolean checklastAndCurrentSelection() {
    	if(sbSearchAlgorithms.getSelectedIndex() != lastSelected) {
    		return true;
    	}
    	return false;
    }

    /**
     * Instantiates a button to toggle the user guide overlay and adds them to the given table.
     *
     * @param table
     * @param skin
     */
    private void setupUserGuideButton(Table table, final Skin skin) {
        final TextButton bToggleUserGuide = new TextButton("Hilfe", skin);
        final TextButton.TextButtonStyle defaultTextButtonStyle = bToggleUserGuide.getStyle();

        bToggleUserGuide.addListener(
            new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    userGuideEnabled = !userGuideEnabled;
                    if(userGuideEnabled) {
                        setTextButtonStylePressed(bToggleUserGuide);
                    } else {
                        resetTextButtonStyle(bToggleUserGuide, defaultTextButtonStyle);
                    }
                }
            });
        table.add(bToggleUserGuide);
    }

    /**
     * sets TextButtonStyle to 'pressed'
     * @param button
     */
    private void setTextButtonStylePressed(TextButton button){
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(button.getStyle().down, button.getStyle().down, button.getStyle().down, button.getStyle().font);
        textButtonStyle.fontColor = Color.BLACK;
        button.setStyle(new TextButton.TextButtonStyle(textButtonStyle));
    }

    /**
     * resets the TextButtonStyle of the changed button
     * @param changedButton
     * @param defaultStyle
     */
    private void resetTextButtonStyle(TextButton changedButton, TextButton.TextButtonStyle defaultStyle){
        changedButton.setStyle(defaultStyle);
    }

    /**
     * Sets up the scrollpane showing explanation texts. Call
     * explanationLabel.selectAlgorithmDescription() during runtime to switch to the given supported
     * algorithm's explanation.
     *
     * @param explanationLabel
     */
    private void setupExplanationText(ExplanationLabel explanationLabel) {
        Table container = new Table();
        stage.addActor(container);
        container.align(Align.topRight);
        container.setFillParent(true);
        container.pad(0, 980, 740, 0);
        Table table = new Table();

        final ScrollPane scroll = new ScrollPane(table, skin);
        scroll.setScrollbarsVisible(true);

        table.add(explanationLabel);
        table.padBottom(10);
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
    private void setupNewLabyrinthDepthFirst(int x, int y) {
        GUI.Generator.DepthFirst a = new GUI.Generator.DepthFirst();
        field = a.generateLabyrinth(x, y);
        map.changeProperties(field);
    }

    /**
     * Sets up a new randomly generated labyrinth with the passed dimensions.
     *
     * @param x
     * @param y
     */
    private void setupNewLabyrinthRecursiveDivision(int x, int y) {
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

        updateExplanationLabel();

        // Draw user guide overlay
        batch.begin();
        if (userGuideEnabled) {
            batch.draw(userGuide, 0, 0);
        }
        batch.end();

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
        pathSteps.clear();
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
        map.visualiseNode();
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
        if (node.getType() == NodeType.PATH) {
            pathNodes.add(node);
        }
    }

    /**
     * creates Label; contains name, step counter and duration of the algorithm
     *
     * @param algorithmName
     */
    private void createLabel(String algorithmName) {
        LabelStyleGenerator labelStyleGenerator = new LabelStyleGenerator();
        Label.LabelStyle labelStyle =
            labelStyleGenerator.generateLabelStyle(
                "font/RobotoMono-VariableFont_wght.ttf", Color.valueOf("#FFDCA4"), 15);
        Label label =
            new Label(
                algorithmName
                    + "\nZeit: "
                    + algoTimes.get(algoTimes.size() - 1)
                    + "ms"
                    + " Schritte: "
                    + algoSteps.get(algoSteps.size() - 1)
                    + " Zielpfad: "
                    + pathSteps.get(pathSteps.size() - 1)
                    + "\n",
                labelStyle);
        if (labels.size() >= 6) {
            counterTable.removeActor(labels.remove(0));
        }

        counterTable.add(label);
        counterTable.row().align(Align.left);
        labels.add(label);

        label.setVisible(false);
    }

    private void updateExplanationLabel() {
        switch (sbSearchAlgorithms.getSelectedIndex()) {
            case 0:
                explanationLabel.selectAlgorithmDescription(SupportedAlgorithms.ASTAR);
                break;
            case 1:
                explanationLabel.selectAlgorithmDescription(SupportedAlgorithms.BESTFIRST);
                break;
            case 2:
                explanationLabel.selectAlgorithmDescription(SupportedAlgorithms.BRANCHANDBOUND);
                break;
            case 3:
                explanationLabel.selectAlgorithmDescription(SupportedAlgorithms.BREADTHFIRST);
                break;
            case 4:
                explanationLabel.selectAlgorithmDescription(SupportedAlgorithms.DEPTHFIRST);
                break;
            case 5:
                explanationLabel.selectAlgorithmDescription(SupportedAlgorithms.DIJKSTRA);
                break;
        }
    }
}
