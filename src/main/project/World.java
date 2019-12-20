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

    AtomicBoolean paused = new AtomicBoolean(false);

    private ControlPanel generalPanel;
    private final int mapBoardWidth = 500;
    private final int mapBoardHeight = 500;

    private List<Map> maps;
    private List<MapSegment> mapPanels;
    private int numberOfMaps;

    private int width, height, startEnergy, plantEnergy, moveEnergy, initialAnimalsNumber, initialPlantsNumber, maxEnergy;
    private double jungleRatio;

    private java.util.Map<String, Icon> pictures = new HashMap<>();

    private Thread threadObject;
    private Runnable runnable;

    private void runSimulation() throws InterruptedException {
        this.runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (paused.get()) {
                        synchronized (threadObject) {
                            try {
                                threadObject.wait();
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }

                    for (int i = 0; i < numberOfMaps; i++) {
                        maps.get(i).run();
                        //generateMap(maps.get(i), mapPanels.get(i).grids);
                        mapPanels.get(i).generateMap();
                    }
                    generalPanel.update();

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        threadObject = new Thread(runnable);
        threadObject.start();
    }

    public World(){
        this.readInput();
        this.getPictures();

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(0, this.numberOfMaps+1, 0, 0));
        frame.setSize((this.numberOfMaps+1)*this.mapBoardWidth, this.mapBoardHeight);

        JPanel panel = new JPanel(new GridLayout(this.numberOfMaps+1, 0));
        panel.setSize(this.mapBoardWidth, this.mapBoardHeight);


        this.maps = new ArrayList<>();
        this.mapPanels = new ArrayList<>();
        for(int i = 0; i<this.numberOfMaps; i++){
            this.maps.add(new Map(this.width, this.height, this.startEnergy, this.plantEnergy, this.moveEnergy, this.jungleRatio, this.initialAnimalsNumber, this.initialPlantsNumber, this.maxEnergy));
            mapPanels.add(new MapSegment(width, height, mapBoardWidth, mapBoardHeight, this.pictures, this.maxEnergy, this.maps.get(i)));
            this.mapPanels.get(i).setPanel(new MapPanel(this.mapBoardWidth, this.mapBoardHeight/(this.numberOfMaps+1), this.maps.get(i).getMapData()));
            panel.add(this.mapPanels.get(i).getPanel());
            frame.add(mapPanels.get(i));
           // this.generateMap(this.maps.get(i), mapPanels.get(i).grids);
            this.mapPanels.get(i).generateMap();
        }

        this.generalPanel = new ControlPanel(this.mapBoardWidth, this.mapBoardHeight/(this.numberOfMaps+1), this.maps.get(0).getMapData(), this);
        panel.add(this.generalPanel);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public void getPictures() {
        int pictureWidth = mapBoardWidth/ width;
        int pictureHeight = mapBoardHeight/height;

        List<String> things_to_show = new ArrayList<>(Arrays.asList("food", "dead_animal", "4_animal", "3_animal", "2_animal", "1_animal", "0_animal"));
        List<String> backgrounds = new ArrayList<>(Arrays.asList("savanna", "jungle"));

        for(String backgroundName : backgrounds) {
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


    public void readInput(){
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("resources/parameters.json"));

            JSONObject jsonObject = (JSONObject) obj;

            this.width = Integer.parseInt(jsonObject.get("width").toString());
            this.height = Integer.parseInt(jsonObject.get("height").toString());
            this.startEnergy = Integer.parseInt(jsonObject.get("startEnergy").toString());
            this.maxEnergy = Integer.parseInt(jsonObject.get("maxEnergy").toString());
            this.plantEnergy = Integer.parseInt(jsonObject.get("plantEnergy").toString());
            this.moveEnergy = Integer.parseInt(jsonObject.get("moveEnergy").toString());
            this.jungleRatio = Double.parseDouble(jsonObject.get("jungleRatio").toString());
            this.initialAnimalsNumber = Integer.parseInt(jsonObject.get("initialAnimalsNumber").toString());
            this.initialPlantsNumber = Integer.parseInt(jsonObject.get("initialPlantsNumber").toString());
            System.out.println(jsonObject.get("secondMap").toString());
            this.numberOfMaps = jsonObject.get("secondMap").toString().equals("True") ? 2:1;
            //this.numberOfMaps = 2;

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
        String command = actionEvent.getActionCommand();
        if(command.equals("Pause")) {
            this.paused.set(true);
        } else {
            this.paused.set(false);
            synchronized(threadObject) {
                threadObject.notify();
            }
        }

    }

    // TODO: testy
    // TODO: czasami znikają zwierzęta, które się urodziły (sprawdzić to)
    // TODO: zrobić bardziej observerowo updateowanie energii w strukturze (jak?) lub zostawić sortowanie
    // TODO: dodać obserwer przy pojawieniu się wolnej pozycji
    // TODO: dwa obserwery do komórki jeden energy will change a drugi energy changed, jeden usuwa z drzewa drugi dodaje do drzewa
    // TODO: dodać wyjątek dla zbyt wielu zwierząt
    // TODO: posprzątać kod
    // TODO: wyjątek przy małęj jungli jak tam się wstawiają zwierzaki

    //funkcjonalności:

    //TODO: interface do statystyk
    //TODO: epoka w której zmarło
    //TODO: wyniki do pliku





}
