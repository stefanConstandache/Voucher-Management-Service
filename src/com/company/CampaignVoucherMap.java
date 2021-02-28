package com.company;

import java.util.Vector;

public class CampaignVoucherMap extends ArrayMap<String, Vector> {
    boolean addVoucher(Voucher v){

        if (containsKey(v.email)) {
            Vector<Voucher> val = get(v.email);

            int i;
            for(i = 0; i < val.size(); i++)
                if(val.get(i).ID == v.ID)
                    return false;

            val.add(v);
            put(v.email, val);
            return true;
        }
        else{
            Vector<Voucher> val = new Vector<Voucher>();
            val.add(v);
            put(v.email, val);
            return true;
        }
    }
}
