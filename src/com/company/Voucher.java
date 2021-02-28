package com.company;

import java.time.LocalDateTime;

public abstract class Voucher {
    int ID;
    String COD;
    int campaign_ID;
    String email;
    LocalDateTime useDate;

    enum VoucherStatusType{
        USED,UNUSED
    }
    VoucherStatusType vType;

    float reducere;
    float suma;

    public void setVType(Voucher.VoucherStatusType type){
        this.vType = type;
    }

    public void setUseDate(LocalDateTime date){
        this.useDate = date;
    }

    public Integer getId(){ return this.ID; }

    public String getEmail(){ return this.email; }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCOD() {
        return COD;
    }

    public void setCOD(String COD) {
        this.COD = COD;
    }

    public int getCampaign_ID() {
        return campaign_ID;
    }

    public void setCampaign_ID(int campaign_ID) {
        this.campaign_ID = campaign_ID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getUseDate() {
        return useDate;
    }

    public VoucherStatusType getvType() {
        return vType;
    }

    public void setvType(VoucherStatusType vType) {
        this.vType = vType;
    }

    public float getReducere() {
        return reducere;
    }

    public void setReducere(float reducere) {
        this.reducere = reducere;
    }

    public float getSuma() {
        return suma;
    }

    public void setSuma(float suma) {
        this.suma = suma;
    }
}

