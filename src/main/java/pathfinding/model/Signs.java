package pathfinding.model;

public enum Signs {


    WALL_SIGN(1),
    FLOOR_SIGN(0),
    START_SIGN(2),
    FINISH_SIGN(3);

    private final int value;

    Signs(int value) {
        this.value = value;
    }

    public int getSignValue() {
        return value;
    }

}
