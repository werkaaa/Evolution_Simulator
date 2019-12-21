package project;


import java.util.List;
import java.util.Random;

public class Jungle {
    private Random random = new Random();
    private final Vector2D jungleLowerLeft;
    private final Vector2D jungleUpperRight;

    public List<Vector2D> freePositions;

    public Jungle(Vector2D jungleLowerLeft, Vector2D jungleUpperRight) {
        this.jungleLowerLeft = jungleLowerLeft;
        this.jungleUpperRight = jungleUpperRight;

        this.freePositions = this.jungleLowerLeft.allPointsBetween(this.jungleUpperRight);
    }

    public boolean inJungle(Vector2D position) {
        return position.follows(this.jungleLowerLeft) && position.precedes(this.jungleUpperRight);
    }

    public Vector2D getFreeJunglePosition() {
        if (this.freePositions.size() > 0) {
            return freePositions.remove(this.random.nextInt(this.freePositions.size()));
        }
        return null;
    }

    public void addFreeJunglePosition(Vector2D position) {
        if (!this.freePositions.contains(position)) {
            this.freePositions.add(position);
        }
    }

    public void removeFreeJunglePosition(Vector2D position) {
        this.freePositions.remove(position);
    }

}
