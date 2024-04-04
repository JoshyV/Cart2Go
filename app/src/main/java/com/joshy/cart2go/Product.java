package com.joshy.cart2go;

public class Product {
    private String barcode;
    private String brand;
    private String variant;
    private String volume;
    private String description;

    // Constructor, getters, and setters

    public Product(String barcode, String brand, String variant, String volume, String description) {
        this.barcode = barcode;
        this.brand = brand;
        this.variant = variant;
        this.volume = volume;
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getBrand() {
        return brand;
    }

    public String getVariant() {
        return variant;
    }

    public String getVolume() {
        return volume;
    }

    public String getDescription() {
        return description;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
