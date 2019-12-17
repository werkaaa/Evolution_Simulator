package project;

public interface IPositionChangeObserver {
    //void positionEnergyChanged(Vector2D oldPosition, int deltaEnergy, Animal animal);
    void positionChanged(Vector2D oldPosition, Animal animal);
    void animalBorn(Animal animal);
    void animalDied(Animal animal);
}
