import greenfoot.World;

import java.util.List;

/**
 * A specialized dodo that automatically collects all eggs in the world.
 * <p>
 * The {@code MyDodo} moves through the grid, finds the nearest egg using
 * Manhattan distance, and follows a computed path to collect it, avoiding
 * obstacles such as fences.
 * </p>
 */
public class MyDodo extends Dodo {

    public void act() {
        collectEggs();
    }

    public MyDodo() {
        super(Direction.EAST);
    }

    /**
     * Moves the dodo one step forward in its current facing direction.
     *
     * @throws IllegalStateException if the dodo is unable to move
     */
    public void move() {
        if (canMove()) {
            step();
        } else {
            throw new IllegalStateException("I AM STUCK HELP");
        }
    }

    /**
     * Moves the dodo along the sequence of directions specified by the given {@link PathNode}.
     * If the node is {@code null}, no movement occurs.
     *
     * @param node the starting node of the path
     * @see #move(Direction)
     */
    public void move(PathNode node) {
        while (node != null) {
            move(node.direction);
            node = node.next;
        }
    }

    /**
     * Sets the dodo's facing direction and attempts to move in that direction.
     *
     * @param direction the desired direction
     * @see Direction
     * @see #move()
     */
    public void move(Direction direction) {
        setDirection(direction);
        move();
    }

    /**
     * Checks if the dodo is able to move forward.
     *
     * @return {@code true} if the facing direction is clear of borders or fences;
     *         {@code false} if something is in the way
     */
    private boolean canMove() {
        return !borderAhead() && !fenceAhead();
    }

    /**
     * Finds and collects all eggs in the world by moving to each one in turn.
     * 
     * @see #getNearestEgg(World)
     * @see #move(PathNode) 
     */
    public void collectEggs() {

        World world = getWorld();
        Egg egg = getNearestEgg(world);

        while (egg != null) {
            PathNode path = PathNode.findPath(getX(), getY(), egg.getX(), egg.getY(), world);
            move(path);
            pickUpEgg();
            egg = getNearestEgg(world);
        }

        setDirection(Direction.EAST);
    }

    /**
     * Finds the nearest egg using Manhattan distance.
     * Searches among all {@link Egg} objects in the world.
     *
     * @param world the world to search in
     * @return the nearest {@link Egg}, or {@code null} if no eggs remain
     */
    private Egg getNearestEgg(World world) {
        List<Egg> eggs = world.getObjects(Egg.class);

        Egg nearestEgg = null;
        int minDistance = Integer.MAX_VALUE;

        int x = getX();
        int y = getY();

        for (Egg egg : eggs) {

            int dx = Math.abs(egg.getX() - x);
            int dy = Math.abs(egg.getY() - y);
            int distance = dy + dx;

            if (distance < minDistance) {
                minDistance = distance;
                nearestEgg = egg;
            }
        }
        return nearestEgg;
    }
}

