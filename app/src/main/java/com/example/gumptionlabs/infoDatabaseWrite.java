package com.example.gumptionlabs;

public class infoDatabaseWrite {
    private String email,imei,fname,uname,mob;

    public infoDatabaseWrite(){

    }

    public infoDatabaseWrite(String email, String imei, String fname, String uname, String mob) {
        this.email = email;
        this.imei = imei;
        this.fname = fname;
        this.uname = uname;
        this.mob = mob;
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

    public String getUname() {
        return uname;
    }

    public String getMob() {
        return mob;
    }
}

