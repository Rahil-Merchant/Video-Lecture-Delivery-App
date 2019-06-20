package com.example.gumptionlabs;

public class MyCourse {
    private int amount;
    private String name, payment_mode, purchase_timestamp;

    public MyCourse()
    {

    }

    public MyCourse(int amount, String name, String payment_mode, String purchase_timestamp) {
        this.amount = amount;
        this.name = name;
        this.payment_mode = payment_mode;
        this.purchase_timestamp = purchase_timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public String getPurchase_timestamp() {
        return purchase_timestamp;
    }
}
