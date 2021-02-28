package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class Test {

    public static void main(String[] args) throws Exception{
        try{
            readCampaign();
            readUsers();
            readEvents();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void readCampaign() throws Exception {
            File file = new File("campaigns.txt");
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
                LocalDateTime applicationDate = LocalDateTime.parse(campaignDateFromFile, formatter);
                Campaign campaign = addCampaign(Integer.parseInt(campaignValues[0]), campaignValues[1], campaignValues[2], startCampaignDate, endCampaignDate, Integer.parseInt(campaignValues[5]), campaignValues[6],applicationDate);
                vms.addCampaign(campaign);
            }
        br.close();
    }

    private static void readUsers() throws Exception{
        File file = new File("users.txt");
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
    private static void readEvents() throws Exception {
        File file = new File("events.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        File outputFile = new File("output.txt");
        if (outputFile.createNewFile())
        {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
            outputFile.delete();
            outputFile.createNewFile();
        }
        FileWriter outputWriter = new FileWriter(outputFile);
        String eventDateFromFile = br.readLine();
        Integer nrOfEvents = Integer.parseInt(br.readLine());
        String st;
        while ((st = br.readLine()) != null) {
            String[] eventsValues = st.split(";");
            VMS vms = VMS.getInstance();
            switch (eventsValues[1].trim()) {
                case "addCampaign" :
                    Integer userId = Integer.parseInt(eventsValues[0]);
                    Vector<User> usersCollection = vms.getUsersCollection();
                    for(User user:usersCollection){
                        if(userId == user.getUser_ID()){
                            if(User.UserType.ADMIN.equals(user.getuType())) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime startCampaignDate = LocalDateTime.parse(eventsValues[5], formatter);
                                LocalDateTime applicationDate = LocalDateTime.parse(eventDateFromFile, formatter);
                                LocalDateTime endCampaignDate = LocalDateTime.parse(eventsValues[6], formatter);
                                Campaign campaignForAdd = addCampaign(Integer.parseInt(eventsValues[2]), eventsValues[3], eventsValues[4], startCampaignDate, endCampaignDate, Integer.parseInt(eventsValues[7]), eventsValues[8], applicationDate);
                                vms.addCampaign(campaignForAdd);
                                System.out.println("Campania a fost adaugat cu succes.");
                            }else {
                                System.out.println("Utilizatorul nu are dreptul de a adauga o campanie");
                            }
                        }
                    }
                    break;
                case "editCampaign" :
                    Integer userIdForEditCampaign = Integer.parseInt(eventsValues[0]);
                    Vector<User> usersCollectionForEditCampaign  = vms.getUsersCollection();
                    for(User user:usersCollectionForEditCampaign){
                        if(userIdForEditCampaign == user.getUser_ID()){
                            if( User.UserType.ADMIN.equals(user.getuType())) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime startCampaignDate = LocalDateTime.parse(eventsValues[5], formatter);
                                LocalDateTime endCampaignDate = LocalDateTime.parse(eventsValues[6], formatter);
                                Campaign campaignForEdit = new Campaign(Integer.parseInt(eventsValues[2]), eventsValues[3], eventsValues[4], startCampaignDate, endCampaignDate, Integer.parseInt(eventsValues[7]), null, null);
                                String updateStatus = vms.updateCampaign(Integer.parseInt(eventsValues[2]), campaignForEdit);
                                System.out.println(updateStatus);
                                Vector<Campaign> CampaignCollection = vms.getCampaignCollection();
                                for(Campaign campaign: CampaignCollection){
                                    if(campaign.getCampaignID() == Integer.parseInt(eventsValues[2])){
                                        LocalDateTime notificationSendDate = LocalDateTime.parse(eventDateFromFile, formatter);
                                        Notification notification = new Notification(notificationSendDate, campaign.getCampaignID(), null, Notification.NotificationType.EDIT);
                                        campaign.notifyAllObservers(notification);
                                    }
                                }
                            }else {
                                System.out.println("Utilizatorul nu are dreptul de a edita o campanie");
                            }
                        }
                    }
                    break;
                case "cancelCampaign" :
                    Integer userIdForCancelCampaign = Integer.parseInt(eventsValues[0]);
                    Vector<User> usersCollectionForCancelCampaign  = vms.getUsersCollection();
                    for(User user:usersCollectionForCancelCampaign){
                        if(userIdForCancelCampaign == user.getUser_ID()){
                            if( User.UserType.ADMIN.equals(user.getuType())) {
                                String updateStatus = vms.cancelCampaign(Integer.parseInt(eventsValues[2]));
                                System.out.println(updateStatus);
                                Vector<Campaign> CampaignCollection = vms.getCampaignCollection();
                                for(Campaign campaign: CampaignCollection){
                                    if(campaign.getCampaignID() == Integer.parseInt(eventsValues[2])){
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                        LocalDateTime notificationSendDate = LocalDateTime.parse(eventDateFromFile, formatter);
                                        Notification notification = new Notification(notificationSendDate, campaign.getCampaignID(), null, Notification.NotificationType.CANCEL);
                                        campaign.notifyAllObservers(notification);
                                    }
                                }
                            }else {
                                System.out.println("Utilizatorul nu are dreptul de a inchide o campanie");
                            }
                        }
                    }
                    break;
                case "generateVoucher" :
                    Integer userIdForGenerateVoucher = Integer.parseInt(eventsValues[0]);
                    Vector<User> usersCollectionForGenerateVoucher  = vms.getUsersCollection();
                    for(User user: usersCollectionForGenerateVoucher){
                        if(userIdForGenerateVoucher == user.getUser_ID()){
                            if( User.UserType.ADMIN.equals(user.getuType())) {
                                Vector<Campaign> campaignCollection = vms.getCampaignCollection();
                                for(Campaign campaign:campaignCollection){
                                    if(campaign.getCampaignID() == Integer.parseInt(eventsValues[2])){
                                        System.out.println("campaignId: " + campaign.getCampaignID());
                                        campaign.generateVoucher(eventsValues[3], eventsValues[4], Float.parseFloat(eventsValues[5]));
                                    }
                                }
                            }else {
                                System.out.println("Utilizatorul nu are dreptul de a adauga un voucher");
                            }
                        }
                    }
                    break;
                case "redeemVoucher" :
                    Integer userIdForRedeemVoucher = Integer.parseInt(eventsValues[0]);
                    Vector<User> usersCollectionForRedeemVoucher  = vms.getUsersCollection();
                    for(User user:usersCollectionForRedeemVoucher){
                        if(userIdForRedeemVoucher == user.getUser_ID()){
                            if( User.UserType.ADMIN.equals(user.getuType())) {
                                Vector<Campaign> campaignCollection = vms.getCampaignCollection();
                                for(Campaign campaign:campaignCollection){
                                    if(campaign.getCampaignID() == Integer.parseInt(eventsValues[2])){
                                        System.out.println("campaignId: " + campaign.getCampaignID());
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                        LocalDateTime localDate = LocalDateTime.parse(eventsValues[4], formatter);
                                        if(Campaign.CampaignStatusType.NEW.equals(campaign.getC_type()) || Campaign.CampaignStatusType.STARTED.equals(campaign.getC_type())) {
                                            if(campaign.getStart_date().isBefore(localDate) && campaign.getEnd_date().isAfter(localDate)) {
                                                campaign.redeemVoucher(Integer.parseInt(eventsValues[3]), localDate);
                                            }
                                            else {
                                                System.out.println("Voucherul nu este valabil");
                                            }
                                        }
                                        else {
                                            System.out.println("Campania s-a incheiat sau a fost anulata");
                                        }
                                    }
                                }
                            }else {
                                System.out.println("Utilizatorul nu are dreptul de a colecta voucherul");
                            }
                        }
                    }
                    break;
                case "getVouchers" :
                    Integer userIdForGetVouchers = Integer.parseInt(eventsValues[0]);
                    Vector<User> usersCollectionForGetVouchers  = vms.getUsersCollection();
                    for(User user:usersCollectionForGetVouchers){
                        if(userIdForGetVouchers == user.getUser_ID()) {
                            if (User.UserType.GUEST.equals(user.getuType())) {
                                Vector<Campaign> campaignCollection = vms.getCampaignCollection();
                                UserVoucherMap userVoucherMap = user.getUserVoucherMap();
                                Vector<Voucher> vouchersFromUser = new Vector<Voucher>();
                                for(Campaign campaign: campaignCollection){
                                    if(userVoucherMap != null) {
                                        if (userVoucherMap.containsKey(campaign.getCampaignID())) {
                                            vouchersFromUser.addAll(userVoucherMap.get(campaign.getCampaignID()));
                                        }
                                    }
                                }
                                System.out.println(vouchersFromUser.toString());
                                outputWriter.write(vouchersFromUser.toString());
                                outputWriter.write("\n");
                            }
                        }
                    }
                    break;
                case "getObservers" :
                    Integer userIdForGetObservers = Integer.parseInt(eventsValues[0]);
                    Vector<User> usersCollectionForGetObservers  = vms.getUsersCollection();
                    for(User user:usersCollectionForGetObservers) {
                        if (userIdForGetObservers == user.getUser_ID()) {
                            if (User.UserType.ADMIN.equals(user.getuType())) {
                                Vector<Campaign> campaignCollection = vms.getCampaignCollection();
                                for(Campaign campaign: campaignCollection){
                                    if(campaign.getCampaignID() == Integer.parseInt(eventsValues[2])){
                                        Vector<User> observatori = campaign.getObservers();
                                        if(observatori != null) {
                                            System.out.println(observatori.toString());
                                            outputWriter.write(observatori.toString());
                                            outputWriter.write("\n");
                                        }
                                    }
                                }
                            }
                            else{
                                System.out.println("Utilizatorul nu are acces la observatori");
                            }
                        }
                    }
                    break;
                case "getNotifications" :
                    Integer userIdForGetNotifications = Integer.parseInt(eventsValues[0]);
                    Vector<User> usersCollectionForGetNotifications  = vms.getUsersCollection();
                    for(User user: usersCollectionForGetNotifications) {
                        if (userIdForGetNotifications == user.getUser_ID()) {
                            if (User.UserType.GUEST.equals(user.getuType())) {
                                Vector<Notification> notifications = user.getNotifications();
                                    System.out.println(notifications.toString());
                                outputWriter.write(notifications.toString());
                                outputWriter.write("\n");
                            }
                            else{
                                System.out.println("Userul nu are dreptul de a accesa notificarile");
                            }
                        }
                    }
                    break;
            }
        }
        outputWriter.close();
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
