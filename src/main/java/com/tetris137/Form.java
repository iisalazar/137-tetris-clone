package com.tetris137;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Form {
    Rectangle a, b, c, d;
    Color color;
    private String name;
    public int form = 1;

    @SuppressWarnings("exports")
    public Form(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @SuppressWarnings("exports")
    public Form(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String name) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;

        switch (name) {
            case "line":
                color = Color.BLUE;
                break;
            case "square":
                color = Color.YELLOW;
                break;
            case "t":
                color = Color.PURPLE;
                break;
            case "l":
                color = Color.ORANGE;
                break;
            case "j":
                color = Color.BLUE;
                break;
            case "s":
                color = Color.GREEN;
                break;
            case "z":
                color = Color.RED;
                break;
        }

        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }

    public String getName() {
        return name;
    }

    public void changeForm() {
        if (form < 4) {
            form++;
        } else {
            form = 1;
        }
    }
}
