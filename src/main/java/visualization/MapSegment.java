package visualization;

import project.Animal;
import project.Map;
import project.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import static java.lang.Integer.min;

public class MapSegment extends JPanel {
    public java.util.Map<Vector2D, JLabel> grids = new HashMap<>();
    public MapPanel panel;
    private int width;
    private int height;
    private Map map;
    private java.util.Map<String, Icon> pictures;
    private int maxEnergy;
    private boolean selectionMade = false;

    public MapSegment(int width, int height, int mapBoardWidth, int mapBoardHeight, java.util.Map<String, Icon> pictures, int maxEnergy, Map map) {
        this.pictures = pictures;
        this.maxEnergy = maxEnergy;
        this.width = width;
        this.height = height;
        this.setLayout(new GridLayout(height, width, 0, 0));
        this.map = map;


        this.setSize(mapBoardWidth, mapBoardHeight);
        this.setBorder(BorderFactory.createLineBorder(Color.black));


        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                JLabel label = new JLabel("", JLabel.CENTER);
                this.add(label);
                this.grids.put(new Vector2D(x, y), label);

                int get_x = x, get_y = y;

                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        super.mouseClicked(mouseEvent);

                        map.selectAnimal(get_x, get_y);
                        generateMap();
                        selectionMade = true;

                    }
                });
            }
        }

    }


    public MapPanel getPanel() {
        return this.panel;
    }

    public void setPanel(MapPanel panel) {
        this.panel = panel;
    }


    public void generateMap() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < width; x++) {
                Vector2D position = new Vector2D(x, y);
                Object mapElement;
                Object selectedOne = this.map.getMapData().getChosenAnimal();
                JLabel label = grids.get(position);
                if (selectedOne != null && ((Animal) selectedOne).getPosition().equals(position)) {
                    mapElement = this.map.getMapData().getChosenAnimal();
                    label.setBorder(BorderFactory.createLineBorder(Color.red));
                } else {
                    mapElement = map.objectAt(position);
                    label.setBorder(BorderFactory.createEmptyBorder());
                }

                String background = map.inJungle(position) ? "jungle" : "savanna";
                String objectType = mapElement == null ? "" : (mapElement instanceof Animal ? "animal_" : "food_");
                String energyLevel = mapElement instanceof Animal ? (((Animal) mapElement).isAlive() ? String.valueOf(min(4, ((Animal) mapElement).getEnergy() / (maxEnergy / 5))) + "_" : "dead_") : "";
                String cellName = energyLevel + objectType + background;
                Icon element = pictures.get(cellName);
                label.setIcon(element);
            }
        }

        if (!this.map.getMapData().getDominantGenome().isEmpty()) {
            for (Animal animal : this.map.getMapData().getGenomes().get(this.map.getMapData().getDominantGenome())) {
                if (!animal.isChosen()) {
                    grids.get(animal.getPosition()).setBorder(BorderFactory.createLineBorder(Color.blue));
                }
            }
        }
    }

}
