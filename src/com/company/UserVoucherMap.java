package com.company;

import java.util.Vector;

public class UserVoucherMap extends ArrayMap<Integer, Vector> {
    boolean addVoucher(Voucher v){

        if (containsKey(v.campaign_ID)) {
            Vector<Voucher> val = get(v.campaign_ID);

            int i;
            for(i = 0; i < val.size(); i++)
                if(val.get(i).ID == v.ID)
                    return false;

            val.add(v);
            val = put(v.campaign_ID, val);
            return true;
        }
        else{
            Vector<Voucher> val = new Vector<Voucher>();
            val.add(v);
            val = put(v.campaign_ID, val);
            return true;
        }
    }
}
