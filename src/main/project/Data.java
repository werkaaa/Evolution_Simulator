package project;

import java.util.List;

public class Data {
    private final Map map;
    private int animalsNumber;
    private int plantsNumber;
    private int date;
    private int averageEnergy;
    private int averageChildrenNumber;
    private int deadAnimalsNumber;
    private int deadAnimalsAverageAge;
    private int allTheEpochsTheyLived;

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

    }

    public int getData(String parameter){
        switch (parameter){
            case "animalsNumber" :
                return this.animalsNumber;

            case "plantsNumber" :
                return this.plantsNumber;

            case "date" :
                return this.date;

            case "averageEnergy" :
                this.updateAverageEnergy();
                return this.averageEnergy;

            case "averageChildrenNumber" :
                this.updateAverageChildrenNumber();
                return this.averageChildrenNumber;

            case "deadAnimalsAverageAge" :
                //TODO: needs to be updated
                return this.deadAnimalsAverageAge;

            default:
                return 0;
        }
    }

    public void updateDeadAnimalsAverageAge(){
        this.updateDeadAnimalsNumber();
        //TODO: update

    }

    public void updateDeadAnimalsNumber(){
        //TODO:
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
}
