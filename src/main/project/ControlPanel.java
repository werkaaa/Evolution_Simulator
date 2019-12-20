package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ControlPanel extends MiddlePanel implements ActionListener {

    private final JButton pauseButton;


    public ControlPanel(int width, int height, Data mapData, ActionListener listener) {
        super(width, height, mapData, Arrays.asList("date"), 2);

        this.setLayout(new GridLayout(2, 0));
        this.pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        pauseButton.setPreferredSize(new Dimension(width, height));
        this.add(pauseButton);

        this.addActionListener(listener);
    }

    public void addActionListener(ActionListener listener) {
        pauseButton.addActionListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getActionCommand().equals("Pause")){
            this.pauseButton.setText("Continue");
        }
        else{
            this.pauseButton.setText("Pause");
        }

    }

}
