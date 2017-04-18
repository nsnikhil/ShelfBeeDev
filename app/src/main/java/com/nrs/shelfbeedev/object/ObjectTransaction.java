package com.nrs.shelfbeedev.object;



public class ObjectTransaction {

    private String buyerId,sellerId,payBuyer,paySeller,buytime,transStatus,bookId;

    public ObjectTransaction(String bId,String sId,String pBy,String pSl,String bTm,String tSts,String bkId){
        buyerId = bId;
        sellerId = sId;
        payBuyer = pBy;
        paySeller = pSl;
        buytime = bTm;
        transStatus = tSts;
        bookId = bkId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getPayBuyer() {
        return payBuyer;
    }

    public String getPaySeller() {
        return paySeller;
    }

    public String getBuytime() {
        return buytime;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public String getBookId() {
        return bookId;
    }
}

