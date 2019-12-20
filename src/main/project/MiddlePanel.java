package project;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class MiddlePanel extends JPanel{

    Data mapData;
    java.util.Map<String, JLabel> dataToDisplay;


    public MiddlePanel(int width, int height, Data mapData, List<String> dataFields, int dataRows){

        this.setSize(width, height);
        this.mapData = mapData;
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        this.dataToDisplay = new HashMap<>();
        for(String name: dataFields) {
            JLabel label = new JLabel(name + "  ");
            this.dataToDisplay.put(name, label);
            this.add(label);
        }
        this.add(new JLabel("\n"));
    }

    public void update() {
        for(java.util.Map.Entry<String, JLabel> entry: dataToDisplay.entrySet()) {
            entry.getValue().setText(entry.getKey() + "  "  + mapData.getData(entry.getKey()) + "\n");
        }
    }

}
