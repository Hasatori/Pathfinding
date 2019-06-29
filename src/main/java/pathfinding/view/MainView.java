package pathfinding.view;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import pathfinding.controller.MainController;
import pathfinding.model.APathFindingAlgorithm;
import pathfinding.model.Signs;
import pathfinding.utils.ResourceLoader;

import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainView extends View {

    private final MainController controller;
    private final Pane centerPane;
    private final Button startButton, stopButton, resetButton;
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
        fillGridPane(300);
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
        Platform.runLater(()->{
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

}
