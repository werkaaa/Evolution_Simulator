package project;

public interface IPositionChangeObserver {
    void positionChanged(Vector2D oldPosition, Animal animal);

    void animalBorn(Animal animal);

    void animalDied(Animal animal);
}
