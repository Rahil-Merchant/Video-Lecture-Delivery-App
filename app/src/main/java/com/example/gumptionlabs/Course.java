package com.example.gumptionlabs;

public class Course {
    private String name;
    private String description;
    private int video_count;
    private int amount;

    public Course()
    {
        //empty const needed
    }

    public Course(String name, String description, int video_count, int amount) {
        this.name = name;
        this.description = description;
        this.video_count = video_count;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getVideo_count() {
        return video_count;
    }

    public int getAmount() {
        return amount;
    }
}
