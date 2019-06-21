package com.example.gumptionlabs;

public class purchaseUserDatabaseWrite {
    private String name;
    private int amount;
    private String payment_mode, purchase_timestamp;

    public purchaseUserDatabaseWrite(){

    }

    public purchaseUserDatabaseWrite(String name, int amount, String payment_mode, String purchase_timestamp) {
        this.name = name;
        this.amount = amount;
        this.payment_mode = payment_mode;
        this.purchase_timestamp = purchase_timestamp;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public String getPurchase_timestamp() {
        return purchase_timestamp;
    }
}
