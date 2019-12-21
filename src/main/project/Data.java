package project;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    public JTable generalTable;
    public JTable chosenTable;
    public JTable dateTable;

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

        this.initializeGeneralTable();
        this.initializeChosenTable();
    }

    public Object getDate(){

        return this.dateTable;
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
               // {"Appearance of genome", this.dominantGenome},


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

    public Object getChosenSuccessors(){
        return this.selectedAnimal == null ? "-":this.selectedSuccessors;
    }

    public Object getChosenChildren(){
        return this.selectedAnimal == null ? "-":this.selectedChildren;
    }

    public Object getChosenLastDay(){
        return this.selectedAnimal == null ? "-":this.selectedLastDay;
    }

    public Object getChosenEnergy(){
        return this.selectedAnimal==null ? "-":this.selectedAnimal.getEnergy();
    }

    public Object getChosenGenome(){
        return this.selectedAnimal==null ? "-":this.selectedAnimal.getGenom();
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
        this.updateGeneralTable();
        this.updateChosenTable();
    }

    public void updateGeneralTable(){
        this.generalTable.setValueAt(this.animalsNumber, 0, 1);
        this.generalTable.setValueAt(this.plantsNumber, 1, 1);
        this.generalTable.setValueAt(this.averageEnergy, 2, 1);
        this.generalTable.setValueAt(this.averageChildrenNumber, 3, 1);
        this.generalTable.setValueAt(this.animalsNumber, 4, 1);
        this.generalTable.setValueAt(this.dominantGenome, 5, 1);
      //  this.generalTable.setValueAt(this.appearanceOfDominantGenome, 6, 1);

        this.dateTable.setValueAt(this.date, 0, 0);
    }

    public void updateChosenTable(){
        this.chosenTable.setValueAt(this.getChosenEnergy(), 0, 1);
        this.chosenTable.setValueAt(this.getChosenGenome(), 1, 1);
        this.chosenTable.setValueAt(this.getChosenChildren(), 2, 1);
        this.chosenTable.setValueAt(this.getChosenSuccessors(), 3, 1);
        this.chosenTable.setValueAt(this.getChosenLastDay(), 4, 1);
    }

    public void updateSelectedLastDay(){
        this.selectedLastDay = this.date;
    }

    public void save(){
        JSONObject obj = new JSONObject();
        int savedDay = (int) this.dateTable.getValueAt(0, 0);
        obj.put("Date", savedDay);
        for(int i = 0; i<6; i++){
            obj.put(this.generalTable.getValueAt(i, 0), this.generalTable.getValueAt(i, 1));
        }

        try (FileWriter file = new FileWriter("statistics/dataAfter"+savedDay+"Epochs.json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(obj);


    }

    public void save2(int mapNumber){


        JSONParser parser = new JSONParser();
        int savedDay = (int) this.dateTable.getValueAt(0, 0);
        String fileName = "statistics/dataAfter"+savedDay+"Epochs.json";
        try {
            Object obj = parser.parse(new FileReader(fileName));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject data = new JSONObject();
            data.put("Date", data);

            for(int i = 0; i<6; i++){
                data.put(this.generalTable.getValueAt(i, 0), this.generalTable.getValueAt(i, 1));
            }

            jsonObject.put("Map"+1, data);
            FileWriter file = new FileWriter(fileName);
            file.write(jsonObject.toJSONString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

