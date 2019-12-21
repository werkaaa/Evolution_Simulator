package project;

import java.util.*;

import static java.lang.Math.sqrt;


public class Map implements IPositionChangeObserver{
    private Random random = new Random();
    private java.util.Map<Vector2D, List<Animal>> animals = new HashMap<>();

    private java.util.Map<Vector2D, Plant> plants = new HashMap<>();

    private List<Vector2D> freeSavannaPositions;
    private java.util.Map<Vector2D, List<Animal>> diedToday = new HashMap<>();

    private final Vector2D lowerLeft;
    private final Vector2D upperRight;
    private Jungle jungle;


    private final int startEnergy;
    private final int plantEnergy;
    private final int moveEnergy;
    private final double jungleRatio;
    private final int maxEnergy;

    private Data mapData;

    public Map(int width, int height, int startEnergy, int plantEnergy, int moveEnergy, double jungleRation, int initialAnimalsNumber, int initialPlantsNumber, int maxEnergy) {
        this.mapData = new Data(this, 0, initialPlantsNumber);

        this.lowerLeft = new Vector2D(0,0);
        this.upperRight = new Vector2D(width-1, height-1);

        this.startEnergy = startEnergy;
        this.plantEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
        this.jungleRatio = jungleRation;
        this.maxEnergy = maxEnergy;

        int jungleWidth = (int) (sqrt(jungleRation)*width);
        int jungleHeight = (int) (sqrt(jungleRation)*height);

        this.jungle = new Jungle(new Vector2D((width - jungleWidth)/2, (height - jungleHeight)/2), new Vector2D((width + jungleWidth)/2-1, (height + jungleHeight)/2-1));

        this.freeSavannaPositions = this.lowerLeft.allPointsBetween(this.upperRight);
        for(Vector2D position : this.jungle.freePositions){
            this.freeSavannaPositions.remove(position);
        }

        //place Adams and Eves on the map
        for(int i=0; i<initialAnimalsNumber; i++) this.animalBorn(new Animal(this, this.startEnergy));
        for(int i=0; i<initialPlantsNumber; i++) this.place(new Plant(this));
    }

    public Vector2D getLowerLeft(){
        return this.lowerLeft;
    }

    public Vector2D getUpperRight(){
        return this.upperRight;
    }

    public java.util.Map<Vector2D, List<Animal>> getAnimals(){
        return this.animals;
    }

    public Data getMapData()
    {
        return this.mapData;
    }

    public int getMaxEnergy(){
        return this.maxEnergy;
    }

    public int getMoveEnergy() { return this.moveEnergy; }

    public java.util.Map<Vector2D, List<Animal>>getDiedToday(){
        return this.diedToday;
    }


    public Vector2D getFreePosition(){
        if(this.random.nextDouble()>this.jungleRatio){
            Vector2D randomPosition = this.getFreeSavannaPosition();
            if(randomPosition==null) {
                return this.getFreeJunglePosition();
            }
            return randomPosition;
        }
        else{
            Vector2D randomPosition = this.getFreeJunglePosition();
            if(randomPosition==null) {
                return this.getFreeSavannaPosition();
            }
            return randomPosition;
        }
    }

    public Vector2D getFreeJunglePosition(){
        return this.jungle.getFreeJunglePosition();
    }

    public Vector2D getFreeSavannaPosition(){
        if(this.freeSavannaPositions.size()>0) {
            return freeSavannaPositions.remove(this.random.nextInt(this.freeSavannaPositions.size()));
        }
        return null;
    }

    public void place(IMapElement element) {
        if(element instanceof Animal) {
            putAnimal((Animal) element);
            ((Animal) element).addObserver(this);
            //this.animalsNumber++;
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
        Collections.sort(this.animals.get(animalPosition), Collections.reverseOrder());

    }

    public void run(){
        this.mapData.updateData();
        buryAnimals();
        moveAnimals();
        feedAnimals();
        mateAnimals();
        placePlants();
    }

    public void moveAnimals(){
        List<Animal> animals = new ArrayList<>();
        for(List<Animal> setOfAnimals : this.animals.values()){
            for(Animal element : setOfAnimals) {
                animals.add(element);
            }
        }

        for(int iter = 0; iter<animals.size(); iter++){
            animals.get(iter).move();
        }
    }

    public void selectAnimal(int x, int y){
        Object element = objectAt(new Vector2D(x, y));
        if(element instanceof Animal){
            ((Animal) element).select();
            this.mapData.updateSelectedAnimal((Animal) element);
            this.mapData.updateData();
        }


    }

    public void mateAnimals(){
        //store parents in the arrays
        List<Animal> mothers = new ArrayList<>();
        List<Animal> fathers = new ArrayList<>();
        for(List<Animal> setOfAnimals : this.animals.values()){
            Animal mother = setOfAnimals.get(0);
            Animal father = null;
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

    public void feedAnimals() {
        for (List<Animal> setOfAnimals : this.animals.values()) {
            Animal maxAnimal = setOfAnimals.get(0);

            Plant plant = this.plants.remove(maxAnimal.getPosition());
            if (plant != null) {
                int maxEnergyAnimals = 0;
                while(maxEnergyAnimals<setOfAnimals.size() && setOfAnimals.get(maxEnergyAnimals)!=null && setOfAnimals.get(maxEnergyAnimals).getEnergy()==maxAnimal.getEnergy()){
                    maxEnergyAnimals++;
                }
                for(int i = 0; i<maxEnergyAnimals; i++){
                    setOfAnimals.get(i).eat(this.plantEnergy/maxEnergyAnimals);
                }
                this.mapData.updatePlantsNumber(-1);
                if (this.jungle.inJungle(maxAnimal.getPosition())) {
                    this.jungle.addFreeJunglePosition(maxAnimal.getPosition());
                } else {
                    this.addFreeSavannaPosition(maxAnimal.getPosition());
                }
            }
        }
    }

    private void addFreeSavannaPosition(Vector2D position){
        if(!this.freeSavannaPositions.contains(position)) {
            this.freeSavannaPositions.add(position);
        }
    }

    public void buryAnimals() {
        this.diedToday.clear();
    }

    public void placePlants() {
        Vector2D junglePosition = this.getFreeJunglePosition();
        if(junglePosition!=null) {
            this.place(new Plant(junglePosition, this));
            this.mapData.updatePlantsNumber(1);
        }
        Vector2D savannaPosition = this.getFreeSavannaPosition();
        if(savannaPosition!=null) {
            this.place(new Plant(savannaPosition, this));
            this.mapData.updatePlantsNumber(1);
        }
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
                return this.animals.get(position).get(0);
            }
        }

        return null;
    }

    public Object deadAnimalAt(Vector2D position){
        if(this.diedToday!=null) {
            if(this.diedToday.get(position)!=null) {
                return this.diedToday.get(position).get(0);
            }
        }
        return null;
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

    @Override
    public void positionChanged(Vector2D oldPosition,  Animal animal) {
        this.animals.get(oldPosition).remove(animal);
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
        this.mapData.updateAnimalsNumber(1);
        this.mapData.addGenome(child);
        if(child.isSuccessor()) this.mapData.updateSelectedSuccessors();
    }

    @Override
    public void animalDied(Animal animal) {
        Vector2D deadAnimalPosition = animal.getPosition();
        this.animals.get(deadAnimalPosition).remove(animal);
        if(this.animals.get(deadAnimalPosition).isEmpty()){
            this.animals.remove(deadAnimalPosition);
        }

        if(deadAnimalAt(deadAnimalPosition)==null){
            this.diedToday.put(deadAnimalPosition, new ArrayList<Animal>());
        }
            this.diedToday.get(deadAnimalPosition).add(animal);

        this.mapData.updateAnimalsNumber(-1);
        this.mapData.removeGenome(animal);
        if(animal.isChosen()) this.mapData.updateSelectedLastDay();

        if(this.jungle.inJungle(animal.getPosition())){
            this.jungle.addFreeJunglePosition(animal.getPosition());
        }
        else{
            this.addFreeSavannaPosition(animal.getPosition());
        }

    }
}
