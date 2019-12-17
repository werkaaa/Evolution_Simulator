package project;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class World {

    private Map map1;
    private Map map2;
    private final int mapBoardWidth = 800;
    private final int mapBoardHeight = 800;

    private final int controlPanelWidth = 300;

    private int width, height;

    private java.util.Map<Vector2D, JLabel> grids = new HashMap<>();
    private java.util.Map<String, ImageIcon> pictures = new HashMap<>();

    private void runSimulation(int epochNumber) throws InterruptedException {
        for(int i = 0; i<epochNumber; i++){
            this.map1.run();
            this.generateMap(this.map1);
            Thread.sleep(200);
        }
    }

    public World(int width, int height, int startEnergy, int plantEnergy, int moveEnergy, double jungleRatio){
        this.width = width;
        this.height = height;
        this.map1 = new Map(width, height, startEnergy, plantEnergy, moveEnergy, jungleRatio);

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JPanel mapBoard = new JPanel();
        mapBoard.setLayout(new GridLayout(height, width, 0, 0));
        mapBoard.setSize(mapBoardWidth, mapBoardHeight);
        for(int y = height - 1; y >= 0; y--) {
            for (int x = 0; x <= width - 1; x++) {
                JLabel label = new JLabel("", JLabel.CENTER);
                label.setForeground(Color.BLACK);
                label.setBackground(Color.DARK_GRAY);

                mapBoard.add(label);
                grids.put(new Vector2D(x, y), label);
            }
        }

        frame.setSize(this.mapBoardWidth+this.controlPanelWidth, this.mapBoardHeight+50);
        frame.add(mapBoard);
        this.getPictures();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void getPictures() {
        int pictureWidth = mapBoardWidth/ width;
        int pictureHeight = mapBoardHeight/height;

        List<String> things_to_show = new ArrayList<>(Arrays.asList("food_savanna", "animal_savanna", "food_jungle", "animal_jungle", "savanna_background", "jungle_background", "dead_animal_savanna", "dead_animal_jungle"));

        for(String elementName : things_to_show) {
            System.out.println(elementName+".png");
            //String name = elementName+".png";
            ImageIcon element = new ImageIcon(getClass().getResource(elementName+".png"));
            Image picture = element.getImage().getScaledInstance(pictureWidth, pictureHeight, java.awt.Image.SCALE_SMOOTH);
            this.pictures.put(elementName, new ImageIcon(picture));
        }
    }

    public void generateMap(Map map) {
        for(int y = this.height - 1; y >= 0; y--) {
            for(int x = 0; x <= width - 1; x++) {
                Vector2D position = new Vector2D(x, y);
                Object mapElement = map.objectAt(position);
                JLabel label = grids.get(position);
                ImageIcon element;
                if(mapElement==null) {
                    if(map.inJungle(position)) {
                        element = pictures.get("jungle_background");
                    }
                    else{
                        element = pictures.get("savanna_background");
                    }
                }
                else if(mapElement instanceof Animal){

                    if(map.inJungle(position)) {
                        if(!((Animal) mapElement).alive){
                            element = pictures.get("dead_animal_jungle");
                        }
                        else {
                            element = pictures.get("animal_jungle");
                        }
                    }
                    else{
                        if(!((Animal) mapElement).alive){
                            element = pictures.get("dead_animal_savanna");
                        }
                        else {
                            element = pictures.get("animal_savanna");
                        }
                    }
                }
                else{
                    if(map.inJungle(position)) {
                        element = pictures.get("food_jungle");
                    }
                    else{
                        element = pictures.get("food_savanna");
                    }
                }
                label.setIcon(element);
            }
        }
    }



    public static void main(String[] args){

        World simulation = new World(20, 20, 100, 20, 2, 0.36);
        for(int i = 0; i<40; i++) {
            simulation.map1.place(new Animal(simulation.map1));
            simulation.map1.placePlants();
        }

        try {
            simulation.runSimulation(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    //pytania
    // TODO: CZy można aktualnizować informację o liczbie potomków na bieżąco zamiast wyświetlać ją po n epokach?


    //funkcjonalności:
    //TODO: dane z pliku
    //TODO: jedzenie na pół
    //TODO: wyświetlanie energii(forma)
    //TODO: zatrzymywanie i wznawianie symulacji
    //TODO: interface do statystyk
    //TODO: liczba wszystkich zwierząt
    //TODO: liczba wszystkich roślin
    //TODO: dominujące genotypy
    //TODO: średni poziom energii
    //TODO: średnia długość życia dla martwych zwierząt
    //TODO: średnia liczba zwierząt dal żyjących zwierząt
    //TODO: wskazać zwierzę i wyświetli się jego genom
    //TODO: dzieci po n-epokach
    //TODO: potomków po n-epokach
    //TODO: epoka w której zmarło
    //TODO: dwie mapy
    //TODO: wyniki do pliku
    //



}
