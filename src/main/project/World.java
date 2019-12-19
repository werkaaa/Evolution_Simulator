package project;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Integer.min;

public class World implements ActionListener {

    AtomicBoolean runMap = new AtomicBoolean(true);

//    private final JButton restartButton;
//    private final JButton pauseButton;
    private Map mapA;
    private Map mapB;
    private MapPanel panelA;
    private MapPanel panelB;
    private ControlPanel generalPanel;
    private final int mapBoardWidth = 600;
    private final int mapBoardHeight = 600;

    private final int controlPanelWidth = 200;

    private int width, height, startEnergy, plantEnergy, moveEnergy, initialAnimalsNumber, initialPlantsNumber, maximalEnergy = 100;
    private double jungleRatio;

    private java.util.Map<Vector2D, JLabel> gridsA = new HashMap<>();
    private java.util.Map<Vector2D, JLabel> gridsB = new HashMap<>();
    private java.util.Map<String, Icon> pictures = new HashMap<>();
    //private java.util.Map<String, ImageIcon> pictures = new HashMap<>();

    private void runSimulation() throws InterruptedException {
        while(true){
        //for(int i= 0; i<0; i++){
            this.mapA.run();
            this.mapB.run();
            this.generateMap(this.mapA, this.gridsA);
            this.generateMap(this.mapB, this.gridsB);
            this.panelA.update();
            this.panelB.update();
            this.generalPanel.update();
            Thread.sleep(100);
            //System.out.println(this.mapA.animals);
        }
    }

//    public World(){
//
//    }

    public World(){
        this.readInput();
        this.mapA = new Map(this.width, this.height, this.startEnergy, this.plantEnergy, this.moveEnergy, this.jungleRatio, this.initialAnimalsNumber, this.initialPlantsNumber);
        this.mapB = new Map(this.width, this.height, this.startEnergy, this.plantEnergy, this.moveEnergy, this.jungleRatio, this.initialAnimalsNumber, this.initialAnimalsNumber);

        JFrame frame = new JFrame();

        JPanel mapBoardA = new JPanel();
        mapBoardA.setLayout(new GridLayout(height, width, 0, 0));
        mapBoardA.setSize(mapBoardWidth, mapBoardHeight);

        JPanel mapBoardB = new JPanel();
        mapBoardB.setLayout(new GridLayout(height, width, 0, 0));
        mapBoardB.setSize(mapBoardWidth, mapBoardHeight);

        for(int y = height - 1; y >= 0; y--) {
            for (int x = 0; x <= width - 1; x++) {
                JLabel labelA = new JLabel("", JLabel.CENTER);
                mapBoardA.add(labelA);
                gridsA.put(new Vector2D(x, y), labelA);

                JLabel labelB = new JLabel("", JLabel.CENTER);
                mapBoardB.add(labelB);
                gridsB.put(new Vector2D(x, y), labelB);
            }
        }

        //control panel section
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setSize(this.controlPanelWidth, this.mapBoardHeight);
        this.panelA = new MapPanel(this.controlPanelWidth, this.mapBoardHeight/3, this.mapA.getMapData());
        this.panelB = new MapPanel(this.controlPanelWidth, this.mapBoardHeight/3, this.mapB.getMapData());
        this.generalPanel = new ControlPanel(this.controlPanelWidth, this.mapBoardHeight/3, this.mapB.getMapData());
        panel.add(this.generalPanel);
        panel.add(this.panelA);
        panel.add(this.panelB);

        frame.setSize(2*this.mapBoardWidth+this.controlPanelWidth, this.mapBoardHeight);
        frame.add(mapBoardA, BorderLayout.WEST);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(mapBoardB, BorderLayout.EAST);
        this.getPictures();
        this.generateMap(this.mapA, this.gridsA);
        this.generateMap(this.mapB, this.gridsB);
        this.panelA.update();
        this.panelB.update();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void getPictures() {
        int pictureWidth = mapBoardWidth/ width;
        int pictureHeight = mapBoardHeight/height;

        //List<String> things_to_show = new ArrayList<>(Arrays.asList("food_savanna", "animal_savanna", "food_jungle", "animal_jungle", "savanna_background", "jungle_background", "dead_animal_savanna", "dead_animal_jungle"));
        List<String> things_to_show = new ArrayList<>(Arrays.asList("food", "dead_animal", "4_animal", "3_animal", "2_animal", "1_animal", "0_animal"));
        List<String> backgrounds = new ArrayList<>(Arrays.asList("savanna", "jungle"));


//        for(String elementName : backgrounds) {
//            //String name = elementName+".png";
//            ImageIcon element = new ImageIcon(getClass().getResource(elementName+".png"));
//            Image picture = element.getImage().getScaledInstance(pictureWidth, pictureHeight, java.awt.Image.SCALE_SMOOTH);
//            this.pictures.put(elementName, new ImageIcon(picture));
//
//        }
        for(String backgroundName : backgrounds) {
            //String name = elementName+".png";
            //ImageIcon background = new ImageIcon(getClass().getResource(backgroundName+".png"));
            ImageIcon background = new ImageIcon(getClass().getResource(backgroundName+".png"));
            Image backgroundPicture = background.getImage().getScaledInstance(pictureWidth, pictureHeight, java.awt.Image.SCALE_SMOOTH);
            this.pictures.put(backgroundName, new ImageIcon(backgroundPicture));

            for(String elementName : things_to_show) {
                ImageIcon element = new ImageIcon(getClass().getResource(elementName+".png"));
                Image elementPicture = element.getImage().getScaledInstance(pictureWidth, pictureHeight, java.awt.Image.SCALE_SMOOTH);
                element = new ImageIcon(elementPicture);
                Icon picture = new CompoundIcon(CompoundIcon.Axis.Z_AXIS, 0, CompoundIcon.CENTER, CompoundIcon.CENTER, new ImageIcon(backgroundPicture), element);
                this.pictures.put(elementName+"_"+backgroundName, picture);
            }

        }

    }

//    public void getPictures() {
//        int pictureWidth = mapBoardWidth/ width;
//        int pictureHeight = mapBoardHeight/height;
//
//        List<String> things_to_show = new ArrayList<>(Arrays.asList("food_savanna", "animal_savanna", "food_jungle", "animal_jungle", "savanna_background", "jungle_background", "dead_animal_savanna", "dead_animal_jungle"));
//
//
//
//        for(String elementName : things_to_show) {
//            //String name = elementName+".png";
//            ImageIcon element = new ImageIcon(getClass().getResource(elementName+".png"));
//            Image picture = element.getImage().getScaledInstance(pictureWidth, pictureHeight, java.awt.Image.SCALE_SMOOTH);
//            this.pictures.put(elementName, new ImageIcon(picture));
//
//        }
//
//    }

    public void generateMap(Map map, java.util.Map<Vector2D, JLabel> grids) {
        for(int y = 0; y <this.height; y++) {
            for(int x = 0; x < width; x++) {
                Vector2D position = new Vector2D(x, y);
                Object mapElement = map.objectAt(position);
                JLabel label = grids.get(position);
                String background = map.inJungle(position) ? "jungle" : "savanna";
                String objectType = mapElement==null ? "" : (mapElement instanceof Animal ? "animal_" : "food_");
                String energyLevel = mapElement instanceof Animal ? (((Animal) mapElement).alive ? String.valueOf(min(4, ((Animal) mapElement).getEnergy()/(this.maximalEnergy /5)))+"_" : "dead_"): "";
                String cellName = energyLevel+objectType+background;
                Icon element = pictures.get(cellName);
                label.setIcon(element);
            }
        }
    }

    public void readInput(){
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("resources/parameters.json"));

            JSONObject jsonObject = (JSONObject) obj;

            this.width = Integer.parseInt(jsonObject.get("width").toString());
            this.height = Integer.parseInt(jsonObject.get("height").toString());
            this.startEnergy = Integer.parseInt(jsonObject.get("startEnergy").toString());
            this.plantEnergy = Integer.parseInt(jsonObject.get("plantEnergy").toString());
            this.moveEnergy = Integer.parseInt(jsonObject.get("moveEnergy").toString());
            this.jungleRatio = Double.parseDouble(jsonObject.get("jungleRatio").toString());
            this.initialAnimalsNumber = Integer.parseInt(jsonObject.get("initialAnimalsNumber").toString());
            this.initialPlantsNumber = Integer.parseInt(jsonObject.get("initialPlantsNumber").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args){
        World simulation = new World();
        try {
            simulation.runSimulation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        runMap.compareAndSet(false, true);
    }

    // TODO: testy
    // TODO: czasami znikają zwierzęta, które się urodziły (sprawdzić to)
    // TODO: zrobić bardziej observerowo updateowanie energii w strukturze (jak?) lub zostawić sortowanie
    // TODO: Czy idealnie obserwerowo należy mapę trzymać tylko jako observera? NIE
    // TODO: dodać obserwer przy pojawieniu się wolnej pozycji
    // TODO: dwa obserwery do komórki jeden energy will change a drugi energy changed, jeden usuwa z drzewa drugi dodaje do drzewa
    // TODO: pomyśleć o atomic boolean wartość bezpieczna ze względu na wątki
    // TODO: reprezentacja energii (jak?)
    // TODO: dodać wyjątek dla zbyt wielu zwierząt
    // TODO: posprzątać kod
    // TODO: upewniić się, że zawszebierzemy zwierzaki z największą energią
    //TODO: zmienić diedToday tak, żeby mogły umierać zwierzęta na tych samych polach

    //pytania
    // TODO: CZy można aktualnizować informację o liczbie potomków na bieżąco zamiast wyświetlać ją po n epokach?


    //funkcjonalności:
    //TODO: jedzenie na pół
    //TODO: zatrzymywanie i wznawianie symulacji
    //TODO: interface do statystyk
    //TODO: dominujące genotypy
    //TODO: średnia długość życia dla martwych zwierząt
    //TODO: wskazać zwierzę i wyświetli się jego genom
    //TODO: dzieci po n-epokach
    //TODO: potomków po n-epokach
    //TODO: epoka w której zmarło
    //TODO: dwie mapy można wybrać, czy jedna czy dwie
    //TODO: wyniki do pliku
    //TODO: jak umrze i jest wybrany ma się wyświetlać dalej jako biały lisek
    //TODO: jak ma max energy to już nie je



}
