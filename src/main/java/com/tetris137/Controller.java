package com.tetris137;

import javafx.scene.shape.Rectangle;

public class Controller {
    public static final int MOVE = TetrisClient.MOVE;
    public static final int SIZE = TetrisClient.SIZE;
    public static int WIDTH = TetrisClient.WIDTH;
    public static int HEIGHT = TetrisClient.HEIGHT;
    public static int[][] grid = TetrisClient.grid;

    public static void moveRight(Form form) {
        if (form.a.getX() + MOVE <= WIDTH - SIZE && form.b.getX() + MOVE <= WIDTH - SIZE &&
                form.c.getX() + MOVE <= WIDTH - SIZE && form.d.getX() + MOVE <= WIDTH - SIZE) {
            int moveA = grid[((int) form.a.getX() / SIZE) + 1][((int) form.a.getY() / SIZE)];
            int moveB = grid[((int) form.b.getX() / SIZE) + 1][((int) form.b.getY() / SIZE)];
            int moveC = grid[((int) form.c.getX() / SIZE) + 1][((int) form.c.getY() / SIZE)];
            int moveD = grid[((int) form.d.getX() / SIZE) + 1][((int) form.d.getY() / SIZE)];
            if (moveA == 0 && moveA == moveB && moveB == moveC && moveC == moveD) {
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
            int moveA = grid[((int) form.a.getX() / SIZE) - 1][((int) form.a.getY() / SIZE)];
            int moveB = grid[((int) form.b.getX() / SIZE) - 1][((int) form.b.getY() / SIZE)];
            int moveC = grid[((int) form.c.getX() / SIZE) - 1][((int) form.c.getY() / SIZE)];
            int moveD = grid[((int) form.d.getX() / SIZE) - 1][((int) form.d.getY() / SIZE)];

            if (moveA == 0 && moveA == moveB && moveB == moveC && moveC == moveD) {
                form.a.setX(form.a.getX() - MOVE);
                form.b.setX(form.b.getX() - MOVE);
                form.c.setX(form.c.getX() - MOVE);
                form.d.setX(form.d.getX() - MOVE);
            }
        }
    }

    public static Form makeRect() {
        int block = (int) (Math.random() * 7);
        String name;

        Rectangle a = new Rectangle(SIZE - 1, SIZE - 1),
                b = new Rectangle(SIZE - 1, SIZE - 1),
                c = new Rectangle(SIZE - 1, SIZE - 1),
                d = new Rectangle(SIZE - 1, SIZE - 1);

        switch (block) {
            case 0:
                a.setX(WIDTH / 2 - (SIZE * 2));
                b.setX(WIDTH / 2 - SIZE);
                c.setX(WIDTH / 2);
                d.setX(WIDTH / 2 + SIZE);
                name = "line";
                return new Form(a, b, c, d, name);
            case 1:
                a.setX(WIDTH / 2 - SIZE);
                b.setX(WIDTH / 2);
                c.setX(WIDTH / 2 - SIZE);
                c.setY(SIZE);
                d.setX(WIDTH / 2);
                d.setY(SIZE);
                name = "square";
                return new Form(a, b, c, d, name);
            case 2:
                a.setX(WIDTH / 2 - SIZE);
                b.setX(WIDTH / 2);
                c.setX(WIDTH / 2);
                c.setY(SIZE);
                d.setX(WIDTH / 2 + SIZE);
                name = "t";
                return new Form(a, b, c, d, name);
            case 3:
                a.setX(WIDTH / 2 + SIZE);
                b.setX(WIDTH / 2 - SIZE);
                b.setY(SIZE);
                c.setX(WIDTH / 2);
                c.setY(SIZE);
                d.setX(WIDTH / 2 + SIZE);
                d.setY(SIZE);
                name = "l";
                return new Form(a, b, c, d, name);
            case 4:
                a.setX(WIDTH / 2 - SIZE);
                b.setX(WIDTH / 2 - SIZE);
                b.setY(SIZE);
                c.setX(WIDTH / 2);
                c.setY(SIZE);
                d.setX(WIDTH / 2 + SIZE);
                d.setY(SIZE);
                name = "j";
                return new Form(a, b, c, d, name);
            case 5:
                a.setX(WIDTH / 2 + SIZE);
                b.setX(WIDTH / 2);
                c.setX(WIDTH / 2);
                c.setY(SIZE);
                d.setX(WIDTH / 2 - SIZE);
                d.setY(SIZE);
                name = "s";
                return new Form(a, b, c, d, name);
            case 6:
                a.setX(WIDTH / 2 + SIZE);
                b.setX(WIDTH / 2);
                c.setX(WIDTH / 2 + SIZE);
                c.setY(SIZE);
                d.setX(WIDTH / 2 + (SIZE * 2));
                d.setY(SIZE);
                name = "z";
                return new Form(a, b, c, d, name);
        }

        return new Form(a, b, c, d);

    }
}

