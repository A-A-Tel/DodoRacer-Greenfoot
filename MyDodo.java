import greenfoot.Actor;
import greenfoot.World;

import java.util.List;

public class MyDodo extends Dodo {

    private int numOfSteps;

    public MyDodo() {
        super(Direction.EAST);
        numOfSteps = 0;
    }

    public void move() {
        if (canMove()) {
            step();
            numOfSteps++;
        } else {
            throw new IllegalStateException("I AM STUCK HELP");
        }
    }

    public void move(int distance) {
        for (int i = 0; i < distance; i++) {
            move();
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

        for (Actor egg : world.getObjects(Egg.class)) {
            PathNode path = PathNode.findPath(getX(), getY(), egg.getX(), egg.getY(), world);
            move(path);
//            pickUpEgg();
        }
        setDirection(Direction.EAST);
    }

    private List<Egg> getEggs() {
        return getWorld().getObjects(Egg.class);
    }

    private void turn180() {
        turnRight();
        turnRight();
    }
}

