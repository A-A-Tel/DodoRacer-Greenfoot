import greenfoot.World;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a node in a linked list of directions that form a path through the world grid.
 * <p>
 * Each {@code PathNode} contains a {@link Direction} indicating the next move to make,
 * and a reference to the next node in the path. This structure is used to represent
 * paths found by the {@link #findPath(int, int, int, int, World)} method, typically from
 * a starting point to a target while avoiding obstacles.
 * </p>
 */
public class PathNode {
    public Direction direction;
    public PathNode next;

    public PathNode(Direction direction, PathNode next) {
        this.direction = direction;
        this.next = next;
    }

    /**
     * Finds the fastest path from A to B avoiding obstacles
     *
     * @param x1 The starting X location
     * @param y1 The starting Y location
     * @param x2 The desired X location
     * @param y2 The desired Y location
     * @param world The world to form the path for
     * @return {@code PathNode} if there is a possible path; {@code null} if there is no possible path
     */
    public static PathNode findPath(int x1, int y1, int x2, int y2, World world) {
        int width = world.getWidth();
        int height = world.getHeight();

        final boolean[][] visited = new boolean[height][width];

        for (Fence wall : world.getObjects(Fence.class)) {
            visited[wall.getY()][wall.getX()] = true;
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x1, y1});
        visited[y1][x1] = true;

        int[][] cameFrom = new int[height][width];
        Direction[][] directionFrom = new Direction[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cameFrom[y][x] = -1;
            }
        }

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == x2 && y == y2) {
                return reconstructPath(cameFrom, directionFrom, x1, y1, x2, y2, width);
            }

            for (Direction dir : Direction.values()) {
                int[] offset = getOffset(dir);
                int newX = x + offset[0];
                int newY = y + offset[1];

                if (newX >= 0 && newX < width && newY >= 0 && newY < height && !visited[newY][newX]) {
                    visited[newY][newX] = true;

                    cameFrom[newY][newX] = y * width + x;
                    directionFrom[newY][newX] = dir;

                    queue.add(new int[]{newX, newY});
                }
            }
        }

        return null;
    }

    /**
     * Returns an integer offset representing the specified direction.
     * The offset is a two-element array: {@code [x, y]}.
     *
     * @param dir the desired direction
     * @return a two-element array containing the x and y offset for the direction
     */
    private static int[] getOffset(Direction dir) {
        return switch (dir) {
            case NORTH -> new int[]{0, -1};
            case EAST -> new int[]{1, 0};
            case SOUTH -> new int[]{0, 1};
            case WEST -> new int[]{-1, 0};
        };
    }

    /**
     * Reconstructs the path from the start position to the target position,
     * using the provided trace data from {@link #findPath(int, int, int, int, World)}.
     *
     * @param cameFrom a 2D array mapping each position to the index of its previous position
     * @param directionFrom a 2D array mapping each position to the direction taken to reach it
     * @param x1 the starting x-coordinate
     * @param y1 the starting y-coordinate
     * @param x2 the target x-coordinate
     * @param y2 the target y-coordinate
     * @param width the width of the world grid (used for index calculations)
     * @return the head of a linked list representing the path as {@code PathNode}s,
     *         or {@code null} if no path could be reconstructed
     */
    private static PathNode reconstructPath(int[][] cameFrom, Direction[][] directionFrom,
                                            int x1, int y1, int x2, int y2, int width) {
        int currentX = x2;
        int currentY = y2;

        PathNode head = null;

        while (currentX != x1 || currentY != y1) {
            int prevIndex = cameFrom[currentY][currentX];
            Direction dir = directionFrom[currentY][currentX];

            if (prevIndex == -1) break;

            int prevY = prevIndex / width;
            int prevX = prevIndex % width;

            head = new PathNode(dir, head);

            currentX = prevX;
            currentY = prevY;
        }

        return head;
    }
}