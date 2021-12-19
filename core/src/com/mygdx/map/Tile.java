package com.mygdx.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Tile. Each Tile represents a Node within the GUI.
 *
 * @author frontend
 */
public class Tile {

    private Texture t;
    public Vector2 tilemapPos;
    public Vector2 worldPos;

    public Vector2 getWorldPos() {
        return worldPos;
    }

    public void setWorldPos(Vector2 worldPos) {
        this.worldPos = worldPos;
    }
    /**
     * Constructor. Creates a Tile with the given Texture and given Coordinates.
     *
     * @param t
     * @param tilemapPos
     * @param worldPos
     */
    public Tile(Texture t, Vector2 tilemapPos, Vector2 worldPos) {
        this.t = t;
        this.tilemapPos = tilemapPos;
        this.worldPos = worldPos;
        //		System.out.println("TilemapPos: " + this.tilemapPos);
        //		System.out.println("WorldPos: " + this.worldPos);

    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(t, worldPos.x, worldPos.y);
    }
}
