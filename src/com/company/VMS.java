package com.company;

import java.util.Vector;

public class VMS {
    private static VMS single_instance = null;
    private Vector<Campaign> campaignCollection = new Vector<Campaign>();
    private Vector<User> usersCollection = new Vector<User>();

    private VMS() {
    }

    public static VMS getInstance(){
        if (single_instance == null)
            single_instance = new VMS();

        return single_instance;
    }

    public void addCampaign (Campaign campaign){
        campaignCollection.add(campaign);
    }

    public String updateCampaign (Integer id, Campaign campaign){
        for(Campaign campaignToUpdate: campaignCollection){
            if(campaignToUpdate.getCampaignID() == id){
                if(Campaign.CampaignStatusType.NEW.equals(campaignToUpdate.getC_type())) {
                    campaignToUpdate.setName(campaign.getName());
                    campaignToUpdate.setDesc(campaign.getDesc());
                    campaignToUpdate.setEnd_date(campaign.getEnd_date());
                    return "Campania a fost updatata cu succes.";
                }
                else if(Campaign.CampaignStatusType.STARTED.equals(campaignToUpdate.getC_type())){
                    campaignToUpdate.setEnd_date(campaign.getEnd_date());
                    Integer nrOfDistributedVouchers = campaignToUpdate.getNr_vou() - campaignToUpdate.getNr_vou_disp();
                    if (campaign.getNr_vou() >= nrOfDistributedVouchers) {
                        campaignToUpdate.setNr_vou(campaign.getNr_vou());
                        campaignToUpdate.setNr_vou_disp(campaign.getNr_vou());
                    }
                    return "Campania a fost updatata cu succes.";
                }
                else{
                    return "Campania nu poate fi modificata deoarece campania nu se mai afla in statusul NEW sau STARTED.";
                }
            }
        }
        return "Campania nu a fost gasita";
    }

    public String cancelCampaign (Integer id){
        Vector<Campaign> CampaignCollection = this.getCampaignCollection();
        for(Campaign campaignToCancel: CampaignCollection){
            if(campaignToCancel.getCampaignID() == id){
                if(Campaign.CampaignStatusType.NEW.equals(campaignToCancel.getC_type()) || Campaign.CampaignStatusType.STARTED.equals(campaignToCancel.getC_type())) {
                    campaignToCancel.setC_type(Campaign.CampaignStatusType.CANCELLED);
                    return "Campania a fost anulata cu succes.";
                }else{
                    return "Campania nu poate fi modificata deoarece campania nu se mai afla in statusul NEW sau STARTED";
                }
            }
        }
        return "Campania nu a fost gasita";
    }
    public void addUser (User user){
        usersCollection.add(user);
    }

    public Vector<Campaign> getCampaignCollection() {
        return campaignCollection;
    }

    public Campaign getCampaign(Integer campaignId){
        for(Campaign campaign: this.campaignCollection){
            if(campaign.getCampaignID() == campaignId)
                return campaign;
        }
        return null;
    }

    public void setCampaignCollection(Vector<Campaign> campaignCollection) {
        this.campaignCollection = campaignCollection;
    }

    public Vector<User> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Vector<User> usersCollection) {
        this.usersCollection = usersCollection;
    }

}
