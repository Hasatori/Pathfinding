package pathfinding.model;

import pathfinding.controller.MainController;

import java.awt.*;

public abstract class APathFindingAlgorithm implements Runnable {


    protected int[][] grid;
    protected MainController controller;
    protected Point start, finish;

    public APathFindingAlgorithm() {

    }

    public void setGridAndInitParameters(int[][] grid) {
        this.grid = grid;
        setStart();
        setFinish();
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        if (grid == null) {
            throw new IllegalStateException("Grid must be set!");
        }
        if (controller == null) {
            throw new IllegalStateException("Controller must be set!");
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    private void setStart() {
        start = getPoint(Signs.START_SIGN.getSignValue());
    }

    private void setFinish() {
        finish = getPoint(Signs.FINISH_SIGN.getSignValue());
    }

    private Point getPoint(int signValue) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == signValue) {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalStateException("Start must be set");
    }

}
