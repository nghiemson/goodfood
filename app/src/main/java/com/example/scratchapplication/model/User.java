package com.example.scratchapplication.model;

import java.util.ArrayList;
    public class User {
        public String userName;
        public String avatar;
        public String address;
        public int likes;
        public ArrayList followers;

        public User() {
        }

        public User(String userName, String avatar, String address, int likes) {
            this.userName = userName;
            this.avatar = avatar;
            this.address = address;
            this.likes = likes;
        }

        public User(String userName, String avatar, String address, int likes, ArrayList followers){
            this.userName = userName;
            this.avatar = avatar;
            this.address = address;
            this.likes = likes;
            this.followers = followers;
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
