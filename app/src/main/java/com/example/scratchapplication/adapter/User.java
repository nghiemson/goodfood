package com.example.scratchapplication.adapter;

import java.util.ArrayList;
    public class User {
        public String userName;
        public String avatar;
        public String address;
        public int likes;
        public ArrayList followers;
        public String phone;
        public String id;

        public User() {
        }

        public User(String userName, String avatar, String address, int likes, ArrayList followers,String phone, String id){
            this.userName = userName;
            this.avatar = avatar;
            this.address = address;
            this.likes = likes;
            this.followers = followers;
            this.phone = phone;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public ArrayList getFollowers() {
            return followers;
        }

        public void setFollowers(ArrayList followers) {
            this.followers = followers;
        }
    }
