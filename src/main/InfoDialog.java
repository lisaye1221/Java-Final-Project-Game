package main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoDialog extends JDialog {

    private GamePanel gp;
    private JTextField usernameField;
    private JPasswordField PINField;
    private JLabel usernameLabel;
    private JLabel PINLabel;
    private JButton saveBtn;
    private JButton cancelBtn;

    public InfoDialog(Frame parent, GamePanel gp){
        super(parent, "Enter info for you save file", true);

        this.gp = gp;
        JPanel jp = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(25);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        jp.add(usernameField, cs);

        PINField = new JPasswordField(4);
        PINField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                saveBtn.setEnabled(getPIN().length() == 4);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                saveBtn.setEnabled(getPIN().length() == 4);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                saveBtn.setEnabled(getPIN().length() == 4);
            }
        });
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        jp.add(PINField, cs);

        usernameLabel = new JLabel("Name: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        jp.add(usernameLabel, cs);

        PINLabel = new JLabel("PIN(4 characters): ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        jp.add(PINLabel, cs);

        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton("Save");
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.setUsername(getUsername());
                gp.setPIN(getPIN());
                dispose();
            }
        });
        buttonPanel.add(saveBtn);
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelBtn);

        getContentPane().add(jp, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);


        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String getUsername(){
        return usernameField.getText().trim();
    }

    public String getPIN(){
        return new String(PINField.getPassword());
    }
}
