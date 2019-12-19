package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ControlPanel extends MiddlePanel implements ActionListener {

    private final JButton continueButton;
    private final JButton pauseButton;


    public ControlPanel(int width, int height, Data mapData) {
        super(width, height, mapData, Arrays.asList("date"));

        this.pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        this.add(pauseButton);

        this.continueButton = new JButton("Continue");
        continueButton.addActionListener(this);
        this.add(continueButton);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

}
