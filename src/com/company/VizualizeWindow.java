package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class VizualizeWindow {
    JFrame vizualizationFrame = new JFrame();
    private JPanel VizualizePanel;
    private JButton vizualizeCampaignsButton;
    private JButton vizualizeMyVouchersButton;
    private JButton cancelButton;
    private JLabel userName;

    public VizualizeWindow(User userParsed) {
        this.userName.setText(userParsed.getName());
        vizualizationFrame.setContentPane(VizualizePanel);
        vizualizationFrame.pack();
        vizualizationFrame.setVisible(true);

        vizualizeCampaignsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                String showMessege = new String();
                Vector<User> users = vms.getUsersCollection();
                for(User user: users){
                    if(user.getEmail().equals(userParsed.getEmail())){
                        UserVoucherMap userVoucherMap = user.getUserVoucherMap();
                        if(userVoucherMap != null) {
                            Vector<Campaign> campaigns = vms.getCampaignCollection();
                            for (Campaign campaign : campaigns) {
                                if (userVoucherMap.containsKey(campaign.getCampaignID())) {
                                    showMessege = showMessege + " " + campaign.getName() + "\n";
                                    }
                                }
                        }
                    }
                }
                JOptionPane.showMessageDialog(null,showMessege);
            }
        });
        vizualizeMyVouchersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMS vms = VMS.getInstance();
                String showMessege = new String();
                Vector<User> users = vms.getUsersCollection();
                for(User user: users){
                    if(user.getEmail().equals(userParsed.getEmail())) {
                        UserVoucherMap userVoucherMap = user.getUserVoucherMap();
                        if (userVoucherMap != null) {
                            Vector<Campaign> campaigns = vms.getCampaignCollection();
                            for(Campaign campaign: campaigns) {
                                    if (userVoucherMap.containsKey(campaign.getCampaignID())) {
                                        Vector<Voucher> vouchers = userVoucherMap.get(campaign.getCampaignID());
                                        showMessege = showMessege + "Campaign ID " + campaign.getCampaignID() + vouchers.toString() + "\n";
                                    }
                            }
                        }
                    }
                    }

                JOptionPane.showMessageDialog(null,showMessege);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vizualizationFrame.dispose();
            }
        });
    }
}
