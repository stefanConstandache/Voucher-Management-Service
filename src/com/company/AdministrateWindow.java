package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class AdministrateWindow {
    JFrame administrationFrame = new JFrame();
    private JPanel AdministratePanel;
    private JButton administrateCampaignsButton;
    private JButton administrateVouchersButton;
    private JLabel userName;
    private JButton closeButton;

    public AdministrateWindow(User user, LocalDateTime applicationStartTime) {
        this.userName.setText(user.getName());
        administrationFrame.setContentPane(AdministratePanel);
        administrationFrame.pack();
        administrationFrame.setVisible(true);

        administrateCampaignsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdministrateCampaigns administrateCampaigns = new AdministrateCampaigns(user, applicationStartTime);
            }
        });
        administrateVouchersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdministrateVouchers administrateVouchers = new AdministrateVouchers(user, applicationStartTime);
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                administrationFrame.dispose();
            }
        });
    }
}
