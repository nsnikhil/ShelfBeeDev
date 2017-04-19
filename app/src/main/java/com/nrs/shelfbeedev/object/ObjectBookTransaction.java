package com.nrs.shelfbeedev.object;


import java.io.Serializable;

public class ObjectBookTransaction  implements Serializable {

    private String name, publisher, description, cateogory, condition, photo0, photo1, photo2, photo3, photo4, photo5, photo6, photo7, userSellerId,
            buyerId,payBuyer,paySeller,buytime,transStatus;
    private int bookid, costPrice, sellingPrice, edition , status;

    public ObjectBookTransaction(int id, String nm, String pb, int cp, int sp, int ed, String condt, String cat, String des, String usrd,
                      String pic0, String pic1, String pic2, String pic3, String pic4, String pic5, String pic6, String pic7, int bookstatus,
                                 String bId,String pBy,String pSl,String bTm,String tSts) {
        bookid = id;
        name = nm;
        publisher = pb;
        costPrice = cp;
        sellingPrice = sp;
        edition = ed;
        condition = condt;
        cateogory = cat;
        description = des;
        userSellerId = usrd;
        photo0 = pic0;
        photo1 = pic1;
        photo2 = pic2;
        photo3 = pic3;
        photo4 = pic4;
        photo5 = pic5;
        photo6 = pic6;
        photo7 = pic7;
        status = bookstatus;
        buyerId = bId;
        payBuyer = pBy;
        paySeller = pSl;
        buytime = bTm;
        transStatus = tSts;
    }

    public int getBookid() {
        return bookid;
    }

    public String getUserSellerId() {
        return userSellerId;
    }

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getCostPrice() {
        return costPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public int getEdition() {
        return edition;
    }

    public String getCondition() {
        return condition;
    }

    public String getCateogory() {
        return cateogory;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto0() {
        return photo0;
    }

    public String getPhoto1() {
        return photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public String getPhoto6() {
        return photo6;
    }

    public String getPhoto7() {
        return photo7;
    }

    public int getStatus() {
        return status;
    }

    public String getBuyerId() {
        return buyerId;
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


}
