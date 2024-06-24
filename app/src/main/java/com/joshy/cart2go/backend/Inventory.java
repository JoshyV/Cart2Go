package com.joshy.cart2go.backend;

public class Inventory {
    private String barcode;
    private String crate;
    private String expiry;
    private String quantity;

    public Inventory(String barcode, String crate, String expiry, String quantity) {
        this.barcode = barcode;
        this.crate = crate;
        this.expiry = expiry;
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getCrate() {
        return crate;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getQuantity() {
        return quantity;
    }
}