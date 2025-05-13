import java.util.List;

public class MyDodo extends Dodo {

    private int numOfSteps;

    public MyDodo() {
        super(EAST);
        numOfSteps = 0;
    }

    public void move() {
        if (canMove()) {
            step();
            numOfSteps++;
        } else {
            showError("I'm stuck!");
        }
    }

    public void move(int distance) {
        for (int i = 0; i < distance; i++) {
            move();
        }
    }

    private boolean canMove() {
        return !borderAhead() && !fenceAhead();
    }

    private List<Egg> getEggs() {
        return getWorld().getObjects(Egg.class);
    }

    private void turn180() {
        turnRight();
        turnRight();
    }
}
