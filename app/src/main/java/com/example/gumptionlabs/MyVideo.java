package com.example.gumptionlabs;

import com.google.firebase.Timestamp;

public class MyVideo {

    private String Length, Password;
    private Timestamp Timestamp;
    private String Updated_By, Video_Name, Video_URL;
    private int seq_no;

    public MyVideo()
    {

    }

    public MyVideo(String length, String password, com.google.firebase.Timestamp timestamp, String updated_By, String video_Name, String video_URL, int seq_no) {
        Length = length;
        Password = password;
        Timestamp = timestamp;
        Updated_By = updated_By;
        Video_Name = video_Name;
        Video_URL = video_URL;
        this.seq_no = seq_no;
    }

    public String getLength() {
        return Length;
    }

    public String getPassword() {
        return Password;
    }

    public com.google.firebase.Timestamp getTimestamp() {
        return Timestamp;
    }

    public String getUpdated_By() {
        return Updated_By;
    }

    public String getVideo_Name() {
        return Video_Name;
    }

    public String getVideo_URL() {
        return Video_URL;
    }

    public int getSeq_no() {
        return seq_no;
    }
}
