package com.company;

import java.util.Date;

public class GiftVoucher extends Voucher {


    public GiftVoucher(int id, int campaign_id, String em, VoucherStatusType t, float s) {
        this.ID = id;
        this.COD = "SC-" + id + "-" + new Date().getTime();
        this.campaign_ID = campaign_id;
        this.email = em;
        this.vType = t;
        this.suma = s;
    }

    public String toString() {
        return "[" + ID + ";" + vType + ";" + email + ";" + suma + ";" + campaign_ID + ";" + useDate + "]";
    }
}
