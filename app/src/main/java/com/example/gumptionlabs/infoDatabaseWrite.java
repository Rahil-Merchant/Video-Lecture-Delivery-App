package com.example.gumptionlabs;

public class infoDatabaseWrite {
    private String email,imei,fname,lname,mob,created_time,last_login;
    private Boolean isPaid,isDisabled,isDeleted;

    public infoDatabaseWrite(){

    }

    public infoDatabaseWrite(String email, String imei, String fname, String lname, String mob, String created_time, String last_login, Boolean isPaid, Boolean isDisabled, Boolean isDeleted) {
        this.email = email;
        this.imei = imei;
        this.fname = fname;
        this.lname = lname;
        this.mob = mob;
        this.created_time = created_time;
        this.last_login = last_login;
        this.isPaid = isPaid;
        this.isDisabled = isDisabled;
        this.isDeleted = isDeleted;
    }

    public String getEmail() {
        return email;
    }

    public String getImei() {
        return imei;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getMob() {
        return mob;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getLast_login() {
        return last_login;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }
}

