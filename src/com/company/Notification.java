package com.company;

import java.time.LocalDateTime;
import java.util.Vector;

public class Notification {

    LocalDateTime send_date;
    int Campaign_ID;
    Vector<String> COD_voucher;

    enum NotificationType{
        EDIT,CANCEL
    }
    NotificationType n_type;

    public Notification(LocalDateTime d, int id, Vector<String> cod, NotificationType t){
        this.send_date = d;
        this.Campaign_ID = id;
        this.COD_voucher = cod;
        this.n_type = t;
    }

    public Vector<String> getCOD_voucher() {
        return COD_voucher;
    }

    public void setCOD_voucher(Vector<String> COD_voucher) {
        this.COD_voucher = COD_voucher;
    }

    public int getCampaign_ID() {
        return Campaign_ID;
    }

    public void setCampaign_ID(int campaign_ID) {
        Campaign_ID = campaign_ID;
    }

    public String toString(){
        return Campaign_ID + " " + COD_voucher.toString() + " " + send_date + " " + n_type;
    }

    public LocalDateTime getSend_date() {
        return send_date;
    }

    public void setSend_date(LocalDateTime send_date) {
        this.send_date = send_date;
    }

    public NotificationType getN_type() {
        return n_type;
    }

    public void setN_type(NotificationType n_type) {
        this.n_type = n_type;
    }
}
