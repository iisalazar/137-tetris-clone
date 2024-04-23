package com.tetris137;

import javafx.scene.shape.Rectangle;

public class Controller {
    public final static int MOVE = Tetris.MOVE;
    public final static int SIZE = Tetris.SIZE;
    public final static int WIDTH = Tetris.WIDTH;
    public final static int HEIGHT = Tetris.HEIGHT;
    public final static int[][] grid = Tetris.grid;

    public static void moveRight(Form form) {
        if (form.a.getX() + MOVE <= WIDTH - SIZE && form.b.getX() + MOVE <= WIDTH - SIZE &&
                form.c.getX() + MOVE <= WIDTH - SIZE && form.d.getX() + MOVE <= WIDTH - SIZE) {
            int moveA = grid[(int) form.a.getX() / SIZE + 1][(int) form.a.getY() / SIZE];
            int moveB = grid[(int) form.b.getX() / SIZE + 1][(int) form.b.getY() / SIZE];
            int moveC = grid[(int) form.c.getX() / SIZE + 1][(int) form.c.getY() / SIZE];
            int moveD = grid[(int) form.d.getX() / SIZE + 1][(int) form.d.getY() / SIZE];
            if (moveA == 0 && moveB == 0 && moveC == 0 && moveD == 0) {
                form.a.setX(form.a.getX() + MOVE);
                form.b.setX(form.b.getX() + MOVE);
                form.c.setX(form.c.getX() + MOVE);
                form.d.setX(form.d.getX() + MOVE);
            }
        }
    }

    public static void moveLeft(Form form) {
        if (form.a.getX() - MOVE >= 0 && form.b.getX() - MOVE >= 0 &&
                form.c.getX() - MOVE >= 0 && form.d.getX() - MOVE >= 0) {
            int moveA = grid[(int) form.a.getX() / SIZE - 1][(int) form.a.getY() / SIZE];
            int moveB = grid[(int) form.b.getX() / SIZE - 1][(int) form.b.getY() / SIZE];
            int moveC = grid[(int) form.c.getX() / SIZE - 1][(int) form.c.getY() / SIZE];
            int moveD = grid[(int) form.d.getX() / SIZE - 1][(int) form.d.getY() / SIZE];
            if (moveA == 0 && moveB == 0 && moveC == 0 && moveD == 0) {
                form.a.setX(form.a.getX() - MOVE);
                form.b.setX(form.b.getX() - MOVE);
                form.c.setX(form.c.getX() - MOVE);
                form.d.setX(form.d.getX() - MOVE);
            }
        }
    }

    // public static void moveDown(Form form) {
    // if (form.a.getY() + MOVE <= HEIGHT - SIZE && form.b.getY() + MOVE <= HEIGHT -
    // SIZE &&
    // form.c.getY() + MOVE <= HEIGHT - SIZE && form.d.getY() + MOVE <= HEIGHT -
    // SIZE) {
    // int moveA = grid[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE + 1];
    // int moveB = grid[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE + 1];
    // int moveC = grid[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE + 1];
    // int moveD = grid[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE + 1];
    // if (moveA == 0 && moveB == 0 && moveC == 0 && moveD == 0) {
    // form.a.setY(form.a.getY() + MOVE);
    // form.b.setY(form.b.getY() + MOVE);
    // form.c.setY(form.c.getY() + MOVE);
    // form.d.setY(form.d.getY() + MOVE);
    // }
    // }
    // }

    public static Form makeRect() {
        int block = (int) (Math.random() * 7);
        String name;

        Rectangle a = new Rectangle(Tetris.SIZE - 1, Tetris.SIZE - 1),
                b = new Rectangle(Tetris.SIZE - 1, Tetris.SIZE - 1),
                c = new Rectangle(Tetris.SIZE - 1, Tetris.SIZE - 1),
                d = new Rectangle(Tetris.SIZE - 1, Tetris.SIZE - 1);

        switch (block) {
            case 0:
                name = "line";
                return new Form(a, b, c, d, name);
            case 1:
                name = "square";
                return new Form(a, b, c, d, name);
            case 2:
                name = "t";
                return new Form(a, b, c, d, name);
            case 3:
                name = "l";
                return new Form(a, b, c, d, name);
            case 4:
                name = "j";
                return new Form(a, b, c, d, name);
            case 5:
                name = "s";
                return new Form(a, b, c, d, name);
            case 6:
                name = "z";
                return new Form(a, b, c, d, name);
        }

        return new Form(a, b, c, d);

    }
}
