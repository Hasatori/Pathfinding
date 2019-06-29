package pathfinding.model;


import com.sun.javafx.scene.traversal.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AAlgorithm extends APathFindingAlgorithm {

    private List<AAlgorithmPoint> queue;
    private AAlgorithmPoint aStart;
    private int distanceCounter;

    public AAlgorithm() {
        super();
    }

    @Override
    public void run() throws IllegalStateException {
        super.run();
        try {
            queue = new ArrayList<>();
            distanceCounter = 0;
            AAlgorithmPoint aaFinish = fromPoint(finish, distanceCounter);

            queue.add(aaFinish);

            List<AAlgorithmPoint> neighbours = getNeighbours(aaFinish);
            queue.addAll(neighbours);

            while (!containsStart(neighbours)) {
                List<AAlgorithmPoint> newNeighbours = new ArrayList<>();
                distanceCounter++;
                for (AAlgorithmPoint neighbour : neighbours) {
                    newNeighbours.addAll(getNeighbours(neighbour));
                }
                newNeighbours.removeAll(neighbours);
                neighbours = newNeighbours;
            }
            controller.fillPath(getPath());
        } catch (InterruptedException e) {

        }
    }

    private List<Point> convert(List<AAlgorithmPoint> source) {
        List<Point> result = new ArrayList<>();
        source.forEach(point -> {
            result.add(new Point(point.x, point.y));
        });
        return result;
    }

    private List<Point> getPath() {
        List<Point> result = new ArrayList<>();
        AAlgorithmPoint point = aStart;
        while (!point.isFinish()) {
            point = findNearest(point);
            result.add(point);
        }
        return result;
    }

    private AAlgorithmPoint findNearest(AAlgorithmPoint point) {
        int min = point.getAvailableNeighbours().stream().mapToInt(AAlgorithmPoint::getDistance).min().getAsInt();
        return point.getAvailableNeighbours().stream().filter(aAlgorithmPoint -> aAlgorithmPoint.getDistance() == min && !aAlgorithmPoint.isStart()).findFirst().get();
    }

    private boolean containsStart(List<AAlgorithmPoint> aAlgorithmPoints) {
        return aAlgorithmPoints.stream().anyMatch(AAlgorithmPoint::isStart);
    }

    private List<AAlgorithmPoint> getNeighbours(AAlgorithmPoint point) throws InterruptedException {
        List<AAlgorithmPoint> result = new ArrayList<>();
        AAlgorithmPoint left = new AAlgorithmPoint(point.x, point.y - 1, distanceCounter);
        AAlgorithmPoint right = new AAlgorithmPoint(point.x, point.y + 1, distanceCounter);
        AAlgorithmPoint top = new AAlgorithmPoint(point.x - 1, point.y, distanceCounter);
        AAlgorithmPoint down = new AAlgorithmPoint(point.x + 1, point.y, distanceCounter);
        addNeighbour(result, point, left, Direction.LEFT);
        addNeighbour(result, point, right, Direction.RIGHT);
        addNeighbour(result, point, top, Direction.UP);
        addNeighbour(result, point, down, Direction.DOWN);
        return result;
    }

    private void addNeighbour(List<AAlgorithmPoint> result, AAlgorithmPoint point, AAlgorithmPoint neighbour, Direction direction) throws InterruptedException {
        if (!neighbour.isOutOfBounds(neighbour.x, neighbour.y)) {
            if (!neighbour.isWall() && !queue.contains(neighbour) && !result.contains(neighbour)) {
                point.setNeighbour(direction, neighbour);
                result.add(neighbour);
                queue.add(neighbour);
                neighbour.setNeighbour(getopositedirection(direction), point);
                if (neighbour.isStart()) {
                    aStart = neighbour;
                }
                if (!neighbour.isStart() && !neighbour.isFinish()) {
                    controller.fillSearch(neighbour.x, neighbour.y);
                    Thread.sleep(0);
                }
            }


        }
    }

    public AAlgorithmPoint fromPoint(Point point, int distance) {
        return new AAlgorithmPoint(point.x, point.y, distance);
    }

    private static Direction getopositedirection(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;

        }
        throw new IllegalStateException("Illegal direction. Allowed directions are: TOP,DOWN,LEFT,RIGHT");
    }

    class AAlgorithmPoint extends Point {
        private final int distance;
        private AAlgorithmPoint leftNeighbour, rightNeighbour, topNeighbour, bottomNeighbour;

        AAlgorithmPoint(int x, int y, int distance) {
            super(x, y);
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }

        void setNeighbour(Direction direction, AAlgorithmPoint neighbour) {
            switch (direction) {
                case UP:
                    this.topNeighbour = neighbour;
                    return;
                case DOWN:
                    this.bottomNeighbour = neighbour;
                    return;
                case LEFT:
                    this.leftNeighbour = neighbour;
                    return;
                case RIGHT:
                    this.rightNeighbour = neighbour;
                    return;
                default:

            }
            throw new IllegalStateException("Illegal direction. Allowed directions are: TOP,DOWN,LEFT,RIGHT");
        }


        AAlgorithmPoint getNeighbour(Direction direction) {
            try {
                switch (direction) {
                    case UP:
                        return topNeighbour;
                    case DOWN:
                        return bottomNeighbour;
                    case LEFT:
                        return leftNeighbour;
                    case RIGHT:
                        return rightNeighbour;
                }
            } catch (NullPointerException e) {
                throw new IllegalStateException("Neighbour is not set");
            }
            throw new IllegalStateException("Illegal direction. Allowed directions are: TOP,DOWN,LEFT,RIGHT");

        }

        List<AAlgorithmPoint> getAvailableNeighbours() {
            List<AAlgorithmPoint> result = new ArrayList<>();
            if (leftNeighbour != null) {
                result.add(leftNeighbour);
            }
            if (rightNeighbour != null) {
                result.add(rightNeighbour);
            }
            if (topNeighbour != null) {
                result.add(topNeighbour);
            }
            if (bottomNeighbour != null) {
                result.add(bottomNeighbour);
            }
            return result;
        }


        boolean isWall() {
            return grid[x][y] == Signs.WALL_SIGN.getSignValue();
        }

        boolean isFinish() {
            return grid[x][y] == Signs.FINISH_SIGN.getSignValue();
        }

        boolean isStart() {
            return grid[x][y] == Signs.START_SIGN.getSignValue();
        }

        boolean isOutOfBounds(int x, int y) {
            return x >= grid.length || y >= grid.length || x < 0 || y < 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AAlgorithmPoint) {
                AAlgorithmPoint toCompare = (AAlgorithmPoint) obj;
                return this.x == toCompare.x && this.y == toCompare.y;
            }
            throw new IllegalArgumentException("Object must be instance of " + AAlgorithmPoint.class.getSimpleName());
        }
    }

}
