import greenfoot.World;

import java.util.List;

public class MyDodo extends Dodo {

    public void act() {
        collectEggs();
    }

    public MyDodo() {
        super(Direction.EAST);
    }

    public void move() {
        if (canMove()) {
            step();
        } else {
            throw new IllegalStateException("I AM STUCK HELP");
        }
    }

    public void move(PathNode node) {

        while (node != null) {
            move(node.direction);
            node = node.next;
        }

    }

    public void move(Direction direction) {
        setDirection(direction);
        move();
    }

    private boolean canMove() {
        return !borderAhead() && !fenceAhead();
    }

    public void collectEggs() {

        World world = getWorld();

        while (true) {
            Egg egg = getNearestEgg(world);
            if (egg == null) break;
            PathNode path = PathNode.findPath(getX(), getY(), egg.getX(), egg.getY(), world);
            move(path);
            pickUpEgg();
        }

        setDirection(Direction.EAST);
    }

    private Egg getNearestEgg(World world) {
        List<Egg> eggs = world.getObjects(Egg.class);

        Egg nearestEgg = null;
        int minDistance = Integer.MAX_VALUE;

        for (Egg egg : eggs) {

            int dx = Math.abs(egg.getX() - getX());
            int dy = Math.abs(egg.getY() - getY());
            int distance = dy + dx;

            if (distance < minDistance) {
                minDistance = distance;
                nearestEgg = egg;
            }
        }
        return nearestEgg;
    }
}

