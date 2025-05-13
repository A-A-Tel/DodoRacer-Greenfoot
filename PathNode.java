import greenfoot.World;

import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.Queue;

public class PathNode {
    public Direction direction;
    public PathNode next;

    public PathNode(Direction direction, PathNode next) {
        this.direction = direction;
        this.next = next;
    }

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

    private static int[] getOffset(Direction dir) {
        return switch (dir) {
            case NORTH -> new int[]{0, -1};
            case EAST -> new int[]{1, 0};
            case SOUTH -> new int[]{0, 1};
            case WEST -> new int[]{-1, 0};
        };
    }

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