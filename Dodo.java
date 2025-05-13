import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.Optional;

public abstract class Dodo extends Actor {

    private Direction direction;

    private final GreenfootImage imageRight, imageLeft;

    protected Dodo(Direction direction) {

        this.direction = direction;
        imageRight = getImage();
        imageLeft = new GreenfootImage(imageRight);
        imageLeft.mirrorHorizontally();
        setImage();

    }

    private void setImage() {
        if (direction == Direction.NORTH) {
            setImage(imageLeft);
            setRotation(90);
        } else if (direction == Direction.EAST) {
            setImage(imageRight);
            setRotation(0);
        } else if (direction == Direction.SOUTH) {
            setImage(imageRight);
            setRotation(90);
        } else if (direction == Direction.WEST) {
            setImage(imageLeft);
            setRotation(0);
        }
    }


    public boolean fenceAhead() {
        return getActorAhead(Fence.class) != null;
    }

    public boolean eggAhead() {
        return getActorAhead(Egg.class) != null;
    }

    public boolean nestAhead() {
        return getActorAhead(Nest.class) != null;
    }

    public boolean onEgg() {
        return getActor(Egg.class) != null;
    }

    public boolean onNest() {
        return getActor(Nest.class) != null;
    }

    public boolean onGrain() {
        return getActor(Grain.class) != null;
    }

    public void layEgg() {
        getWorld().addObject(new BlueEgg(), getX(), getY());
    }

    public Egg getEgg() {
        return getActor(Egg.class);
    }

    public Egg pickUpEgg() {
        Egg maybeEgg = getEgg();
        if (maybeEgg == null) {
            showError("There is no egg in this cell");
            Greenfoot.stop();
        } else {
            removeActor(maybeEgg);
        }
        return maybeEgg;
    }

    public Grain pickUpGrain() {
        Grain maybeGrain = getActor(Grain.class);
        if (maybeGrain == null) {
            showError("There is no grain in this cell");
            Greenfoot.stop();
        } else {
            removeActor(maybeGrain);
        }
        return maybeGrain;
    }

    private void removeActor(Actor actor) {
        getWorld().removeObject(actor);
    }

    public boolean dodoAhead() {
        return getActorAhead(Dodo.class) != null;
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            setImage();
            if (Mauritius.traceIsOn()) {
                Greenfoot.delay(1);
            }
        }
    }

    public int randomDirection() {
        return Greenfoot.getRandomNumber(4);
    }

    private int modulo(int a, int b) {
        return (a % b + b) % b;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getX() {
        return super.getX();
    }

    public int getY() {
        return super.getY();
    }

    private <E extends Actor> E getActorAhead(Class<E> cls) {
        if (direction == Direction.NORTH) {
            return (E) getOneObjectAtOffset(0, -1, cls);
        } else if (direction == Direction.EAST) {
            return (E) getOneObjectAtOffset(1, 0, cls);
        } else if (direction == Direction.SOUTH) {
            return (E) getOneObjectAtOffset(0, 1, cls);
        } else if (direction == Direction.WEST) {
            return (E) getOneObjectAtOffset(-1, 0, cls);
        } else {
            return null;
        }
    }

    private <E extends Actor> E getActor(Class<E> cls) {
        return (E) getOneObjectAtOffset(0, 0, cls);
    }


    public void step() {
        if (direction == Direction.NORTH) {
            setLocation(getX(), getY() - 1);
        } else if (direction == Direction.EAST) {
            setLocation(getX() + 1, getY());
        } else if (direction == Direction.SOUTH) {
            setLocation(getX(), getY() + 1);
        } else if (direction == Direction.WEST) {
            setLocation(getX() - 1, getY());
        }
        if (Mauritius.traceIsOn()) {
            Greenfoot.delay(1);
        }
    }


    /**
     * Test is we are facing the border.
     */
    public boolean borderAhead() {
        if (direction == Direction.NORTH) {
            return getY() == 0;
        } else if (direction == Direction.EAST) {
            return getX() == getWorld().getWidth() - 1;
        } else if (direction == Direction.SOUTH) {
            return getY() == getWorld().getHeight() - 1;
        } else { // if ( myDirection == WEST ) {
            return getX() == 0;
        }
    }

    public boolean facingNorth() {
        return getDirection() == Direction.NORTH;
    }

    public void turnLeft() {

        Optional<Direction> direction = Direction.valueOf(modulo(this.direction.ordinal() - 1, 4));

        if (direction.isEmpty()) throw new IllegalArgumentException("Invalid direction");

        setDirection(direction.get());
    }

    public void turnRight() {

        Optional<Direction> direction = Direction.valueOf(modulo(this.direction.ordinal() + 1, 4));

        if (direction.isEmpty()) throw new IllegalArgumentException("Invalid direction");

        setDirection(direction.get());
    }

    public void updateScores(int score1, int score2) {
        ((Mauritius) getWorld()).updateScore(score1, score2);
    }

    protected void showError(String err_msg) {
        Message.showMessage(new Alert(err_msg), getWorld());
    }

    protected void showCompliment(String compl_msg) {
        Message.showMessage(new Compliment(compl_msg), getWorld());
    }

}
