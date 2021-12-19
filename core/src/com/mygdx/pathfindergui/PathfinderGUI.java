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
 * PathfinderGUI. Manages all of the GUI's components. Prepares GUI objects and places them onto the stage. 
 * 
 *
 * @author frontend
 */

public class PathfinderGUI extends ApplicationAdapter implements IFrontend{
	Stage stage;
	private TileMap map;
	private Table table;
	
	APIManager manager;
	SearchAlgorithm backend;
	Node[][] field;

	/**
	 * Sets up the stage. WIP!
	 * 
	 *
	 * @author frontend
	 */
	
	@Override
	public void create() {
		ScreenViewport viewport = new ScreenViewport();
		stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);
		DepthFirst df = new DepthFirst();
		Node[][]labyrinth = df.generateLabyrinth(10, 10);
		map = new TileMap(labyrinth);
		map.setMapUpdatableTrue();
		table= new Table();
		table.setFillParent(true);
        table.center();

		stage.addActor(table);
		table.add(map);
		Skin skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
		final TextButton b = new TextButton("Start Algorithm", skin);
		
		b.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	launchBackend();
                System.out.println("Clicked! Is checked: " + b.isChecked());
                b.setDisabled(true);
            }
        });
		table.add(b);

        manager = new APIManager();
        backend = new AStar(manager);
        manager.attachFrontend(this);
        manager.attachBackend(backend);
        int x = 13;
        int y = 13;
        DepthFirst a = new DepthFirst();
        field = a.generateLabyrinth(x, y);
        map.changeProperties(field);
	}

	/**
	 * Renders a GUI frame.
	 * 
	 *
	 * @author frontend
	 */
	
	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.1f, 0.2f, 0.3f, 1);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		
	}
	
	/**
	 * Launches the backend. WIP!
	 * 
	 *
	 * @author frontend
	 */
	
	public void launchBackend() {

        manager.initMatrix(field);
        backend.run();
	}

	@Override
	public void update(Node node) {
		map.updateMap(node);
		render();	
	}
	
}
