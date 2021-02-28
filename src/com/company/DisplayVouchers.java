package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class DisplayVouchers {
    JFrame displayVouchersFrame = new JFrame();
    private JPanel DisplayVouchersPanel;
    private JTable tableVouchers;
    private JButton closeButton;

    public DisplayVouchers(Integer campaignId) {
        displayVouchersFrame.setContentPane(DisplayVouchersPanel);
        displayVouchersFrame.pack();
        displayVouchersFrame.setVisible(true);

        VMS vms = VMS.getInstance();
        Vector<Campaign> campaigns = vms.getCampaignCollection();

        CampaignVoucherMap campaignVoucherMap = null;
        for(Campaign campaign: campaigns){
            if(campaign.getCampaignID() == campaignId){
                campaignVoucherMap = campaign.getCampaignVoucherMap();
            }
        }

        if(campaignVoucherMap != null) {

            Vector<User> users = vms.getUsersCollection();
            int numberOfRows = 0;
            for (User user : users) {
                if (campaignVoucherMap.containsKey(user.getEmail())) {
                    Vector<Voucher> vouchers = campaignVoucherMap.get(user.getEmail());
                    numberOfRows = numberOfRows + vouchers.size();
                }
            }

            String[] columnNames = {"USER ID", "Voucher ID", "Voucher Type", "Gift sau Loyalty", "Voucher Value"};
            String[][] columnData = new String[numberOfRows][columnNames.length];

            int i = 0;
            for (User user : users) {
                if (campaignVoucherMap.containsKey(user.getEmail())) {
                    Vector<Voucher> vouchers = campaignVoucherMap.get(user.getEmail());
                    int nr = 0;
                    for (Voucher voucher : vouchers) {
                        columnData[i + nr][0] = user.getName() + " " + user.getUser_ID();
                        columnData[i + nr][1] = "Voucher " + voucher.getId();
                        columnData[i + nr][2] = "Type " + voucher.getvType();
                        if(voucher.getReducere() != 0.0f){
                            columnData[i + nr][3] = "Loyalty";
                            columnData[i + nr][4] = "Value " + voucher.getReducere();
                        }
                        else if(voucher.getSuma() != 0.0f){
                            columnData[i + nr][3] = "Gift";
                            columnData[i + nr][4] = "Value " + voucher.getSuma();
                        }
                        nr++;
                    }
                    i = i + nr;
                }
            }

            String[][] data = columnData;
            DefaultTableModel tableModel = (DefaultTableModel) tableVouchers.getModel();
            tableModel.setRowCount(0);
            for (String column : columnNames) {
                tableModel.addColumn(column);
            }
            for (String[] row : data) {
                tableModel.addRow(row);
            }
            tableVouchers.setModel(tableModel);
            tableVouchers.setPreferredScrollableViewportSize(new Dimension(500, 100));
        }

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayVouchersFrame.dispose();
            }
        });
    }
}
