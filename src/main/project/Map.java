package project;



import javax.swing.*;
import java.util.*;

import static java.lang.Math.sqrt;


public class Map implements IPositionChangeObserver{
    Random random = new Random();
    protected MapVisualizer visualizer = new MapVisualizer(this);
    //public java.util.Map<Vector2D, SortedSet<Animal>> animals = new HashMap<>();
    public java.util.Map<Vector2D, List<Animal>> animals = new HashMap<>();

    public java.util.Map<Vector2D, Plant> plants = new HashMap<>();

    public List<Vector2D> freePositions;
    public List<Vector2D> freeSavannaPositions;
    public java.util.Map<Vector2D, Animal> diedToday = new HashMap<>();

    public final Vector2D lowerLeft;
    public final Vector2D upperRight;
    public Jungle jungle;


    public final int startEnergy;
    public final int plantEnergy;
    public final int moveEnergy;
    public final double jungleRatio;

    //TODO: to ogólnie nie liczy zwierząt
    public int animalsNumber;

    public Map(int width, int height, int startEnergy, int plantEnergy, int moveEnergy, double jungleRation) {
        this.lowerLeft = new Vector2D(0,0);
        this.upperRight = new Vector2D(width-1, height-1);

        this.animalsNumber = 0;

        this.startEnergy = startEnergy;
        this.plantEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
        this.jungleRatio = jungleRation;

        int jungleWidth = (int) (sqrt(jungleRation)*width);
        int jungleHeight = (int) (sqrt(jungleRation)*height);
        System.out.println("ratio"+sqrt(jungleRation)+" "+width+" "+sqrt(jungleRation)*width);
        System.out.println("jungle size"+jungleWidth+" "+jungleHeight);

        this.jungle = new Jungle(new Vector2D((width - jungleWidth)/2, (height - jungleHeight)/2), new Vector2D((width + jungleWidth)/2-1, (height + jungleHeight)/2-1));

        this.freePositions = this.lowerLeft.allPointsBetween(this.upperRight);

        this.freeSavannaPositions = this.lowerLeft.allPointsBetween(this.upperRight);

        for(Vector2D position : this.jungle.freePositions){
            this.freeSavannaPositions.remove(position);
        }
    }

    public String toString() {
        return visualizer.draw(lowerLeft, upperRight);
    }


    public void place(IMapElement element) {
        if(element instanceof Animal) {
            putAnimal((Animal) element);
            ((Animal) element).addObserver(this);
            this.animalsNumber++;
        }
        else{
            this.plants.put(element.getPosition(), (Plant) element);
        }
        if(this.jungle.inJungle(element.getPosition())){
            this.jungle.removeFreeJunglePosition(element.getPosition());
        }
        else{
            this.freeSavannaPositions.remove(element.getPosition());
        }
    }

    private void putAnimal(Animal animal){
        Vector2D animalPosition = animal.getPosition();
        if(isOccupiedByAnimal(animalPosition)){ //this only happens if we add a child and all the places around are already taken
                                                //usually we get a random free position
            this.animals.get(animalPosition).add(animal);
        }
        else{
            //this.animals.put(animalPosition, new TreeSet<Animal>());
            this.animals.put(animalPosition, new ArrayList<Animal>());
            this.animals.get(animalPosition).add(animal);
        }
        Collections.sort(this.animals.get(animalPosition));

    }

    public void run(){
        buryAnimals();
        moveAnimals();
        feedAnimals();
        System.out.println(this.animalsNumber);
        System.out.println(this);
        mateAnimals();
        placePlants();
       // place(new Plant(this));
        System.out.println(diedToday);
    }

    public void moveAnimals(){
        List<Animal> animals = new ArrayList<>();
        //for(SortedSet<Animal> setOfAnimals : this.animals.values()){
        for(List<Animal> setOfAnimals : this.animals.values()){
            for(Animal element : setOfAnimals) {
                animals.add(element);
            }
        }

        for(int iter = 0; iter<animals.size(); iter++){
            animals.get(iter).move();
        }
    }
    // TODO: zapytać, czy podejście z rozmnażanie w mapie jest ok
    public void mateAnimals(){
        //store parents in the arrays
        List<Animal> mothers = new ArrayList<>();
        List<Animal> fathers = new ArrayList<>();
        //for(SortedSet<Animal> setOfAnimals : this.animals.values()){
        for(List<Animal> setOfAnimals : this.animals.values()){
            //Animal mother = setOfAnimals.first();
            Animal mother = setOfAnimals.get(0);
            Animal father = null;
            //find father
            int iter = 0;
            for (Animal animal : setOfAnimals) {
                if (iter == 1) {
                    father = animal;
                }
                iter++;
            }
            if (mother != null && father != null) {
                if(2*mother.getEnergy()>=this.startEnergy && 2*father.getEnergy()>=this.startEnergy) {
                    mothers.add(mother);
                    fathers.add(father);
                }
            }
        }
        //reproduce animals element-wise
        for(int i = 0; i<mothers.size(); i++){
            mothers.get(i).reproduce(fathers.get(i));
        }
    }

    public void feedAnimals(){
       // for(SortedSet<Animal> setOfAnimals : this.animals.values()){
       for(List<Animal> setOfAnimals : this.animals.values()){
           //Animal animal = setOfAnimals.first();
           Animal animal = setOfAnimals.get(0);
           Plant plant = this.plants.remove(animal.getPosition());
           if(plant!=null){
               animal.eat();
           }
           if(this.jungle.inJungle(animal.getPosition())){
               this.jungle.addFreeJunglePosition(animal.getPosition());
           }
           else{
               this.addFreeSavannaPosition(animal.getPosition());
           }
        }

    }

    private void addFreeSavannaPosition(Vector2D position){
        if(!this.freeSavannaPositions.contains(position)) {
            this.freeSavannaPositions.add(position);
        }
    }

    private void removeFreeSavannaPosition(Vector2D position){

    }


    public void buryAnimals() {
        this.diedToday.clear();
    }

    public void placePlants() {
        Vector2D junglePosition = this.getFreeJunglePosition();
        if(junglePosition!=null) {
            this.place(new Plant(junglePosition, this));
        }
        Vector2D savannaPosition = this.getFreeSavannaPosition();
        if(savannaPosition!=null) {
            this.place(new Plant(savannaPosition, this));
        }
    }

    public Vector2D getFreePosition(){
        if(this.random.nextDouble()>this.jungleRatio){
            return this.getFreeSavannaPosition();
        }
        else{
            return this.getFreeJunglePosition();
        }
        //return freePositions.remove(this.random.nextInt(this.freePositions.size()));
    }

    public Vector2D getFreeJunglePosition(){
        return this.jungle.getFreeJunglePosition();
    }

    public Vector2D getFreeSavannaPosition(){
        System.out.println("savanna "+this.freeSavannaPositions);
        if(this.freeSavannaPositions.size()>0) {
            return freeSavannaPositions.remove(this.random.nextInt(this.freeSavannaPositions.size()));
        }
        return null;
    }

    public boolean canMoveTo(Vector2D position) {
        return position.follows(lowerLeft) && position.precedes(upperRight) && !isOccupied(position);
    }


    public boolean isOccupied(Vector2D position) {
        if (objectAt(position)!=null)
            return true;
        return false;
    }

    public boolean isOccupiedByAnimal(Vector2D position) {
        if (animalAt(position)!=null)
            return true;
        return false;
    }

    public boolean isOccupiedByPlant(Vector2D position) {
        if (plantAt(position)!=null)
            return true;
        return false;
    }

    public Object objectAt(Vector2D position) {
        Object animalAtPosition =  animalAt(position);
        if(animalAtPosition != null) {
            return animalAtPosition;
        }
        else if(deadAnimalAt(position)!=null){
            return deadAnimalAt(position);
        }
        else{
            return plantAt(position);
        }
    }

    public Object animalAt(Vector2D position) {
        if(this.animals!=null) {
            if(this.animals.get(position)!=null){
                //return this.animals.get(position).first();
                return this.animals.get(position).get(0);
            }
        }

        return null;
    }

    public Object deadAnimalAt(Vector2D position){
        //if(this.diedToday!=null){
            return this.diedToday.get(position);
//        }
//        return null;
    }

    public Object plantAt(Vector2D position) {
        if(this.plants!=null) {
            return this.plants.get(position);
        }
        return null;
    }

    public boolean inJungle(Vector2D position){
        return this.jungle.inJungle(position);
    }

//    @Override
//    public void positionEnergyChanged(Vector2D oldPosition, int deltaEnergy, Animal animal) {
//        //System.out.println(this.animals.get(oldPosition));
//        //System.out.println(animal);
//        this.animals.get(oldPosition).remove(animal);
//        //System.out.println(this.animals.get(oldPosition));
//        if(this.animals.get(oldPosition).isEmpty()){
//            this.animals.remove(oldPosition);
//        }
//        animal.updateEnergy(deltaEnergy);
//        putAnimal(animal);
//    }

    @Override
    public void positionChanged(Vector2D oldPosition,  Animal animal) {
        //System.out.println(this.animals.get(oldPosition));
        //System.out.println(animal);
        this.animals.get(oldPosition).remove(animal);
        //System.out.println(this.animals.get(oldPosition));
        if(this.animals.get(oldPosition).isEmpty()){
            this.animals.remove(oldPosition);
        }

        if(this.objectAt(oldPosition)==null){
            if(this.jungle.inJungle(oldPosition)){
                this.jungle.addFreeJunglePosition(oldPosition);
            }
            else{
                this.addFreeSavannaPosition(oldPosition);
            }
        }
        putAnimal(animal);
    }


    @Override
    public void animalBorn(Animal child) {
        this.place(child);
    }

    @Override
    public void animalDied(Animal animal) {
        //System.out.println(this.animals.get(animal.getPosition()).first());
        //System.out.println(animal.getPosition());
        //System.out.println(this.animals.get(animal.getPosition()));
        this.animals.get(animal.getPosition()).remove(animal);
        if(this.animals.get(animal.getPosition()).isEmpty()){
            this.animals.remove(animal.getPosition());
        }
        this.diedToday.put(animal.getPosition(), animal);
        this.animalsNumber--;
        if(this.jungle.inJungle(animal.getPosition())){
            this.jungle.addFreeJunglePosition(animal.getPosition());
        }
        else{
            this.addFreeSavannaPosition(animal.getPosition());
        }
        //this.freePositions.add(animal.getPosition());
        //System.out.println(this.diedToday);
    }
}
