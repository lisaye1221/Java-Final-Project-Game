package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {
    JFrame jf;
    public StartPanel(JFrame jf) {
        this.jf = jf;
        setPreferredSize(new Dimension(400, 400));
        setBackground(new Color(145,240,255));
        this.setLayout(null);

        JButton newButton = new JButton("New Journey");
        newButton.setBounds(100, 200, 200, 50);
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        JButton loadButton = new JButton("Continue Journey");
        loadButton.setBounds(100, 260, 200, 50);


        // add button
        this.add(newButton);
        this.add(loadButton);

    }
}
