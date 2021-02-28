package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class DisplayCampaigns {
    JFrame displayCampaignsFrame = new JFrame();
    private JTable tableCampaigns;
    private JButton closeButton;
    private JPanel DisplayCampaignsPanel;
    private JButton sortAfterStartDateButton;
    private JButton sortAfterNameButton;

    public DisplayCampaigns() {
        VMS vms = VMS.getInstance();
        Vector<Campaign> campaigns = vms.getCampaignCollection();
        String[] columnNames = {"Campaign ID", "Campaign Name", "Campaign Start Date", "Campaign End Date", "Campaign Number Of Vouchers"};
        String[][] columnData = new String[campaigns.size()][columnNames.length];

        int i = 0;
        for(Campaign campaign: campaigns){
            columnData[i][0] = Integer.toString(campaign.getCampaignID());
            columnData[i][1] = campaign.getName();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startDate = campaign.getStart_date().format(formatter);
            columnData[i][2] = startDate;
            String endDate = campaign.getEnd_date().format(formatter);
            columnData[i][3] = endDate;
            columnData[i][4] = Integer.toString(campaign.getNr_vou());
            i++;
        }

        String[][] data = columnData;
        DefaultTableModel tableModel = (DefaultTableModel) tableCampaigns.getModel();
        tableModel.setRowCount(0);
        for(String column: columnNames){
            tableModel.addColumn(column);
        }
        for(String[] row: data){
            tableModel.addRow(row);
        }
        tableCampaigns.setModel(tableModel);
        tableCampaigns.setPreferredScrollableViewportSize(new Dimension(1000, 300));

        displayCampaignsFrame.setContentPane(DisplayCampaignsPanel);
        displayCampaignsFrame.pack();
        displayCampaignsFrame.setVisible(true);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCampaignsFrame.dispose();
            }
        });
        sortAfterStartDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < campaigns.size() - 1; i++) {
                    for (int j = 0; j < campaigns.size() - i - 1; j++) {
                        if(columnData[j][2].compareTo(columnData[j+1][2]) > 0){
                            String[] toSwap = columnData[j];
                            columnData[j] = columnData[j+1];
                            columnData[j+1] = toSwap;
                        }
                    }
                }

                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.setRowCount(0);
                for(String column: columnNames){
                    tableModel.addColumn(column);
                }
                for(String[] row: data){
                    tableModel.addRow(row);
                }
                tableCampaigns.setModel(tableModel);
            }
        });
        sortAfterNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < campaigns.size() - 1; i++) {
                    for (int j = 0; j < campaigns.size() - i - 1; j++) {
                        if(columnData[j][1].compareTo(columnData[j+1][1]) > 0){
                            String[] toSwap = columnData[j];
                            columnData[j] = columnData[j+1];
                            columnData[j+1] = toSwap;
                        }
                    }
                }

                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.setRowCount(0);
                for(String column: columnNames){
                    tableModel.addColumn(column);
                }
                for(String[] row: data){
                    tableModel.addRow(row);
                }
                tableCampaigns.setModel(tableModel);
            }
        });
    }
}
