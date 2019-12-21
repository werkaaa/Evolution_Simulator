package visualization;

import project.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel implements ActionListener {

    private final JButton pauseButton;
    private final JButton saveButton;
    private Data mapData;


    public ControlPanel(int width, int height, Data mapData, ActionListener listener) {
        this.setSize(width, height);
        this.mapData = mapData;
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        this.setLayout(new GridLayout(3, 0));

        this.add(new JScrollPane((Component) this.mapData.getDate()));

        this.pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        pauseButton.setPreferredSize(new Dimension(width, height));
        this.add(pauseButton);

        this.saveButton = new JButton("Save");
        this.add(saveButton);

        this.addActionListener(listener);
    }

    public void addActionListener(ActionListener listener) {
        pauseButton.addActionListener(listener);
        saveButton.addActionListener(listener);
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
