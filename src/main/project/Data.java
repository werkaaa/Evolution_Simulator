package project;

import javax.swing.*;
import java.util.ArrayList;
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
    private java.util.Map<Genome, List<Animal>> genomes = new HashMap<>();
    private java.util.Map<Genome, Integer> allDominantGenomes = new HashMap<>();
    private Genome dominantGenome;
    private int appearanceOfDominantGenome;
    private List<Animal> dominantAnimals;
    private Animal chosenAnimal;
    private int chosenSuccessors;
    private int chosenChildrenAtTheBeginning;
    private int chosenLastDay;
    private JTable generalTable;
    private JTable chosenTable;
    private JTable dateTable;
    private JTable averageTable;

    private long allAnimalsNumber;
    private long allPlantsNumber;
    private long allAvgEnergy;
    private long allAvgChildrenNumber;
    private long allAvgDeadAnimalsAge;

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
        this.dominantGenome = new Genome(true);
        this.dominantAnimals = new ArrayList<>();
        this.appearanceOfDominantGenome = 0;
        this.chosenAnimal = null;
        this.chosenSuccessors = 0;
        this.chosenChildrenAtTheBeginning = 0;
        this.chosenLastDay = 0;
        this.allPlantsNumber = 0;
        this.allAnimalsNumber = 0;
        this.allAvgEnergy = 0;
        this.allAvgChildrenNumber = 0;
        this.allAvgDeadAnimalsAge = 0;

        this.initializeGeneralTable();
        this.initializeChosenTable();
        this.initializeAverageTable();
    }


    private void initializeGeneralTable(){
        String[] columnNames = {"Property", "Value"};

        Object[][] data = {
                {"Number of animals", this.animalsNumber},
                {"Number of plants", this.plantsNumber},
                {"Avg. energy", this.averageEnergy},
                {"Avg. children number", this.averageChildrenNumber},
                {"Avg. dead animals age", this.deadAnimalsAverageAge},
                {"Dominant genome", this.dominantGenome},
                {"Appearance of genome", this.dominantGenome},


        };
        this.generalTable = new JTable(data, columnNames);
        this.generalTable.getColumnModel().getColumn(1).setMinWidth(200);

        String[] dateName = {"Date"};

        Object[][] dateData = {
                {this.date}
        };
        this.dateTable = new JTable(dateData, dateName);
    }

    private void initializeChosenTable(){
        String[] columnNames = {"Property", "Value"};

        Object[][] data = {
                {"Energy of chosen one", this.getChosenEnergy()},
                {"Genome of chosen one", this.getChosenGenome()},
                {"Children of chosen one", this.getChosenChildren()},
                {"Successors of chosen one", this.getChosenSuccessors()},
                {"Last day of chosen one", this.getChosenLastDay()},

        };
        this.chosenTable = new JTable(data, columnNames);
        this.chosenTable.getColumnModel().getColumn(1).setMinWidth(200);
    }

    private void initializeAverageTable(){
        String[] columnNames = {"Property", "Value"};

        Object[][] data = {
                {"Avg. number of animals", this.allAnimalsNumber},
                {"Avg. number of plants", this.allPlantsNumber},
                {"Avg. energy", this.allAvgEnergy},
                {"Avg. children number", this.averageChildrenNumber},
                {"Avg. dead animals age", this.deadAnimalsAverageAge},
                {"Most dominant genome", "-"},

        };
        this.averageTable = new JTable(data, columnNames);
    }

    public JTable getDate(){

        return this.dateTable;
    }

    public Object getChosenTable(){
        return this.chosenTable;
    }

    public Object getGeneralTable(){
        return this.generalTable;
    }

    public JTable getAverageTable(){
        return this.averageTable;
    }

    public Animal getChosenAnimal(){
        return this.chosenAnimal;
    }

    public Object getChosenSuccessors(){
        return this.chosenAnimal == null ? "-":this.chosenSuccessors;
    }

    public Object getChosenChildren(){
        return this.chosenAnimal == null ? "-":this.chosenAnimal.getChildrenNumber() - this.chosenChildrenAtTheBeginning;
    }

    public Object getChosenLastDay(){
        return this.chosenAnimal == null || this.chosenLastDay ==0 ? "-":this.chosenLastDay;
    }

    public Object getChosenEnergy(){
        return this.chosenAnimal ==null ? "-":this.chosenAnimal.getEnergy();
    }

    public Object getChosenGenome(){
        return this.chosenAnimal ==null ? "-":this.chosenAnimal.getGenome();
    }

    public java.util.Map<Genome, List<Animal>> getGenomes() {
        return this.genomes;
    }

    public Genome getDominantGenome(){ return this.dominantGenome; }

    private Object getAppearanceOfDominantGenome(){
        if(this.appearanceOfDominantGenome==1){
            return "-";
        }
        return this.appearanceOfDominantGenome;
    }

    public long getAvgAnimalsNumber(){
        if(this.date>0) {
            return this.allAnimalsNumber / this.date;
        }
        return 0;
    }

    public long getAvgPlantsNumber(){
        if(this.date>0) {
            return this.allPlantsNumber / this.date;
        }
        return 0;
    }

    public long getAvgEnergy(){
        if(this.date>0) {
            return this.allAvgEnergy / this.date;
        }
        return 0;
    }

    public long getAverageChildrenNumber(){
        if(this.date>0) {
            return this.allAvgChildrenNumber / this.date;
        }
        return 0;
    }

    public long getAvgDeadAnimalsAge(){
        if(this.date>0) {
            return this.allAvgDeadAnimalsAge / this.date;
        }
        return 0;
    }

    public Genome getMostDominantGenome(){
        Genome mostDominant = new Genome(true);
        int daysOfDomination = 0;
        for(Genome genome: this.allDominantGenomes.keySet()){
            if (this.allDominantGenomes.get(genome) > daysOfDomination){
                daysOfDomination = this.allDominantGenomes.get(genome);
                mostDominant = genome;
            }
        }
        return mostDominant;
    }

    public void updateDeadAnimalsAverageAge(){
        this.updateDeadAnimalsNumber();
        this.updateAllTheEpochsTheyLived();
        if(this.deadAnimalsNumber!=0) {
            this.deadAnimalsAverageAge = this.allTheEpochsTheyLived / this.deadAnimalsNumber;
        }
    }

    public void updateDeadAnimalsNumber(){
        for (List<Animal> setOfAnimals : map.getDiedToday().values()) {
            this.deadAnimalsNumber+=setOfAnimals.size();
        }
    }

    public void updateAllTheEpochsTheyLived(){
        for (List<Animal> setOfAnimals : map.getDiedToday().values()) {
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
        this.chosenSuccessors +=1;
    }

    private void updateAverageEnergy(){
        if(this.animalsNumber!=0) {
            int allEnergy = 0;
            for (List<Animal> setOfAnimals : map.getAnimals().values()) {
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
            for (List<Animal> setOfAnimals : map.getAnimals().values()) {
                for (Animal element : setOfAnimals) {
                    allChildren += element.getChildrenNumber();
                }
            }
            this.averageChildrenNumber = allChildren / this.animalsNumber;
        }
        else{
            this.averageChildrenNumber=0;
        }
    }


    public void removeGenome(Animal animal){
        if(this.genomes.get(animal.getGenome()).size()==1){
            this.genomes.remove(animal.getGenome());
            if(animal.getGenome().equals(this.dominantGenome)){
                this.dominantGenome = new Genome(true);
            }
        }
        else{
            this.genomes.get(animal.getGenome()).remove(animal);
        }
    }

    public void addGenome(Animal animal){
        if(this.genomes.get(animal.getGenome())==null){
            this.genomes.put(animal.getGenome(), new ArrayList<>());
        }
        this.genomes.get(animal.getGenome()).add(animal);
    }

    private void updateDominantGenome(){
        Genome maxGenome = new Genome(true);
        int appearanceNumber = 1;
        for(Genome genome : this.genomes.keySet()){
            if(this.genomes.get(genome).size()>appearanceNumber){
                maxGenome = genome;
                appearanceNumber = this.genomes.get(genome).size();
            }
        }
        this.dominantGenome = maxGenome;
        this.appearanceOfDominantGenome = appearanceNumber;
    }


    public void updateSelectedAnimal(Animal animal){
        if(this.chosenAnimal !=null) {
            this.chosenAnimal.removeSelection();
            for (List<Animal> setOfAnimals : map.getDiedToday().values()) {
                for(Animal otherAnimal : setOfAnimals) {
                    otherAnimal.removeSuccessor();
                }
            }
        }
        this.chosenSuccessors = 0;
        this.chosenAnimal = animal;
        this.chosenChildrenAtTheBeginning = animal.getChildrenNumber();
        this.chosenLastDay = 0;
    }

    public void updateData(){
        this.updateDate();
        this.updateDeadAnimalsAverageAge();
        this.updateAverageEnergy();
        this.updateAverageChildrenNumber();
        this.updateDominantGenome();
        this.updateGeneralTable();
        this.updateChosenTable();
        this.allAvgEnergy+=this.averageEnergy;
        this.allAvgChildrenNumber+=this.averageChildrenNumber;
        this.allAvgDeadAnimalsAge+=this.deadAnimalsAverageAge;
        this.allAnimalsNumber+=this.animalsNumber;
        this.allPlantsNumber+=this.plantsNumber;
        if(!this.dominantGenome.isEmpty()){
            if(this.allDominantGenomes.get(this.dominantGenome)==null){
                this.allDominantGenomes.put(this.dominantGenome, 1);
            }
            else{
                this.allDominantGenomes.put(this.dominantGenome, this.allDominantGenomes.get(this.dominantGenome)+1);
            }
        }
    }

    public void updateGeneralTable(){
        this.generalTable.setValueAt(this.animalsNumber, 0, 1);
        this.generalTable.setValueAt(this.plantsNumber, 1, 1);
        this.generalTable.setValueAt(this.averageEnergy, 2, 1);
        this.generalTable.setValueAt(this.averageChildrenNumber, 3, 1);
        this.generalTable.setValueAt(this.deadAnimalsAverageAge, 4, 1);
        this.generalTable.setValueAt(this.dominantGenome, 5, 1);
        this.generalTable.setValueAt(this.getAppearanceOfDominantGenome(), 6, 1);

        this.dateTable.setValueAt(this.date, 0, 0);
    }

    public void updateChosenTable(){
        this.chosenTable.setValueAt(this.getChosenEnergy(), 0, 1);
        this.chosenTable.setValueAt(this.getChosenGenome(), 1, 1);
        this.chosenTable.setValueAt(this.getChosenChildren(), 2, 1);
        this.chosenTable.setValueAt(this.getChosenSuccessors(), 3, 1);
        this.chosenTable.setValueAt(this.getChosenLastDay(), 4, 1);
    }

    public void updateAverageTable(){
        this.averageTable.setValueAt(this.getAvgAnimalsNumber(), 0, 1);
        this.averageTable.setValueAt(this.getAvgPlantsNumber(), 1, 1);
        this.averageTable.setValueAt(this.getAvgEnergy(), 2, 1);
        this.averageTable.setValueAt(this.getAverageChildrenNumber(), 3, 1);
        this.averageTable.setValueAt(this.getAvgDeadAnimalsAge(), 4, 1);
        this.averageTable.setValueAt(this.getMostDominantGenome(), 5, 1);

    }

    public void updateSelectedLastDay(){
        this.chosenLastDay = this.date;
    }

}

