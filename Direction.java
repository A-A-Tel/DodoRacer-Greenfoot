import java.util.Arrays;
import java.util.Optional;

/**
 * Represents compass directions (NORTH, EAST, SOUTH, WEST) used by the dodo for movement in Greenfoot.
 * <p>
 * This enum is typically used to set or query the facing direction of the dodo.
 * It also provides a utility method to retrieve a direction by its ordinal value.
 * </p>
 */
public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    /**
     * Returns the {@code Direction} corresponding to the given ordinal value.
     *
     * @param value the ordinal value of the desired direction
     * @return an {@code Optional} containing the matching {@code Direction},
     *         or an empty {@code Optional} if no match exists
     */
    public static Optional<Direction> valueOf(int value) {
        return Arrays.stream(values())
                .filter(direction -> direction.ordinal() == value)
                .findFirst();
    }
}
