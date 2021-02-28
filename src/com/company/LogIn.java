package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Vector;

public class LogIn {
    JFrame logInFrame = new JFrame();
    private JPasswordField passwordField;
    private JPanel LogInPanel;
    private JButton logInButton;
    private JTextField textField;
    private JButton closeButton;
    private String str;
    public LogIn(LocalDateTime applicationStartTime) {

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                Vector<User> users = vms.getUsersCollection();
                for(User user: users){
                    if(user.getName().equals(textField.getText()) && user.getPassword().equals(passwordField.getText())){
                        if(user.getuType().equals(User.UserType.ADMIN)){
                            AdministrateWindow administrateWindow = new AdministrateWindow(user, applicationStartTime);
                        }
                        else if(user.getuType().equals(User.UserType.GUEST)){
                            VizualizeWindow vizualizeWindow = new VizualizeWindow(user);
                        }
                    }
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInFrame.dispose();
                System.exit(0);
            }
        });

        logInFrame.setContentPane(LogInPanel);
        logInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logInFrame.pack();
        logInFrame.setVisible(true);

    }
}
