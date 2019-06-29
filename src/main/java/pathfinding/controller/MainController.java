package pathfinding.controller;

import javafx.stage.Stage;
import pathfinding.model.APathFindingAlgorithm;
import pathfinding.view.MainView;

import java.awt.Point;
import java.io.IOException;
import java.util.List;

public class MainController extends Controller {
    private MainView view;

    /**
     * @param stage stage
     */
    public MainController(Stage stage) {
        super(stage);
        loadView();
    }


    public void startPathFindingAlgorthm(APathFindingAlgorithm aPathFindingAlgorithm, int[][] grid) {
        aPathFindingAlgorithm.setController(this);
        aPathFindingAlgorithm.setGridAndInitParameters(grid);
       Thread thread=new Thread(aPathFindingAlgorithm);
       thread.start();
    }

    public void stop() {

    }

    @Override
    public void loadView() {
        try {
            this.view = new MainView(this);
            stage.setScene(view);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillPath(List<Point> path) {
        view.highlightPath(path);
    }

    public void fillSearch(int x, int y) {
        view.highlightSearch(x, y);

    }
}
