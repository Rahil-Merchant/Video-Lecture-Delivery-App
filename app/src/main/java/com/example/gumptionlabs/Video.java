package com.example.gumptionlabs;

import java.util.Date;

public class Video {
    private String Length,Password;
    private Date Timestamp;
    private String Video_Name, Video_URL;
    private int seq_no;

    public Video()
    {
        //empty const needed
    }

    public Video(String length, String password, Date timestamp, String video_Name, String video_URL, int seq_no) {
        Length = length;
        Password = password;
        Timestamp = timestamp;
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

    public Date getTimestamp() {
        return Timestamp;
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
