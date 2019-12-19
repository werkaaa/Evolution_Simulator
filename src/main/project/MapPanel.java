package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapPanel extends MiddlePanel implements ActionListener {

    public MapPanel(int width, int height, Data mapData){
        super(width, height, mapData, Arrays.asList("animalsNumber", "plantsNumber", "averageEnergy", "averageChildrenNumber", "deadAnimalsAverageAge"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
