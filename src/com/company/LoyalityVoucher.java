package com.company;

import java.util.Date;

public class LoyalityVoucher extends Voucher {


    public LoyalityVoucher(int id, int campaign_id, String em,  VoucherStatusType t, float r){
        this.ID = id;
        this.COD = "SC-" + id + "-" + new Date().getTime();
        this.campaign_ID = campaign_id;
        this.email = em;
        this.vType = t;
        this.reducere = r;
    }


    public String toString(){
        return "[" + ID + ";" + vType + ";" + email + ";" + reducere + ";" + campaign_ID +  ";" + useDate + "]";
    }
}
