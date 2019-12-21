package visualization;

import project.Data;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {

    private Data mapData;

    public MapPanel(int width, int height, Data mapData){

        this.setSize(width, height);
        this.mapData = mapData;
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        this.setLayout(new GridLayout(2, 0));

        //add the table to the frame
        this.add(new JScrollPane((Component) this.mapData.getGeneralTable()));
        this.add(new JScrollPane((Component) this.mapData.getChosenTable()));
    }

}
