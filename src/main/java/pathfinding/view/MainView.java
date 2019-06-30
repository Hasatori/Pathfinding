package pathfinding.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import pathfinding.Main;
import pathfinding.controller.MainController;
import pathfinding.model.APathFindingAlgorithm;
import pathfinding.model.Signs;
import pathfinding.utils.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainView extends View {

    private final MainController controller;
    private final Pane centerPane;
    private final Button startButton, stopButton, resetButton, generateMazeButton;
    private final ComboBox<APathFindingAlgorithm> algorithmComboBox;
    private final GridPane gridPane = new GridPane();
    private int[][] grid;
    private int gridSize;
    private Point startPoint;
    private Node startNode;
    private Point finishPoint;
    private Node finishNode;

    private static final String FLOOR_STYLE = "-fx-background-color:white";
    private static final String WALL_STYLE = "-fx-background-color:black";
    private static final String START_STYLE = "-fx-background-color:green";
    private static final String FINISH_STYLE = "-fx-background-color:red";
    private static final String PATH_STYLE = "-fx-background-color:blue";
    private static final String SEARCH_STYLE = "-fx-background-color:yellow";

    public MainView(MainController controller) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("mainView.fxml")));
        this.controller = controller;

        this.startButton = (Button) this.lookup("#startButton");
        this.stopButton = (Button) this.lookup("#stopButton");
        this.resetButton = (Button) this.lookup("#resetButton");
        this.generateMazeButton = (Button) this.lookup("#generateMazeButton");
        stopButton.setDisable(true);
        this.centerPane = (Pane) lookup("#centerPane");
        this.algorithmComboBox = (ComboBox) this.lookup("#algorithmCombobox");
        algorithmComboBox.getItems().addAll(ResourceLoader.getListOfPathFindingAlgorithms());
        algorithmComboBox.getSelectionModel().select(0);

        startButton.setOnAction((action) -> {
            controller.startPathFindingAlgorthm(algorithmComboBox.getSelectionModel().getSelectedItem(), grid);
            startButton.setDisable(true);
            stopButton.setDisable(false);
        });

        stopButton.setOnAction((action) -> {
            startButton.setDisable(false);
            stopButton.setDisable(true);
            controller.stop();
        });

        resetButton.setOnAction((action) -> {
            gridPane.getChildren().forEach(node -> {
                node.setStyle(FLOOR_STYLE);
            });
            for (int i = 0; i < grid.length; i++) {
                Arrays.fill(grid[i], Signs.FLOOR_SIGN.getSignValue());
            }

        });

        generateMazeButton.setOnAction((action) -> {
            generateMaze(grid);
        });
        fillGridPane(500);
        centerPane.getChildren().add(gridPane);


    }


    private void fillGridPane(double size) {

        grid = new int[(int) Math.round(size)][(int) Math.round(size)];
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.CENTER);
        gridSize = (int) Math.round(size);

        for (int i = 0; i < size; i++) {
            gridPane.addRow(i, getRow(size, i));
        }
        gridPane.setGridLinesVisible(false);
        gridPane.setGridLinesVisible(true);
    }

    private Node[] getRow(double count, int rowNumber) {
        Node[] result = new Node[(int) Math.round(count)];
        double size = Math.ceil(centerPane.getPrefHeight() / count);

        for (int i = 0; i < count; i++) {
            int finalI = i;
            grid[rowNumber][i] = 0;


            Pane pane = new Pane();
/*
            pane.setOnMouseEntered(event -> {
                if (event.isAltDown() && pane.getStyle().equals(FLOOR_STYLE)) {
                    grid[rowNumber][finalI] = Signs.WALL_SIGN.getSignValue();
                    pane.setStyle(WALL_STYLE);
                }
                if (event.isControlDown() && pane.getStyle().equals(WALL_STYLE)) {
                    grid[rowNumber][finalI] = Signs.FLOOR_SIGN.getSignValue();
                    pane.setStyle(FLOOR_STYLE);
                }

            });
            pane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    setUniquePointAction(startPoint, startNode, rowNumber, finalI, Signs.START_SIGN.getSignValue(), START_STYLE, pane);
                    this.startPoint = new Point(rowNumber, finalI);
                    startNode = pane;
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    setUniquePointAction(finishPoint, finishNode, rowNumber, finalI, Signs.FINISH_SIGN.getSignValue(), FINISH_STYLE, pane);
                    this.finishPoint = new Point(rowNumber, finalI);
                    finishNode = pane;
                }
            });
*/
            pane.setStyle(FLOOR_STYLE);
            pane.setMinSize(size, size);
            result[i] = pane;
        }
        return result;
    }

    private void setUniquePointAction(Point point, Node node, int rowNumber, int column, int signValue, String style, Pane pane) {
        if (point != null && node != null) {
            grid[rowNumber][column] = Signs.FLOOR_SIGN.getSignValue();
            node.setStyle(FLOOR_STYLE);
        }
        grid[rowNumber][column] = signValue;
        pane.setStyle(style);

    }

    public void highlightPath(List<Point> path) {

        highlight(PATH_STYLE, path);
    }

    public void highlightSearch(int x, int y) {
        Platform.runLater(() -> {
            Node node = gridPane.getChildren().get(x * gridSize + y);
            node.setStyle(SEARCH_STYLE);
        });

    }

    private void highlight(String style, List<Point> toHighlight) {
        Platform.runLater(() -> {
            for (Point point : toHighlight) {
                Node node = gridPane.getChildren().get(point.x * gridSize + point.y);
                node.setStyle(style);
            }
        });
    }

    private void generateMaze(int[][] maz) {
        for (int x = 0; x < maz.length; x++) {
            for (int i = 0; i < maz[x].length; i++) {
                maz[x][i] = Signs.WALL_SIGN.getSignValue();
                gridPane.getChildren().get(x * gridSize + i).setStyle(WALL_STYLE);

            }
        }

        // select random point and open as start node
        MyPoint startingPoint = new MyPoint((int) (Math.random() * maz.length), (int) (Math.random() * maz.length), null);
        List<Node> toHighlight = new LinkedList<>();
        maz[startingPoint.r][startingPoint.c] = Signs.START_SIGN.getSignValue();
        gridPane.getChildren().get(startingPoint.r * gridSize + startingPoint.c).setStyle(START_STYLE);

        // iterate through direct neighbors of node
        ArrayList<MyPoint> frontier = new ArrayList<>();
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0 || x != 0 && y != 0)
                    continue;
                try {
                    if (maz[startingPoint.r + x][startingPoint.c + y] == Signs.FLOOR_SIGN.getSignValue()) continue;
                } catch (Exception e) { // ignore ArrayIndexOutOfBounds
                    continue;
                }
                // add eligible points to frontier
                frontier.add(new MyPoint(startingPoint.r + x, startingPoint.c + y, startingPoint));
            }

        MyPoint last = null;
        while (!frontier.isEmpty()) {
            // pick current node at random
            MyPoint cu = frontier.remove((int) (Math.random() * frontier.size()));
            MyPoint op = cu.opposite();
            try {
                // if both node and its opposite are walls
                if (maz[cu.r][cu.c] == Signs.WALL_SIGN.getSignValue()) {
                    if (maz[op.r][op.c] == Signs.WALL_SIGN.getSignValue()) {

                        // open path between the nodes
                        maz[cu.r][cu.c] = Signs.FLOOR_SIGN.getSignValue();
                        maz[op.r][op.c] = Signs.FLOOR_SIGN.getSignValue();
                        toHighlight.add(gridPane.getChildren().get(cu.r * gridSize + cu.c));
                        toHighlight.add(gridPane.getChildren().get(op.r * gridSize + op.c));

                        // store last node in order to mark it later
                        last = op;

                        // iterate through direct neighbors of node, same as earlier
                        for (int x = -1; x <= 1; x++)
                            for (int y = -1; y <= 1; y++) {
                                if (x == 0 && y == 0 || x != 0 && y != 0)
                                    continue;
                                try {
                                    if (maz[op.r + x][op.c + y] == Signs.FLOOR_SIGN.getSignValue()) continue;
                                } catch (Exception e) {
                                    continue;
                                }
                                frontier.add(new MyPoint(op.r + x, op.c + y, op));
                            }
                    }
                }
            } catch (Exception e) { // ignore NullPointer and ArrayIndexOutOfBounds
            }

            // if algorithm has resolved, mark end node
            if (frontier.isEmpty())
                maz[last.r][last.c] = Signs.FINISH_SIGN.getSignValue();
        }

        MyPoint finalLast = last;
        new Thread(() -> {
            for (int i = 0; i < toHighlight.size(); i++) {
                int finalI = i;
                Platform.runLater(() -> {
                    toHighlight.get(finalI).setStyle(FLOOR_STYLE);
                });
                if (i % 50 == 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            gridPane.getChildren().get(finalLast.r * gridSize + finalLast.c).setStyle(FINISH_STYLE);
        }).start();

    }

    private void printMaze(int[][] maz, int start, int end) {
        int floor = Signs.FLOOR_SIGN.getSignValue();
        int wall = Signs.WALL_SIGN.getSignValue();
        for (int x = start; x < end; x++) {
            for (int i = 0; i < maz[x].length; i++) {
                int value = maz[x][i];
                String style = "";
                if (value == floor) {
                    style = FLOOR_STYLE;
                }

                if (value == wall) {
                    style = WALL_STYLE;
                }
                int finalX = x;
                int finalI = i;
                String finalStyle = style;
                Platform.runLater(() -> {
                    gridPane.getChildren().get(finalX * gridSize + finalI).setStyle(finalStyle);
                });


                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class MyPoint {
        Integer r;
        Integer c;
        MyPoint parent;

        public MyPoint(int x, int y, MyPoint p) {
            r = x;
            c = y;
            parent = p;
        }

        // compute opposite node given that it is in the other direction from the parent
        public MyPoint opposite() {
            if (this.r.compareTo(parent.r) != 0)
                return new MyPoint(this.r + this.r.compareTo(parent.r), this.c, this);
            if (this.c.compareTo(parent.c) != 0)
                return new MyPoint(this.r, this.c + this.c.compareTo(parent.c), this);
            return null;
        }
    }
}
