package com.nrs.shelfbeedev.object;



public class ObjectUser {

    private String uid,name,phoneno,address,fkey,bstatus;

    public ObjectUser(String ud,String nm,String ph,String ad,String fk,String bst){
        uid = ud;
        name = nm;
        phoneno = ph;
        address = ad;
        fkey = fk;
        bstatus = bst;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getAddress() {
        return address;
    }

    public String getFkey() {
        return fkey;
    }

    public String getBstatus() {
        return bstatus;
    }
}
