package com.company;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class GUI {
    JFrame fileChooserFrame = new JFrame();
    private JPanel TextFilesWindow;
    private JButton submitButton;
    private JButton browseButton1;
    private JButton browseButton2;
    private JTextField fileChosenTextField1;
    private JTextField fileChosenTextField2;
    private JButton cancelButton;

    public GUI() {
        browseButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                if(e.getSource() == browseButton1){
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        String pathToUsers = file.getAbsolutePath();
                        fileChosenTextField1.setText(pathToUsers);
                    }
                }
            }
        });
        browseButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                if(e.getSource() == browseButton2){
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        String pathToCampaigns = file.getAbsolutePath();
                        fileChosenTextField2.setText(pathToCampaigns);
                    }
                }
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File userFile = new File(fileChosenTextField1.getText());
                File campaignFile = new File(fileChosenTextField2.getText());
                try {
                    addUsers(userFile);
                    addCampaigns(campaignFile);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                LocalDateTime applicationStartTime = LocalDateTime.now();
                VMS vms = VMS.getInstance();
                Vector<User> users =vms.getUsersCollection();
                fileChooserFrame.dispose();
                LogIn logIn = new LogIn(applicationStartTime);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooserFrame.dispose();
                System.exit(0);
            }
        });

        fileChooserFrame.setContentPane(TextFilesWindow);
        fileChooserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fileChooserFrame.pack();
        fileChooserFrame.setVisible(true);
    }

    public static void addUsers(File file) throws  Exception{
        BufferedReader br = new BufferedReader(new FileReader(file));
        Integer nrUsers  = Integer.parseInt(br.readLine());
        String st;
        while ((st = br.readLine()) != null) {
            String[] usersValues = st.split(";");
            VMS vms = VMS.getInstance();
            User user = null;
            switch (usersValues[4]) {
                case "GUEST":
                    user = new User(Integer.parseInt(usersValues[0]), usersValues[1], usersValues[2], usersValues[3], User.UserType.GUEST);
                    break;
                case "ADMIN":
                    user = new User(Integer.parseInt(usersValues[0]), usersValues[1], usersValues[2], usersValues[3], User.UserType.ADMIN);
                    break;
            }
            vms.addUser(user);
        }
        br.close();
    }

    public static void addCampaigns(File file) throws  Exception{
        BufferedReader br = new BufferedReader(new FileReader(file));
        Integer nrCampanii = Integer.parseInt(br.readLine());
        String campaignDateFromFile = br.readLine();
        String st;
        while ((st = br.readLine()) != null) {
            String[] campaignValues = st.split(";");
            VMS vms = VMS.getInstance();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startCampaignDate = LocalDateTime.parse(campaignValues[3], formatter);
            LocalDateTime endCampaignDate = LocalDateTime.parse(campaignValues[4], formatter);
            LocalDateTime applicationStartTime = LocalDateTime.now();
            Campaign campaign = addCampaign(Integer.parseInt(campaignValues[0]), campaignValues[1], campaignValues[2], startCampaignDate, endCampaignDate, Integer.parseInt(campaignValues[5]), campaignValues[6],applicationStartTime);
            vms.addCampaign(campaign);
        }
        br.close();
    }

    private static Campaign addCampaign(Integer id, String nume, String des, LocalDateTime sd, LocalDateTime ed, int nr_v, String strategyType, LocalDateTime applicationDate){
        Campaign campaign = null;
        switch (strategyType) {
            case "A":
                if(applicationDate.isBefore(sd)) {
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.NEW, Campaign.StrategyType.A);
                }
                if((applicationDate.isEqual(sd) || applicationDate.isAfter(sd)) && applicationDate.isBefore(ed)){
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.STARTED, Campaign.StrategyType.A);
                }
                if(applicationDate.isAfter(ed) || applicationDate.isEqual(ed)){
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.EXPIRED, Campaign.StrategyType.A);
                }
                break;
            case "B":
                if(applicationDate.isBefore(sd)) {
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.NEW, Campaign.StrategyType.B);
                }
                if((applicationDate.isEqual(sd) || applicationDate.isAfter(sd)) && applicationDate.isBefore(ed)){
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.STARTED, Campaign.StrategyType.B);
                }
                if(applicationDate.isAfter(ed) || applicationDate.isEqual(ed)){
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.EXPIRED, Campaign.StrategyType.B);
                }
                break;
            case "C":
                if(applicationDate.isBefore(sd)) {
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.NEW, Campaign.StrategyType.C);
                }
                if((applicationDate.isEqual(sd) || applicationDate.isAfter(sd)) && applicationDate.isBefore(ed)){
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.STARTED, Campaign.StrategyType.C);
                }
                if(applicationDate.isAfter(ed) || applicationDate.isEqual(ed)){
                    campaign = new Campaign(id, nume, des, sd, ed, nr_v, Campaign.CampaignStatusType.EXPIRED, Campaign.StrategyType.C);
                }
                break;
        }
        return campaign;
    }

    public static void main(String args[]) {
        GUI gui = new GUI();
    }

}
