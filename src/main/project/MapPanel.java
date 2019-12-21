package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MapPanel extends JPanel {

    private Data mapData;

    public MapPanel(int width, int height, Data mapData){

        this.setSize(width, height);
        this.mapData = mapData;
        this.setBorder(BorderFactory.createLineBorder(Color.black));


        this.setLayout(new GridLayout(2, 0));

        //add the table to the frame
        this.add(new JScrollPane(this.mapData.generalTable));
        this.add(new JScrollPane(this.mapData.chosenTable));
    }

}
