import java.util.Arrays;
import java.util.Optional;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static Optional<Direction> valueOf(int value) {
        return Arrays.stream(values())
                .filter(direction -> direction.ordinal() == value)
                .findFirst();
    }
}
