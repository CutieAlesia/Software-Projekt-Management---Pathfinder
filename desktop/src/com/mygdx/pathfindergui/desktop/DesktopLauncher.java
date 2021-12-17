package com.mygdx.pathfindergui.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.pathfindergui.PathfinderGUI;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PathfinderGUI(), config);
		config.width = 1700;
		config.height = 1000;
	}
}



