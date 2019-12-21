package project;

import java.util.*;

import static java.lang.Integer.min;


public class Animal implements IMapElement, Comparable<Animal> {
    private Random random = new Random();
    private MapDirection orientation;
    private Vector2D position;
    private Map map;
    private Genome genome;
    private int energy;
    private boolean alive;
    private int age;
    private boolean chosenOne;
    private boolean isSuccessor;

    private boolean wasBorn;
    private boolean hasDominantGenome;

    private int childrenNumber;

    private List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(Map map, int energy){
            this.map = map;
            this.position = this.map.getFreePosition();
            this.orientation = MapDirection.intToDirection(this.random.nextInt(8));
            this.wasBorn = false;
            this.hasDominantGenome = false;

            this.genome = new Genome();
            this.energy = energy;
            this.alive = true;
            this.childrenNumber = 0;
            this.age = 0;
            this.chosenOne = false;
            this.isSuccessor = false;

    }

    public Animal(Animal mother, Animal father){
            this(mother.getMap(), mother.getEnergy()/4+father.getEnergy()/4);
            this.wasBorn = true;
            this.genome = new Genome(mother, father);
            this.isSuccessor = mother.isChosen() || father.isChosen() || mother.isSuccessor() || father.isSuccessor();

            mother.updateEnergy((-1)*mother.getEnergy()/4);
            mother.incrementChildrenNumber();
            father.updateEnergy((-1)*father.getEnergy()/4);
            father.incrementChildrenNumber();

            List<MapDirection> possibleChildDirections  = new ArrayList<>(Arrays.asList(MapDirection.values()));
            MapDirection childDirection;

            do{
                childDirection = possibleChildDirections.get(this.random.nextInt(possibleChildDirections.size()));
                possibleChildDirections.remove(childDirection);

            }
            while(this.map.isOccupiedByAnimal(mother.getPosition().add(childDirection.toPseudoUnitVector())) && possibleChildDirections.size()>0);

            if(this.map.isOccupiedByAnimal(mother.getPosition().add(childDirection.toPseudoUnitVector()))){
                childDirection =  MapDirection.values()[this.random.nextInt(8)];
            }
            this.position = mother.getPosition().add(childDirection.toPseudoUnitVector());
    }


    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public Genome getGenome() {
        return this.genome;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getAge() {
        return this.age;
    }

    public Map getMap() {
        return this.map;
    }

    public int getChildrenNumber(){
        return this.childrenNumber;
    }

    public boolean isChosen(){
        return this.chosenOne;
    }

    public boolean isSuccessor(){
        return this.isSuccessor;
    }

    public boolean isAlive(){
        return this.alive;
    }

    public void select(){
        this.chosenOne = true;
    }

    public void removeSelection(){
        this.chosenOne = false;
    }

    public void removeSuccessor(){
        this.isSuccessor = false;
    }


    public void updateEnergy(int value) {
        this.energy=min(this.map.getMaxEnergy(), value+this.energy);
    }

    private void incrementChildrenNumber() { this.childrenNumber++; }

    public void move(){
        if(this.energy>=this.map.getMoveEnergy()) {
            this.orientation = MapDirection.intToDirection((MapDirection.directionToInt(this.orientation) + this.genome.getGenome().get(this.random.nextInt(32))) % 8);
            Vector2D oldPosition = new Vector2D(this.position.getX(), this.position.getY());
            this.position = this.position.add(this.orientation.toPseudoUnitVector()).fold(map.getUpperRight());
            this.positionChanged(oldPosition);
            this.updateEnergy((-1)*this.map.getMoveEnergy());

            this.getOlder();
        }
        else{
            this.die();
        }
    }

    private void getOlder(){
        this.age++;
    }

    public void eat(int foodValue){
        this.updateEnergy(foodValue);
    }

    public void reproduce(Animal animal){
        Animal child = new Animal(this, animal);
        this.animalBorn(child);
    }

    public void die(){
        this.alive = false;
        this.energy = 0;
        this.animalDied();
    }

    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }


    private void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    public void animalDied(){
        for(IPositionChangeObserver observer : this.observers){
            observer.animalDied(this);
        }
    }

    private void positionChanged(Vector2D oldPosition){
        for(IPositionChangeObserver observer : this.observers){
            observer.positionChanged(oldPosition, this);
        }
    }

    private void animalBorn(Animal child){
        for(IPositionChangeObserver observer : this.observers){
            observer.animalBorn(child);
        }
    }

    public String toLongString(){
        return "Orientation: "+this.orientation.toString()+" Position: "+this.position.toString()+
                "\nGenome: "+this.genome.getGenome().toString() + "\nEnergy: "+this.energy;
    }


    @Override
    public String toString(){
        if(!this.alive)
            return "xx";

        switch(orientation){
            case NORTH:
                return "^.";
            case NORTHEAST:
                return "^>";
            case EAST:
                return ">.";
            case SOUTHEAST:
                return "v>";
            case SOUTH:
                return "v.";
            case SOUTHWEST:
                return "<v";
            case WEST:
                return "<.";
            case NORTHWEST:
                return "<^";
            default:
                return "";
        }

    }

    @Override
    public int compareTo(Animal other) {
        if(this.equals(other)){
            return 0;
        }
        else if(other.energy<=this.energy){
            return -1;
        }
        else{
            return 1;
        }
    }
}
