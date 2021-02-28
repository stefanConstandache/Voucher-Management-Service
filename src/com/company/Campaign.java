package com.company;

import java.time.LocalDateTime;
import java.util.Vector;


public class Campaign {
    private int campaignID;
    private String name;
    private String desc;
    private LocalDateTime start_date;


    private LocalDateTime end_date;
    private int nr_vou;
    private int nr_vou_disp;

    private int voucherId = 1;

    enum CampaignStatusType{
        NEW,STARTED,EXPIRED,CANCELLED
    }

    CampaignStatusType c_type;

    CampaignVoucherMap campaignVoucherMap;
    Vector<User> observers;

    enum StrategyType{
        A,B,C
    }
    StrategyType s_type;

    public Campaign(int id, String nume, String des, LocalDateTime sd, LocalDateTime ed, int nr_v, CampaignStatusType t, StrategyType s_t){
        this.campaignID = id;
        this.name = nume;
        this.desc = des;
        this.start_date = sd;
        this.end_date = ed;
        this.nr_vou = nr_v;
        this.nr_vou_disp = nr_v;
        this.c_type = t;
        this.s_type =s_t;
    }


    public void generateVoucher(String email, String voucherType, float value){
        System.out.println("campaignId: " + this.getCampaignID());
        if(this.getNr_vou_disp() == 0){
            System.out.println("Nu mai exista vouchere disponibile");
            return;
        }
        VMS vms = VMS.getInstance();
        Vector<User> users = vms.getUsersCollection();
        int ok = 0;
        for(User user: users){
            if(user.getEmail().equals(email)){
                ok = 1;
                break;
            }
        }
        if(ok == 0){
            System.out.println("Nu exista user cu acest email");
            return;
        }
        int nr = this.getNr_vou_disp();
        this.setNr_vou_disp(--nr);
        switch(voucherType){
            case "LoyaltyVoucher":
                Integer voucherIdLoyality = this.getVoucherId();
                LoyalityVoucher loyalityVoucher = new LoyalityVoucher(voucherIdLoyality, this.campaignID, email, Voucher.VoucherStatusType.UNUSED, value);
                voucherIdLoyality++;
                this.setVoucherId(voucherIdLoyality);
                CampaignVoucherMap campaignLoyalityVoucherMap = this.getCampaignVoucherMap();
                if(campaignLoyalityVoucherMap == null){
                    campaignLoyalityVoucherMap = new CampaignVoucherMap();
                }
                campaignLoyalityVoucherMap.addVoucher(loyalityVoucher);
                this.setCampaignVoucherMap(campaignLoyalityVoucherMap);
                addVoucherToUser(loyalityVoucher, email);
                System.out.println("voucherId : " + loyalityVoucher.getId());
                System.out.println( "Voucher-ul de Loyalty a fost adaugat cu succes.");
                VMS vmsLoyalty = VMS.getInstance();
                Vector<User> userCollectionLoyalty = vmsLoyalty.getUsersCollection();
                for(User user: userCollectionLoyalty){
                    if(user.getEmail().equals(email)){
                        this.addObserver(user);
                    }
                }
                break;
            case "GiftVoucher":
                Integer voucherIdGift = this.getVoucherId();
                GiftVoucher giftVoucher = new GiftVoucher(voucherIdGift, this.campaignID, email, Voucher.VoucherStatusType.UNUSED, value);
                voucherIdGift++;
                this.setVoucherId(voucherIdGift);
                CampaignVoucherMap campaignGiftVoucherMap = this.getCampaignVoucherMap();
                if(campaignGiftVoucherMap == null){
                    campaignGiftVoucherMap = new CampaignVoucherMap();
                }
                campaignGiftVoucherMap.addVoucher(giftVoucher);
                this.setCampaignVoucherMap(campaignGiftVoucherMap);
                addVoucherToUser(giftVoucher, email);
                System.out.println("voucherId : " + giftVoucher.getId());
                System.out.println( "Voucher-ul de Gift a fost adaugat cu succes.");
                VMS vmsGift = VMS.getInstance();
                Vector<User> userCollectionGift = vmsGift.getUsersCollection();
                for(User user: userCollectionGift){
                    if(user.getEmail().equals(email)){
                        this.addObserver(user);
                    }
                }
                break;
        }
    }

    public void addObserver(User user){
        Vector<User> observer = this.getObservers();
        if(observer == null) {
            observer = new Vector<User>();
        }
        if(!observer.contains(user)) {
            observer.add(user);
        }
        this.setObservers(observer);
    }

    private void addVoucherToUser(Voucher voucher, String email) {
        VMS vms = VMS.getInstance();
        Vector<User> userCollection = vms.getUsersCollection();
        for (User user : userCollection) {
            if (user.getEmail().equals(email)) {
                UserVoucherMap userVoucherMap = user.getUserVoucherMap();
                if(userVoucherMap == null){
                    userVoucherMap = new UserVoucherMap();
                }
                userVoucherMap.addVoucher(voucher);
                user.setUserVoucherMap(userVoucherMap);
            }
        }
    }

    public void redeemVoucher(Integer voucherId, LocalDateTime localDate) {
        System.out.println("campaignId: " + this.getCampaignID());
        CampaignVoucherMap campaignVoucherMap = this.getCampaignVoucherMap();
        if(campaignVoucherMap == null){
            return;
        }

        VMS vms = VMS.getInstance();
        Vector<User> userCollection = vms.getUsersCollection();

        for (User user : userCollection) {
            UserVoucherMap userVoucherMap = user.getUserVoucherMap();
            if(userVoucherMap == null){
                continue;
            }
            if(userVoucherMap.containsKey(this.getCampaignID())) {
                Vector<Voucher> vouchersFromUserRedeemVoucher = userVoucherMap.get(this.getCampaignID());
                for (Voucher voucher : vouchersFromUserRedeemVoucher) {
                    if (voucher.getId().equals(voucherId)) {
                        if (Voucher.VoucherStatusType.UNUSED.equals(voucher.getvType())) {
                            voucher.setUseDate(localDate);
                            voucher.setvType(Voucher.VoucherStatusType.USED);
                            userVoucherMap.put(this.getCampaignID(), vouchersFromUserRedeemVoucher);
                            user.setUserVoucherMap(userVoucherMap);
                            System.out.println("Voucherul a fost folosit cu succes");
                            break;
                        }
                    }
                }
            }
        }


    }

    public void removeObserver(User user){
        Vector<User> observers =  this.getObservers();
        observers.remove(user);
        this.setObservers(observers);
    }

    public void notifyAllObservers(Notification notification){
        Vector<User> observers = this.getObservers();
        for(User observer: observers){
            Vector<String> coduriVouchere = new Vector<String>();
            UserVoucherMap userVoucherMap = observer.getUserVoucherMap();
            if(userVoucherMap.containsKey(notification.getCampaign_ID())){
                Vector<Voucher> vouchersForCodes = userVoucherMap.get(notification.getCampaign_ID());
                for(Voucher voucher: vouchersForCodes){
                    coduriVouchere.add(voucher.getCOD());
                }
            }
            notification.setCOD_voucher(coduriVouchere);
            Notification notificationForObserver = new Notification(notification.getSend_date(), notification.getCampaign_ID(), notification.getCOD_voucher(), notification.getN_type());
            Vector<Notification> notifications = observer.getNotifications();
            if(notifications == null){
                notifications = new Vector<Notification>();
            }
            notifications.add(notificationForObserver);
            observer.setNotifications(notifications);
        }

    }

    public int getCampaignID() {
        return campaignID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public int getNr_vou() {
        return nr_vou;
    }

    public void setNr_vou(int nr_vou) {
        this.nr_vou = nr_vou;
    }

    public int getNr_vou_disp(){
        return nr_vou_disp;
    }

    public void setNr_vou_disp(int nr_vou_disp){
        this.nr_vou_disp = nr_vou_disp;
    }

    public CampaignStatusType getC_type() {
        return c_type;
    }

    public void setC_type(CampaignStatusType c_type) {
        this.c_type = c_type;
    }

    public StrategyType getS_type() {
        return s_type;
    }

    public void setS_type(StrategyType s_type) {
        this.s_type = s_type;
    }
    public void setCampaignID(int campaignID) {
        this.campaignID = campaignID;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public CampaignVoucherMap getCampaignVoucherMap() {
        return campaignVoucherMap;
    }

    public void setCampaignVoucherMap(CampaignVoucherMap campaignVoucherMap) {
        this.campaignVoucherMap = campaignVoucherMap;
    }

    public Vector<User> getObservers() {
        return observers;
    }

    public void setObservers(Vector<User> observers) {
        this.observers = observers;
    }
}
