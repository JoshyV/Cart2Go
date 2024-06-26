package com.joshy.cart2go.backend;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    public static class Request {
        private String username;
        private String password;

        public Request(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class Response {
        @SerializedName("message")
        private String message;

        @SerializedName("user")
        private User user;

        public String getMessage() {
            return message;
        }

        public User getUser() {
            return user;
        }
        public static class User {
            @SerializedName("ID")
            private int id;
            @SerializedName("admin")
            private int admin;
            @SerializedName("addproduct")
            private int addproduct;
            @SerializedName("productlist")
            private int productlist;
            @SerializedName("addinventory")
            private int addinventory;
            @SerializedName("inventory")
            private int inventory;
            @SerializedName("generatecrate")
            private int generatecrate;
            @SerializedName("quicksearch")
            private int quicksearch;
            @SerializedName("username")
            private String username;


            public int getId() {
                return id;
            }

            public int getAdmin() {
                return admin;
            }

            public int getAddproduct() {
                return addproduct;
            }

            public int getProductlist() {
                return productlist;
            }

            public int getaddInventory() {
                return addinventory;
            }

            public int getInventory() {
                return inventory;
            }

            public int getGenerateCrate() {
                return generatecrate;
            }

            public int getQuickSearch() {
                return quicksearch;
            }

            public String getUsername() {
                return username;
            }
        }
    }
}
