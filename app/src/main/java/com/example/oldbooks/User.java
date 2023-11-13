package com.example.oldbooks;

import androidx.annotation.NonNull;

import java.util.List;

public class User {

    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private int coin;
    private String Country;
    private String Province;
    private String City;
    private String Town;
    private String joinedDate;
    private String profileImg;
    private String phoneNumber;
    private List<String> favPostId;

    public User() {}

    public User(String userId, String username, String firstName, String lastName, String email, int coin, String country, String province, String city, String town, String joinedDate, String profileImg, String phoneNumber, List<String> favPostId) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.coin = coin;
        this.Country = country;
        this.Province = province;
        this.City = city;
        this.Town = town;
        this.joinedDate = joinedDate;
        this.profileImg = profileImg;
        this.phoneNumber = phoneNumber;
        this.favPostId = favPostId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getCoin() {
        return coin;
    }
    public void setCoin(int coin) {
        this.coin = coin;
    }
    public String getCountry() {
        return Country;
    }
    public void setCountry(String country) {
        Country = country;
    }
    public String getProvince() {
        return Province;
    }
    public void setProvince(String province) {
        Province = province;
    }
    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }
    public String getTown() {
        return Town;
    }
    public void setTown(String town) {
        Town = town;
    }
    public String getJoinedDate() {
        return joinedDate;
    }
    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }
    public String getProfileImg() {
        return profileImg;
    }
    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public List<String> getFavPostId() {
        return favPostId;
    }
    public void setFavPostId(List<String> favPostId) {
        this.favPostId = favPostId;
    }
}
