import greenfoot.Actor;
import greenfoot.World;

import java.util.LinkedList;
import java.util.Queue;

public class PathNode {
    public int x, y;
    public PathNode next;

    PathNode(int x, int y, PathNode next) {
        this.x = x;
        this.y = y;
        this.next = next;
    }

    public static PathNode findPath(int x1, int y1, int x2, int y2, World world) {
        int width = world.getWidth();
        int height = world.getHeight();

        boolean[][] visited = new boolean[height][width];

        for (Actor fenceObj : world.getObjects(Fence.class)) {
            visited[fenceObj.getY()][fenceObj.getX()] = true;
        }

        Queue<PathNode> queue = new LinkedList<>();
        queue.add(new PathNode(x1, y1, null));
        visited[y1][x1] = true;

        int[][] directions = {{0, -1}, // up
                {0, 1},  // down
                {-1, 0}, // left
                {1, 0}   // right
        };

        while (!queue.isEmpty()) {
            PathNode current = queue.poll();

            if (current.x == x2 && current.y == y2) {
                // Rebuild path from goal to start by reversing `next` pointers
                PathNode result = null;
                while (current != null) {
                    result = new PathNode(current.x, current.y, result);
                    current = current.next;
                }
                return result;
            }

            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                if (nx >= 0 && ny >= 0 && nx < width && ny < height && !visited[ny][nx]) {
                    visited[ny][nx] = true;
                    queue.add(new PathNode(nx, ny, current));
                }
            }
        }

        return null; // No path found
    }
}