package com.tetris137;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
// import com.tetris137.Controller;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.css.Size;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TetrisClient extends Application {
    public static final int MOVE = 25;
    public static final int SIZE = 25;
    public static final int SIZE_OTHER = 10;
    public static final int WIDTH = SIZE * 12;
    public static final int HEIGHT = SIZE * 24;
    public static int[][] grid = new int[WIDTH / SIZE][HEIGHT / SIZE];
    public static Form object;
    @SuppressWarnings("exports")
    public static Pane mainPane = new Pane();
    public static Pane group = new Pane();
    @SuppressWarnings("exports")
    public static Scene scn = new Scene(mainPane, WIDTH + 800, HEIGHT);
    public static int score = 0;
    public static int top = 0;
    public static boolean game = true;
    public static Form next = new Controller().makeRect();

    private static int nLines = 0;

    // chatbox
    private static final TextField inputBox = new TextField();
    private static VBox scoreBox = new VBox(20);
    private static VBox chatbox = new VBox(20);

    // dummyGames
    private Pane otherGroup = new Pane();


    @Override
    public void start(Stage stage) throws Exception {
        // init: get usernames of all users
        for (int a[] : grid) {
            Arrays.fill(a, 0);
        }

        Line line = new Line(WIDTH, 0, WIDTH, HEIGHT);
        Text scoreText = new Text(WIDTH + 25, 25, "Score: " + score);
        scoreText.setStyle("-fx-font: 24 arial;");
        scoreText.setX(WIDTH + 25);
        scoreText.setY(50);
        Text linesText = new Text(WIDTH + 25, 100, "Lines: " + nLines);
        linesText.setStyle("-fx-font: 24 arial;");
        linesText.setX(WIDTH + 25);
        linesText.setY(70);

        group.getChildren().addAll(line, scoreText, linesText);

        Form a = next;
        group.getChildren().addAll(a.a, a.b, a.c, a.d);
        moveOnKeyPress(a);
        object = a;
        next = new Controller().makeRect();

        // chatbox ------------ 
        UserData ud = UserData.getInstance();

        chatbox.setTranslateX(WIDTH +10);
        chatbox.setTranslateY(300);
        chatbox.setPrefWidth(200);
        
        scoreBox.setTranslateX(WIDTH + 10);
        scoreBox.setTranslateY(90);
        scoreBox.setPrefWidth(200);
        scoreBox.setDisable(true);
        scoreBox.getChildren().addAll(ud.getScoreArea());

        inputBox.setPrefWidth(200);
        inputBox.setPromptText("Press Tab to Chat");

        inputBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String temp = "msg;" + ud.getUserName() + ": " + inputBox.getText(); // message to send// update messages on screen
                byte[] msg = temp.getBytes(); // convert to bytes
                inputBox.setText(""); // remove text from input box
                inputBox.setPromptText("Press Tab to Chat");


                // create a packet & send
                DatagramPacket send = new DatagramPacket(msg, msg.length, ud.getInetAddress(), ud.getServerPort());
                try {
                    ud.getSocket().send(send);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                group.requestFocus();

            }
        });
        chatbox.getChildren().addAll(ud.getMessageArea(), inputBox);
        mainPane.getChildren().addAll(chatbox, group, scoreBox, otherGroup);

        group.setOnMouseClicked(event -> group.requestFocus());
        inputBox.setOnMouseClicked(event -> inputBox.requestFocus()); // currently not working
        // --------- chatbox

        group.requestFocus();

        stage.setScene(scn);
        stage.setTitle("Tetris");
        stage.show();

        Timer fall = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable(){
                    public void run() {
                        if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0
                                || object.d.getY() == 0)
                            top++;
                        else
                            top = 0;

                        if (top == 2) {
                            Text over = new Text(WIDTH + 25, 200, "Game Over");
                            over.setStyle("-fx-font: 24 arial;");
                            over.setX(10);
                            over.setY(250);
                            group.getChildren().add(over);
                            game = false;
                        }

                        if (top == 15) {
                            System.exit(0);
                        }

                        if (game) {
                            group.setOnMouseClicked(event -> group.requestFocus());
                            moveDown(object);
                            otherGroup.getChildren().clear();
                            try {
                                getBinary();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            setOtherGroup();
                            scoreText.setText("Score: " + score);
                            linesText.setText("Lines: " + nLines);
                        }

                    }
                });
            }
        };
        fall.schedule(task, 0, 300);
    }
    private void updateScore(int count){
        score += count;
        UserData ud = UserData.getInstance();
        // emit event
        String temp = "event:gameStateUpdated;" +
                ud.getUserName() + ";" +
                "score:" +
                score + ";";
        byte[] msg = temp.getBytes(); // convert to bytes
        // create a packet & send
        DatagramPacket send = new DatagramPacket(msg, msg.length, ud.getInetAddress(), ud.getServerPort());
        try {
            ud.getSocket().send(send);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void moveOnKeyPress(Form form) {
        scn.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case RIGHT:
                        new Controller().moveRight(form);
                        break;
                    case LEFT:
                        new Controller().moveLeft(form);
                        break;
                    case DOWN:
                        moveDown(form);
                        updateScore(1);
                        break;
                    case UP:
                        rotate(form);
                        break;
                }
            }
        });
    }

    private void rotate(Form form) {
        int f = form.form;
        Rectangle a = form.a;
        Rectangle b = form.b;
        Rectangle c = form.c;
        Rectangle d = form.d;
        switch (form.getName()) {
            case "line":
                if (f == 1) {
                    if (a.getX() + SIZE * 3 <= WIDTH && grid[(int) a.getX() / SIZE + 3][(int) a.getY() / SIZE] == 0) {
                        b.setX(a.getX() + SIZE);
                        b.setY(a.getY());
                        c.setX(a.getX() + SIZE * 2);
                        c.setY(a.getY());
                        d.setX(a.getX() + SIZE * 3);
                        d.setY(a.getY());
                        form.form++;
                    }
                } else {
                    if (a.getY() + SIZE * 3 <= HEIGHT && grid[(int) a.getX() / SIZE][(int) a.getY() / SIZE + 3] == 0) {
                        b.setX(a.getX());
                        b.setY(a.getY() + SIZE);
                        c.setX(a.getX());
                        c.setY(a.getY() + SIZE * 2);
                        d.setX(a.getX());
                        d.setY(a.getY() + SIZE * 3);
                        form.form = 1;
                    }
                }
                break;
            case "square":
                break;
            case "t":
                if (f == 1 && kickCheck(a, 1, 1) && kickCheck(d, -1, -1) && kickCheck(c, -1, 1)) {
                    moveUp(form.a);
                    moveRight(form.a);
                    moveDown(form.d);
                    moveLeft(form.d);
                    moveLeft(form.c);
                    moveUp(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 2 && kickCheck(a, 1, -1) && kickCheck(d, -1, 1) && kickCheck(c, 1, 1)) {
                    moveRight(form.a);
                    moveDown(form.a);
                    moveLeft(form.d);
                    moveUp(form.d);
                    moveUp(form.c);
                    moveRight(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 3 && kickCheck(a, -1, -1) && kickCheck(d, 1, 1) && kickCheck(c, 1, -1)) {
                    moveDown(form.a);
                    moveLeft(form.a);
                    moveUp(form.d);
                    moveRight(form.d);
                    moveRight(form.c);
                    moveDown(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 4 && kickCheck(a, -1, 1) && kickCheck(d, 1, -1) && kickCheck(c, -1, -1)) {
                    moveLeft(form.a);
                    moveUp(form.a);
                    moveRight(form.d);
                    moveDown(form.d);
                    moveDown(form.c);
                    moveLeft(form.c);
                    form.changeForm();
                    break;
                }
                break;
            case "l":
                if (f == 1 && kickCheck(a, 1, -1) && kickCheck(c, 1, 1) && kickCheck(b, 2, 2)) {
                    moveRight(form.a);
                    moveDown(form.a);
                    moveUp(form.c);
                    moveRight(form.c);
                    moveUp(form.b);
                    moveUp(form.b);
                    moveRight(form.b);
                    moveRight(form.b);
                    form.changeForm();
                    break;
                }
                if (f == 2 && kickCheck(a, -1, -1) && kickCheck(b, 2, -2) && kickCheck(c, 1, -1)) {
                    moveDown(form.a);
                    moveLeft(form.a);
                    moveRight(form.b);
                    moveRight(form.b);
                    moveDown(form.b);
                    moveDown(form.b);
                    moveRight(form.c);
                    moveDown(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 3 && kickCheck(a, -1, 1) && kickCheck(c, -1, -1) && kickCheck(b, -2, -2)) {
                    moveLeft(form.a);
                    moveUp(form.a);
                    moveDown(form.c);
                    moveLeft(form.c);
                    moveDown(form.b);
                    moveDown(form.b);
                    moveLeft(form.b);
                    moveLeft(form.b);
                    form.changeForm();
                    break;
                }
                if (f == 4 && kickCheck(a, 1, 1) && kickCheck(b, -2, 2) && kickCheck(c, -1, 1)) {
                    moveUp(form.a);
                    moveRight(form.a);
                    moveLeft(form.b);
                    moveLeft(form.b);
                    moveUp(form.b);
                    moveUp(form.b);
                    moveLeft(form.c);
                    moveUp(form.c);
                    form.changeForm();
                    break;
                }
                break;
            case "j":
                if (f == 1 && kickCheck(a, 1, -1) && kickCheck(c, -1, -1) && kickCheck(d, -2, -2)) {
                    moveRight(form.a);
                    moveDown(form.a);
                    moveDown(form.c);
                    moveLeft(form.c);
                    moveDown(form.d);
                    moveDown(form.d);
                    moveLeft(form.d);
                    moveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && kickCheck(a, -1, -1) && kickCheck(c, -1, 1) && kickCheck(d, -2, 2)) {
                    moveDown(form.a);
                    moveLeft(form.a);
                    moveLeft(form.c);
                    moveUp(form.c);
                    moveLeft(form.d);
                    moveLeft(form.d);
                    moveUp(form.d);
                    moveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && kickCheck(a, -1, 1) && kickCheck(c, 1, 1) && kickCheck(d, 2, 2)) {
                    moveLeft(form.a);
                    moveUp(form.a);
                    moveUp(form.c);
                    moveRight(form.c);
                    moveUp(form.d);
                    moveUp(form.d);
                    moveRight(form.d);
                    moveRight(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && kickCheck(a, 1, 1) && kickCheck(c, 1, -1) && kickCheck(d, 2, -2)) {
                    moveUp(form.a);
                    moveRight(form.a);
                    moveRight(form.c);
                    moveDown(form.c);
                    moveRight(form.d);
                    moveRight(form.d);
                    moveDown(form.d);
                    moveDown(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "s":
                if (f == 1 && kickCheck(a, -1, -1) && kickCheck(c, -1, 1) && kickCheck(d, 0, 2)) {
                    moveDown(form.a);
                    moveLeft(form.a);
                    moveLeft(form.c);
                    moveUp(form.c);
                    moveUp(form.d);
                    moveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && kickCheck(a, 1, 1) && kickCheck(c, 1, -1) && kickCheck(d, 0, -2)) {
                    moveUp(form.a);
                    moveRight(form.a);
                    moveRight(form.c);
                    moveDown(form.c);
                    moveDown(form.d);
                    moveDown(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && kickCheck(a, -1, -1) && kickCheck(c, -1, 1) && kickCheck(d, 0, 2)) {
                    moveDown(form.a);
                    moveLeft(form.a);
                    moveLeft(form.c);
                    moveUp(form.c);
                    moveUp(form.d);
                    moveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && kickCheck(a, 1, 1) && kickCheck(c, 1, -1) && kickCheck(d, 0, -2)) {
                    moveUp(form.a);
                    moveRight(form.a);
                    moveRight(form.c);
                    moveDown(form.c);
                    moveDown(form.d);
                    moveDown(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "z":
                if (f == 1 && kickCheck(b, 1, 1) && kickCheck(c, -1, 1) && kickCheck(d, -2, 0)) {
                    moveUp(form.b);
                    moveRight(form.b);
                    moveLeft(form.c);
                    moveUp(form.c);
                    moveLeft(form.d);
                    moveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && kickCheck(b, -1, -1) && kickCheck(c, 1, -1) && kickCheck(d, 2, 0)) {
                    moveDown(form.b);
                    moveLeft(form.b);
                    moveRight(form.c);
                    moveDown(form.c);
                    moveRight(form.d);
                    moveRight(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && kickCheck(b, 1, 1) && kickCheck(c, -1, 1) && kickCheck(d, -2, 0)) {
                    moveUp(form.b);
                    moveRight(form.b);
                    moveLeft(form.c);
                    moveUp(form.c);
                    moveLeft(form.d);
                    moveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && kickCheck(b, -1, -1) && kickCheck(c, 1, -1) && kickCheck(d, 2, 0)) {
                    moveDown(form.b);
                    moveLeft(form.b);
                    moveRight(form.c);
                    moveDown(form.c);
                    moveRight(form.d);
                    moveRight(form.d);
                    form.changeForm();
                    break;
                }
                break;
        }
    }

    private boolean kickCheck(Rectangle rect, int x, int y) {
        boolean xb = false;
        boolean yb = false;

        if (x >= 0) {
            xb = rect.getX() + x * MOVE <= WIDTH - SIZE;
        }
        if (x < 0) {
            xb = rect.getX() + x * MOVE >= 0;
        }

        if (y >= 0) {
            yb = rect.getY() + y * MOVE >= 0;
        }
        if (y < 0) {
            yb = rect.getY() + y * MOVE < HEIGHT;
        }

        return xb && yb && grid[(int) rect.getX() / SIZE + x][(int) rect.getY() / SIZE - y] == 0;
    }

    private void moveDown(Form form) {
        if (form.a.getY() == HEIGHT - SIZE || form.b.getY() == HEIGHT - SIZE || form.c.getY() == HEIGHT - SIZE
                || form.d.getY() == HEIGHT - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {
            grid[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
            grid[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
            grid[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
            grid[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
            clearLines(group);

            Form a = next;
            next = Controller.makeRect();
            object = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d);
            moveOnKeyPress(a);
        }

        if (form.a.getY() + MOVE < HEIGHT && form.b.getY() + MOVE < HEIGHT && form.c.getY() + MOVE < HEIGHT
                && form.d.getY() + MOVE < HEIGHT) {
            int movea = grid[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1];
            int moveb = grid[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1];
            int movec = grid[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1];
            int moved = grid[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1];
            if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
                form.a.setY(form.a.getY() + MOVE);
                form.b.setY(form.b.getY() + MOVE);
                form.c.setY(form.c.getY() + MOVE);
                form.d.setY(form.d.getY() + MOVE);
            }
        }
    }

    private void moveDown(Rectangle rect) {
        if (rect.getY() + MOVE < HEIGHT)
            rect.setY(rect.getY() + MOVE);

    }

    private void moveRight(Rectangle rect) {
        if (rect.getX() + MOVE <= WIDTH - SIZE)
            rect.setX(rect.getX() + MOVE);
    }

    private void moveLeft(Rectangle rect) {
        if (rect.getX() - MOVE >= 0)
            rect.setX(rect.getX() - MOVE);
    }

    private void moveUp(Rectangle rect) {
        if (rect.getY() - MOVE > 0)
            rect.setY(rect.getY() - MOVE);
    }

    private void MoveDown(Form form) {
        if (form.a.getY() == HEIGHT - SIZE || form.b.getY() == HEIGHT - SIZE || form.c.getY() == HEIGHT - SIZE
                || form.d.getY() == HEIGHT - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {
            grid[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
            grid[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
            grid[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
            grid[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
            clearLines(group);

            Form a = next;
            next = Controller.makeRect();
            object = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d);
            moveOnKeyPress(a);
        }

        if (form.a.getY() + MOVE < HEIGHT && form.b.getY() + MOVE < HEIGHT && form.c.getY() + MOVE < HEIGHT
                && form.d.getY() + MOVE < HEIGHT) {
            int movea = grid[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1];
            int moveb = grid[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1];
            int movec = grid[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1];
            int moved = grid[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1];
            if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
                form.a.setY(form.a.getY() + MOVE);
                form.b.setY(form.b.getY() + MOVE);
                form.c.setY(form.c.getY() + MOVE);
                form.d.setY(form.d.getY() + MOVE);
            }
        }
    }

    private boolean moveA(Form form) {
        return (grid[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1] == 1);
    }

    private boolean moveB(Form form) {
        return (grid[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1] == 1);
    }

    private boolean moveC(Form form) {
        return (grid[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1] == 1);
    }

    private boolean moveD(Form form) {
        return (grid[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1] == 1);
    }

    private void clearLines(Pane pane) {
        ArrayList<Node> rects = new ArrayList<Node>();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Node> newrects = new ArrayList<Node>();
        int full = 0;
        for (int i = 0; i < grid[0].length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[j][i] == 1)
                    full++;
            }
            if (full == grid.length)
                lines.add(i);
            // lines.add(i + lines.size());
            full = 0;
        }
        if (lines.size() > 0)
            do {
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                updateScore(50);
                nLines++;

                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        grid[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node : newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        grid[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        grid[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
                rects.clear();
            } while (lines.size() > 0);
    }

    private void getBinary() throws IOException {
        UserData ud = UserData.getInstance();
        String binary = "0".repeat(288);
        char[] charB = binary.toCharArray();
        double x;
        double y;
        
        int xCount = 0;
        int yCount = 0;

        for (javafx.scene.Node node : group.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle rect = (Rectangle) node;
                x = rect.getX();
                y = rect.getY();

                double xCoor = Math.round(x / SIZE);
                double yCoor = Math.round(y / SIZE);

                double index = yCoor * 12 + xCoor;

                charB[(int)index] = '1';
            }
        }

        binary = String.valueOf(charB);

        binary = binary.substring(36, binary.length());

        // send the gamestate to server
        binary = ud.getUID() + ";" + binary;
        byte[] binaryB = binary.getBytes();

        DatagramPacket gameState = new DatagramPacket(binaryB, binaryB.length, ud.getInetAddress(), ud.getServerPort());
        ud.getSocket().send(gameState);
    }

    private void setOtherGroup() {
        otherGroup.getChildren().clear();
        UserData ud = UserData.getInstance();

        if (ud.p0.equals("")) {
            ;
        } else {
            int xCount0 = 0;
            int yCount0 = 0;
            char[] charB0 = ud.p0.toCharArray();

            for (int i = 0; i < 252; i++) {
                if (xCount0 == 12) {
                    xCount0 = 0;
                    yCount0++;
                } 

                if (charB0[i] == '1') {
                    Rectangle rect = new Rectangle(SIZE_OTHER - 1, SIZE_OTHER -1);
                    rect.setX(600 + SIZE_OTHER * xCount0);
                    rect.setY(SIZE_OTHER * yCount0);
                    rect.setFill(Color.RED);
                    otherGroup.getChildren().add(rect);
                }  
                xCount0++;  
            }
        }

        if (ud.p1.equals("")) {
            ;
        } else {
            int xCount1 = 0;
            int yCount1 = 0;
            char[] charB1 = ud.p1.toCharArray();

            for (int i = 0; i < 252; i++) {
                if (xCount1 == 12) {
                    xCount1 = 0;
                    yCount1++;
                } 

                if (charB1[i] == '1') {
                    Rectangle rect = new Rectangle(SIZE_OTHER - 1, SIZE_OTHER -1);
                    rect.setX(800 + SIZE_OTHER * xCount1);
                    rect.setY(SIZE_OTHER * yCount1);
                    rect.setFill(Color.BLUE);
                    otherGroup.getChildren().add(rect);
                }  
                xCount1++;  
            }
        }

        if (ud.p2.equals("")) {
            ;
        } else {
            int xCount2 = 0;
            int yCount2 = 0;
            char[] charB2 = ud.p2.toCharArray();

            for (int i = 0; i < 252; i++) {
                if (xCount2 == 12) {
                    xCount2 = 0;
                    yCount2++;
                } 

                if (charB2[i] == '1') {
                    Rectangle rect = new Rectangle(SIZE_OTHER - 1, SIZE_OTHER -1);
                    rect.setX(600 + SIZE_OTHER * xCount2);
                    rect.setY(HEIGHT/2 + SIZE_OTHER * yCount2);
                    rect.setFill(Color.VIOLET);
                    otherGroup.getChildren().add(rect);
                }  
                xCount2++;  
            }
        }

        if (ud.p3.equals("")) {
            ;
        } else {
            int xCount3 = 0;
            int yCount3 = 0;
            char[] charB3 = ud.p3.toCharArray();

            for (int i = 0; i < 252; i++) {
                if (xCount3 == 12) {
                    xCount3 = 0;
                    yCount3++;
                } 

                if (charB3[i] == '1') {
                    Rectangle rect = new Rectangle(SIZE_OTHER - 1, SIZE_OTHER -1);
                    rect.setX(800 + SIZE_OTHER * xCount3);
                    rect.setY(HEIGHT/2 + SIZE_OTHER * yCount3);
                    rect.setFill(Color.GREEN);
                    otherGroup.getChildren().add(rect);
                }  
                xCount3++;  
            }
        }

        
    }

}
