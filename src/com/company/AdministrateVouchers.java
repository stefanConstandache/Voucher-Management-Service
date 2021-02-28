package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class AdministrateVouchers {
    JFrame administrateVouchersFrame = new JFrame();
    private JPanel AdministrateVouchersPanel;
    private JTextField campaignIDTextField;
    private JTextField generateVoucherFormatCampaignIdTextField;
    private JTextField redeemVoucherFormatCampaignIdTextField;
    private JButton displayVouchersButton;
    private JButton generateButton;
    private JButton redeemButton;
    private JButton closeButton;
    private JLabel userName;
    private JLabel textDisplayed;

    public AdministrateVouchers(User user, LocalDateTime applicationStartTime) {
        this.userName.setText(user.getName());
        administrateVouchersFrame.setContentPane(AdministrateVouchersPanel);
        administrateVouchersFrame.pack();
        administrateVouchersFrame.setVisible(true);
        displayVouchersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisplayVouchers displayVouchers = new DisplayVouchers(Integer.parseInt(campaignIDTextField.getText()));
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                String st = generateVoucherFormatCampaignIdTextField.getText();
                String[] generateVoucherValues = st.split(";");

                Vector<Campaign> campaignCollection = vms.getCampaignCollection();
                for(Campaign campaign:campaignCollection){
                    if(campaign.getCampaignID() == Integer.parseInt(generateVoucherValues[0])){
                        System.out.println("campaignId: " + campaign.getCampaignID());
                        campaign.generateVoucher(generateVoucherValues[1], generateVoucherValues[2], Float.parseFloat(generateVoucherValues[3]));
                    }
                }
            }
        });
        redeemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                String st = redeemVoucherFormatCampaignIdTextField.getText();
                String[] redeemVoucherValues = st.split(";");

                Vector<Campaign> campaignCollection = vms.getCampaignCollection();
                for(Campaign campaign:campaignCollection) {
                    if (campaign.getCampaignID() == Integer.parseInt(redeemVoucherValues[0])) {
                        System.out.println("campaignId: " + campaign.getCampaignID());
                        if (Campaign.CampaignStatusType.NEW.equals(campaign.getC_type()) || Campaign.CampaignStatusType.STARTED.equals(campaign.getC_type())) {
                            if (campaign.getStart_date().isBefore(applicationStartTime) && campaign.getEnd_date().isAfter(applicationStartTime)) {
                                campaign.redeemVoucher(Integer.parseInt(redeemVoucherValues[1]), applicationStartTime);
                            }
                        }
                    }
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                administrateVouchersFrame.dispose();
            }
        });
    }
}
