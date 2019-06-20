package com.example.gumptionlabs;

public class purchaseDatabaseWrite {
    private String email, payment_mode, purchase_timestamp;
    private int amount;

    public purchaseDatabaseWrite(){

    }

    public purchaseDatabaseWrite(int amount, String email, String payment_mode, String purchase_timestamp) {
        this.amount = amount;
        this.email = email;
        this.payment_mode = payment_mode;
        this.purchase_timestamp = purchase_timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public String getEmail() {
        return email;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public String getPurchase_timestamp() {
        return purchase_timestamp;
    }
}
