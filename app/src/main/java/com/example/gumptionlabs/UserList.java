package com.example.gumptionlabs;

public class UserList {

    private String created_time,email,fname,imei,last_login,lname,mob;
    private Boolean isPaid, isDisabled;

    public UserList()
    {
        //empty const needed
    }

    public UserList(String created_time, String email, String fname, String imei, String last_login, String lname, String mob, Boolean isPaid, Boolean isDisabled) {
        this.created_time = created_time;
        this.email = email;
        this.fname = fname;
        this.imei = imei;
        this.last_login = last_login;
        this.lname = lname;
        this.mob = mob;
        this.isPaid = isPaid;
        this.isDisabled = isPaid;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getEmail() {
        return email;
    }

    public String getFname() {
        return fname;
    }

    public String getImei() {
        return imei;
    }

    public String getLast_login() {
        return last_login;
    }

    public String getLname() {
        return lname;
    }

    public String getMob() {
        return mob;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }
}


