package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class AdministrateCampaigns {
    JFrame administrateCampaignsFrame = new JFrame();
    private JPanel AdministrateCampaignsPanel;
    private JButton displayCampaignsButton;
    private JButton addButton;
    private JButton editButton;
    private JButton closeCampaignButton;
    private JButton exitButton;
    private JTextField addCampaignField;
    private JTextField editCampaignField;
    private JTextField cancelCampaignField;
    private JLabel userName;
    private JTextField campaignIDTextField;
    private JButton campaignDetailsButton;

    public AdministrateCampaigns(User user, LocalDateTime applicationStartTime) {
        this.userName.setText(user.getName());
        administrateCampaignsFrame.setContentPane(AdministrateCampaignsPanel);
        administrateCampaignsFrame.pack();
        administrateCampaignsFrame.setVisible(true);

        displayCampaignsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisplayCampaigns displayCampaigns = new DisplayCampaigns();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                String st = addCampaignField.getText();
                String[] addCampaignValues = st.split(";");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime startCampaignDate = LocalDateTime.parse(addCampaignValues[3], formatter);
                LocalDateTime endCampaignDate = LocalDateTime.parse(addCampaignValues[4], formatter);
                Vector<Campaign> campaigns = vms.getCampaignCollection();
                int ok = 0;
                for(Campaign campaign: campaigns){
                    if(campaign.getCampaignID() == Integer.parseInt(addCampaignValues[0])){
                        JOptionPane.showMessageDialog(null,"Exista deja Campania cu acest ID");
                        ok = 1;
                    }
                }
                if(ok == 0) {
                    Campaign campaignForAdd = addCampaign(Integer.parseInt(addCampaignValues[0]), addCampaignValues[1], addCampaignValues[2], startCampaignDate, endCampaignDate, Integer.parseInt(addCampaignValues[5]), addCampaignValues[6], applicationStartTime);
                    vms.addCampaign(campaignForAdd);
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                String st = editCampaignField.getText();
                String[] editCampaignValues = st.split(";");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime startCampaignDate = LocalDateTime.parse(editCampaignValues[3], formatter);
                LocalDateTime endCampaignDate = LocalDateTime.parse(editCampaignValues[4], formatter);
                Campaign campaignForEdit = new Campaign(Integer.parseInt(editCampaignValues[0]), editCampaignValues[1], editCampaignValues[2], startCampaignDate, endCampaignDate, Integer.parseInt(editCampaignValues[5]), null, null);
                String updateStatus = vms.updateCampaign(Integer.parseInt(editCampaignValues[0]), campaignForEdit);
                System.out.println(updateStatus);
            }
        });
        closeCampaignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                String st = cancelCampaignField.getText();
                String[] cancelCampaignValues = st.split(";");

                String updateStatus = vms.cancelCampaign(Integer.parseInt(cancelCampaignValues[0]));
                System.out.println(updateStatus);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                administrateCampaignsFrame.dispose();
            }
        });
        campaignDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                String st = campaignIDTextField.getText();
                String showMessege = new String();
                Vector<Campaign> campaigns = vms.getCampaignCollection();
                for(Campaign campaign: campaigns){
                    if(campaign.getCampaignID() == Integer.parseInt(st)){
                        showMessege = Integer.toString(campaign.getCampaignID()) + ", " + campaign.getName() + ", " + campaign.getC_type() + ", " + campaign.getNr_vou_disp();
                        break;
                    }
                }
                JOptionPane.showMessageDialog(null,showMessege);

            }
        });
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
}
