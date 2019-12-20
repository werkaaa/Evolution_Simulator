package project;

import java.util.*;

import static java.lang.Integer.min;


public class Animal implements IMapElement, Comparable<Animal> {
    private MapDirection orientation;
    private Vector2D position;
    private Map map;
    private Genom genom;
    private int energy;
    public boolean alive;
    private int age;
    private boolean chosenOne;
    private boolean isSuccessor;


    public Animal mother;
    private Animal father;

    boolean wasBorn = false;

    private int childrenNumber;
    // TODO: number of successors

    private List<IPositionChangeObserver> observers = new ArrayList<>();

    public String toLongString(){
        return "Orientation: "+this.orientation.toString()+" Position: "+this.position.toString()+
                "\nGenom: "+this.genom.genom.toString() + "\nEnergy: "+this.energy;
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

    public Animal(Map map) {
        this(map,  70);
        //this.genom = genom;
    }

    public Animal(Map map, int energy){
            Random random = new Random();
            this.map = map;
            this.position = this.map.getFreePosition();
            this.orientation = MapDirection.intToDirection(random.nextInt(8));

            this.genom = new Genom();
            this.energy = energy;
            this.alive = true;
            this.mother = null;
            this.father = null;
            this.childrenNumber = 0;
            this.age = 0;
            this.chosenOne = false;
            this.isSuccessor = false;

    }

    public Animal(Animal mother, Animal father){
            System.out.println("Animal was borned");
            this.wasBorn = true;
            this.alive = true;
            this.childrenNumber = 0;
            this.mother = mother;
            this.father = father;
            this.map = mother.getMap();
            this.genom = new Genom(mother, father);
            this.isSuccessor = mother.isChosen() || father.isChosen() || mother.isSuccessor() || father.isSuccessor();
            this.energy = mother.getEnergy()/4+father.getEnergy()/4;
            mother.updateEnergy((-1)*mother.getEnergy()/4);
            //mother.positionEnergyChanged(mother.position, (-1)*mother.getEnergy()/4);
            mother.incrementChildrenNumber();
            father.updateEnergy((-1)*father.getEnergy()/4);
            //father.positionEnergyChanged(father.position, (-1)*father.getEnergy()/4);
            father.incrementChildrenNumber();

            Random random = new Random();
            this.orientation = MapDirection.values()[random.nextInt(8)];

            List<MapDirection> possibleChildDirections  = new ArrayList<>(Arrays.asList(MapDirection.values()));
            MapDirection childDirection;

            do{
                childDirection = possibleChildDirections.get(random.nextInt(possibleChildDirections.size()));
                possibleChildDirections.remove(childDirection);

            }
            while(this.map.isOccupiedByAnimal(mother.getPosition().add(childDirection.toPseudoUnitVector())) && possibleChildDirections.size()>0);

            if(this.map.isOccupiedByAnimal(mother.getPosition().add(childDirection.toPseudoUnitVector()))){
                childDirection =  MapDirection.values()[random.nextInt(8)];
            }
            this.position = mother.getPosition().add(childDirection.toPseudoUnitVector());
    }


    public MapDirection getOrientation() {
        return this.orientation;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    public Genom getGenom() {
        return this.genom;
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

    public int getChildrenNumber(){
        return this.childrenNumber;
    }


    public void move(){
        if(this.energy>=this.map.moveEnergy) {
            Random random = new Random();
            this.orientation = MapDirection.intToDirection((MapDirection.directionToInt(this.orientation) + this.genom.genom.get(random.nextInt(32))) % 8);
            Vector2D oldPosition = new Vector2D(this.position.x, this.position.y);
            this.position = this.position.add(this.orientation.toPseudoUnitVector()).fold(map.lowerLeft, map.upperRight);
            //this.positionEnergyChanged(oldPosition, (-1)*this.map.moveEnergy);
            this.positionChanged(oldPosition);
            this.updateEnergy((-1)*this.map.moveEnergy); //energy update is hear at the end because
                                                                // TreeSet sometimes won't remove an element if we change a value, which is need in compareTo
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

    public void animalDied(){
        for(IPositionChangeObserver observer : this.observers){
            observer.animalDied(this);
        }
    }

    public void addObserver(IPositionChangeObserver observer){
            this.observers.add(observer);
        }

    private void removeObserver(IPositionChangeObserver observer){
            this.observers.remove(observer);
        }

//    private void positionEnergyChanged(Vector2D oldPosition, int deltaEnergy){
//            for(IPositionChangeObserver observer : this.observers){
//                observer.positionEnergyChanged(oldPosition, deltaEnergy, this);
//            }
//        }

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
