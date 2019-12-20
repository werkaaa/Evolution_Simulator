package project;

import java.util.HashMap;
import java.util.List;

public class Data{
    private final Map map;
    private int animalsNumber;
    private int plantsNumber;
    private int date;
    private int averageEnergy;
    private int averageChildrenNumber;
    private int deadAnimalsNumber;
    private int deadAnimalsAverageAge;
    private int allTheEpochsTheyLived;
    private java.util.Map<Genom, Integer> genomes = new HashMap<Genom, Integer>();
    private Genom dominantGenome;
    private int appearanceOfDominantGenome;
    private Animal selectedAnimal;
    private int selectedSuccessors;
    private int selectedChildren;
    private int selectedLastDay;

    public Data(Map map, int initialAnimalsNumber, int initialPlantsNumber){
        this.map = map;
        this.animalsNumber = initialAnimalsNumber;
        this.plantsNumber = initialPlantsNumber;
        this.date = 0;
        this.averageEnergy = 0;
        this.averageChildrenNumber = 0;
        this.deadAnimalsNumber = 0;
        this.deadAnimalsAverageAge = 0;
        this.allTheEpochsTheyLived = 0;
        this.dominantGenome = new Genom(true);
        this.appearanceOfDominantGenome = 0;
        this.selectedAnimal = null;
        this.selectedSuccessors = 0;
        this.selectedChildren = 0;
        this.selectedLastDay = 0;

    }

    public Object getData(String parameter){
        switch (parameter){
            case "animalsNumber" :
                return this.animalsNumber;

            case "plantsNumber" :
                return this.plantsNumber;

            case "date" :
                return this.date;

            case "averageEnergy" :
                return this.averageEnergy;

            case "averageChildrenNumber" :
                return this.averageChildrenNumber;

            case "deadAnimalsAverageAge" :
                return this.deadAnimalsAverageAge;

            case "dominantGenome" :
                return this.dominantGenome;

            case "appearanceOfGenome" :
                return this.appearanceOfDominantGenome;

            case "chosenEnergy":
                if(this.selectedAnimal!=null) {
                    return this.selectedAnimal.getEnergy();
                }
                return "-";

            case "chosenGenome":
                if(this.selectedAnimal!=null) {
                    return this.selectedAnimal.getGenom();
                }
                return "-";

            case "chosenChildren":
                if(this.selectedAnimal!=null) {
                    return this.selectedAnimal.getChildrenNumber()-this.selectedChildren;
                }
                return "-";

            case "chosenSuccessors":
                if(this.selectedAnimal!=null) {
                    return this.selectedSuccessors;
                }
                return "-";

            case "chosenDeath":
                if(this.selectedAnimal!=null && !this.selectedAnimal.isAlive()) {
                    return this.selectedLastDay;
                }
                return "-";

            default:
                return 0;
        }
    }

    public void updateDeadAnimalsAverageAge(){
        this.updateDeadAnimalsNumber();
        this.updateAllTheEpochsTheyLived();
        if(this.deadAnimalsNumber!=0) {
            this.deadAnimalsAverageAge = this.allTheEpochsTheyLived / this.deadAnimalsNumber;
        }

    }

    public void updateDeadAnimalsNumber(){
        for (List<Animal> setOfAnimals : map.diedToday.values()) {
            this.deadAnimalsNumber+=setOfAnimals.size();
        }
    }

    public void updateAllTheEpochsTheyLived(){
        for (List<Animal> setOfAnimals : map.diedToday.values()) {
            for(Animal animal : setOfAnimals) {
                this.allTheEpochsTheyLived += animal.getAge();
            }
        }
    }

    public void updateAnimalsNumber(int deltaAnimals){
        this.animalsNumber+=deltaAnimals;
    }

    public void updatePlantsNumber(int deltaPlants){
        this.plantsNumber+=deltaPlants;
    }

    public void updateDate(){
        this.date+=1;
    }

    public void updateSelectedSuccessors(){
        this.selectedSuccessors+=1;
    }

    private void updateAverageEnergy(){
        if(this.animalsNumber!=0) {
            int allEnergy = 0;
            for (List<Animal> setOfAnimals : map.animals.values()) {
                for (Animal element : setOfAnimals) {
                    allEnergy += element.getEnergy();
                }
            }
            this.averageEnergy = allEnergy / this.animalsNumber;
        }
        else{
            this.averageEnergy=0;
        }
    }
    private void updateAverageChildrenNumber(){
        if(this.animalsNumber!=0) {
            int allChildren = 0;
            for (List<Animal> setOfAnimals : map.animals.values()) {
                for (Animal element : setOfAnimals) {
                    allChildren += element.getChildrenNumber();
                    System.out.println(element.getChildrenNumber());
                }
            }
            this.averageChildrenNumber = allChildren / this.animalsNumber;
        }
        else{
            this.averageChildrenNumber=0;
        }
    }

    public void removeGenome(Genom genome){
        if(this.genomes.get(genome)==1){
            this.genomes.remove(genome);
        }
        else{
            this.genomes.put(genome, this.genomes.get(genome)-1);
        }
    }

    public void addGenome(Genom genome){
        if(this.genomes.get(genome)==null){
            this.genomes.put(genome, 1);
        }
        else{
            this.genomes.put(genome, this.genomes.get(genome)+1);
        }
    }

    private void updateDominantGenome(){
        Genom maxGenome = new Genom(true);
        int appearanceNumber = 0;
        for(Genom genome : this.genomes.keySet()){
            if(this.genomes.get(genome)>appearanceNumber){
                System.out.println("dupda");
                maxGenome = genome;
                appearanceNumber = this.genomes.get(genome);
            }
        }
        this.dominantGenome = maxGenome;
        this.appearanceOfDominantGenome = appearanceNumber;
    }


    public void updateSelectedAnimal(Animal animal){
        if(this.selectedAnimal!=null) {
            this.selectedAnimal.removeSelection();
            for (List<Animal> setOfAnimals : map.diedToday.values()) {
                for(Animal otherAnimal : setOfAnimals) {
                    otherAnimal.removeSuccessor();
                }
            }
        }
        this.selectedSuccessors = 0;
        this.selectedAnimal = animal;
        this.selectedChildren = animal.getChildrenNumber();
    }

    public Animal getSelectedAnimal(){
        return this.selectedAnimal;
    }

    public void updateData(){
        this.updateDate();
        this.updateDeadAnimalsAverageAge();
        this.updateAverageEnergy();
        this.updateAverageChildrenNumber();
        this.updateDominantGenome();
    }

    public void updateSelectedLastDay(){
        this.selectedLastDay = this.date;
    }
}
