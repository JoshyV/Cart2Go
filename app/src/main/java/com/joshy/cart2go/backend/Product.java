package com.joshy.cart2go.backend;

import com.google.gson.annotations.SerializedName;

public class Product {
    private String barcode;
    private String brand;
    private String variant;
    private String volume;
    private String description;

    @SerializedName("token")
    private String token;

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

    public String getToken() {
        return token;
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


    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
