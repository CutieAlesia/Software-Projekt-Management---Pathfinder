package com.mygdx.pathfindergui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelStyleGenerator {

    public Label.LabelStyle generateLabelStyle(String fontPath, Color color, int fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        parameter.borderWidth = 0;
        parameter.padTop = 20;
        parameter.color = color;
        // parameter.padRight = 80;
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        return labelStyle;
    }
}
