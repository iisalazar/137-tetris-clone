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
                color = Color.CYAN;
                // this.a.setX(50); this.a.setY(0);
                // this.b.setX(75); this.b.setY(0);
                // this.c.setX(100); this.c.setY(0);
                // this.d.setX(125); this.d.setY(0);
                break;
            case "square":
                color = Color.YELLOW;
                // this.a.setX(50); this.a.setY(0);
                // this.b.setX(75); this.b.setY(0);
                // this.c.setX(50); this.c.setY(25);
                // this.d.setX(75); this.d.setY(25);
                break;
            case "t":
                color = Color.PURPLE;
                // this.a.setX(50); this.a.setY(0);
                // this.b.setX(75); this.b.setY(0);
                // this.c.setX(100); this.c.setY(0);
                // this.d.setX(75); this.d.setY(25);
                break;
            case "l":
                color = Color.ORANGE;
                // this.a.setX(50); this.a.setY(0);
                // this.b.setX(75); this.b.setY(0);
                // this.c.setX(100); this.c.setY(0);
                // this.d.setX(100); this.d.setY(25);
                break;
            case "j":
                color = Color.BLUE;
                // this.a.setX(50); this.a.setY(0);
                // this.b.setX(75); this.b.setY(0);
                // this.c.setX(100); this.c.setY(0);
                // this.d.setX(50); this.d.setY(25);
                break;
            case "s":
                color = Color.GREEN;
                // this.a.setX(50); this.a.setY(25);
                // this.b.setX(75); this.b.setY(25);
                // this.c.setX(75); this.c.setY(0);
                // this.d.setX(100); this.d.setY(0);
                break;
            case "z":
                color = Color.RED;
                // this.a.setX(50); this.a.setY(0);
                // this.b.setX(75); this.b.setY(0);
                // this.c.setX(75); this.c.setY(25);
                // this.d.setX(100); this.d.setY(25);
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
